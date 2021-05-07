#include <MeAuriga.h>
#include <Wire.h>
#include <SoftwareSerial.h>

#define LEDNUM 12

#define AUTONOMOUS 'O'
#define LINEFOLLOW '_'
#define MANUAL 'M'
#define MSTOP 'S'
#define MLEFT 'L'
#define MRIGHT 'R'
#define MFORWARD 'F'
#define MREVERSE 'B'
#define HEARTBEAT 'd'

#define HEARTBEATTIMEOUT 4000

#include "heartbeat.h"
#include "motor.h"
#include "ledRing.h"
#include "communicationControl.h"
#include "motorEncoder.h"


const int reverseLength = -10;
const int minObstacleDistance = 5;

bool newSession = false;
bool turnLeft = true;
bool obstacle = false;
int motorSpeed = 75;

MeEncoderOnBoard Encoder_1(SLOT1);
MeEncoderOnBoard Encoder_2(SLOT2);
MotorEncoder motorEncoder = MotorEncoder();

Motor motor(11, 49, 48, 10, 47, 46);

MeBluetooth bluetooth(PORT_16);
MeSerial piSerial(PORT_5);

MeLineFollower lineFinder(PORT_10);
MeUltrasonicSensor ultraSensor(PORT_9);

MeRGBLed led( 0, LEDNUM );
LedRing ledRing(&led);

Heartbeat bluetoothHeartbeat(HEARTBEATTIMEOUT);

enum autonomousSM_t {
  FORWARD,
  REVERSE,
  TURN
};
autonomousSM_t autonomousSM = FORWARD;

struct Commands btCommand = {AUTONOMOUS, MSTOP};

void isr_process_encoder1(void) {
  if (digitalRead(Encoder_1.getPortB()) == 0) {
    motorEncoder.addPulseRight();
  }
  else {
    motorEncoder.subtractPulseRight();
  }
}

void isr_process_encoder2(void) {
  if (digitalRead(Encoder_2.getPortB()) == 0) {
    motorEncoder.subtractPulseLeft();
  }
  else {
    motorEncoder.addPulseLeft();
  }
}



void setup() {
  attachInterrupt(Encoder_1.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(Encoder_2.getIntNum(), isr_process_encoder2, RISING);

  randomSeed(analogRead(0));
  bluetooth.begin(115200);    //The factory default baud rate is 115200
  piSerial.begin(9600);

  ledRing.startUpBlink(50, 50 , 0);
}


void loop() {

  if (bluetooth.available()) {
    readBT(&btCommand , &bluetooth);
  }

  setState(btCommand);


  if (bluetoothHeartbeat.isTimeout()) {   // Bluetooth is not connected
    ledRing.fullCirlce(100, 0, 100);
    btCommand.command = MSTOP;
  }
  else {                                  //Bluetooth is connected
    ledRing.fullCirlce(0, 0, 100);
  }
  if (btCommand.heartBeat) {
    bluetoothHeartbeat.beat();
    btCommand.heartBeat = false;

  }

  delay(20);                              //Without delay bt commands are not handled right
}


void setState(Commands command) {

  static unsigned char prevType = ' ';

  switch (command.type) {

    case MANUAL:
      manualController(command.command);
      break;

    case AUTONOMOUS:
      if (command.type != prevType) {
        newSession = true;
        autonomousSM = FORWARD;
        motorEncoder.startMeasuring();
      }
      autonomousStateMachine();
      break;

    case LINEFOLLOW:
      lineFollow();
      break;

    default:
      break;
  }

  prevType = command.type;
}



void manualController(char command) {

  static unsigned char prevCommand = ' ';

  if (command != prevCommand) {
    switch (command) {
      case MFORWARD:
        motor.moveSpeed(motorSpeed);
        break;

      case MREVERSE:
        motor.moveSpeed(-motorSpeed);
        break;

      case MLEFT:
        motor.turnLeft(motorSpeed);
        break;

      case MRIGHT:
        motor.turnRight(motorSpeed);
        break;

      case MSTOP:
        motor.brake();
        break;

      default:
        break;
    }
  }

  prevCommand = command;
}


void autonomousStateMachine() {

  static int turnAngle = 30;
  static int distance = 0;
  static int angleTurned = 0;

  switch (autonomousSM) {
    case FORWARD:
      motor.moveSpeed(motorSpeed);
      if ( (obstacle = isObstacle()) || isLine()) {
        motor.brake();
        delay(50);
        distance = motorEncoder.getDistance();

        motorEncoder.startMeasuring();
        
        autonomousSM = REVERSE;
        delay(50);

        motor.moveSpeed(-motorSpeed);
      }
      break;

    case REVERSE:
      if (motorEncoder.getDistance() <= reverseLength) {
        motor.brake();
        delay(50);

        motorEncoder.startMeasuring();
        autonomousSM = TURN;
        delay(50);

        turnAngle = motor.turnAngle(30, 70);
        motor.turnLeft(motorSpeed);
      }
      break;

    case TURN:
      if ((angleTurned = motorEncoder.getAngle()) >= turnAngle) {
        motor.brake();
        delay(50);
        
        motorEncoder.startMeasuring();
        autonomousSM = FORWARD;

        sendToRbp(&piSerial, turnLeft, angleTurned, distance, obstacle, newSession);
        newSession = false;
      }
      break;
  }
}




bool isLine() {
  return (lineFinder.readSensors() == S1_IN_S2_IN);
}

bool isObstacle() {
  return (ultraSensor.distanceCm() < minObstacleDistance);
}


void lineFollow() {
  int lineState = lineFinder.readSensors();

  switch (lineState)
  {
    case S1_IN_S2_IN:
      motor.turnLeft(motorSpeed);
      break;
    case S1_IN_S2_OUT:
      motor.turnLeft(motorSpeed);
      break;
    case S1_OUT_S2_IN:
      motor.moveSpeed(motorSpeed);
      break;
    case S1_OUT_S2_OUT:
      //motor.turnRight(motorSpeed);
      motor.moveSpeed(motorSpeed);
      break;
    default: break;
  }
}
