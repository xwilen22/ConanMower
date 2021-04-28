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
#define HEARTBEAT 'H'

#include "heartbeat.h"
#include "motor.h"
#include "ledRing.h"
#include "manualControl.h"
#include "Encoder.h"


#define degreeTime 14 // calculated on 150 speed. 12v.  OLD

const int reverseLength = -10;
const int minObstacleDistance = 5;

#define HEARTBEATTIMEOUT 3000

MeEncoderOnBoard Encoder_1(SLOT1);
MeEncoderOnBoard Encoder_2(SLOT2);
Encoder encoder = Encoder();

Motor motor(11, 49, 48, 10, 47, 46);
int motorSpeed = 40;

MeBluetooth bluetooth(PORT_16);
MeSerial piSerial(PORT_5);

MeLineFollower lineFinder(PORT_10);
MeUltrasonicSensor ultraSensor(PORT_9);
//MeGyro gyro(1,0x69);
//MeBuzzer buzzer;

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


void isr_process_encoder1(void)
{
  if(digitalRead(Encoder_1.getPortB()) == 0)
  {
    encoder.addPulseRight();
  }
  else
  {
    encoder.subtractPulseRight();
  }
}

void isr_process_encoder2(void)
{
  if(digitalRead(Encoder_2.getPortB()) == 0)
  {
    encoder.subtractPulseLeft();
  }
  else
  {
    encoder.addPulseLeft();
  }
}



void setup() {
  attachInterrupt(Encoder_1.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(Encoder_2.getIntNum(), isr_process_encoder2, RISING);

  randomSeed(analogRead(0));
  bluetooth.begin(115200);    //The factory default baud rate is 115200
  piSerial.begin(9600);

  //buzzer.setpin(45);

  //Serial2.begin(9600);

  ledRing.startUpBlink(50, 50 , 0);

  bluetoothHeartbeat.activate();

}


void loop() {



  


  if (bluetooth.available()) {
    readBT(&btCommand , &bluetooth);
  }

  pathTaker(btCommand);


  if (bluetoothHeartbeat.isTimeout()) {
    // Bluetooth is not connected
  }
  else {
    //Bluetooth is connected
  }
  if (btCommand.heartBeat) {
    bluetoothHeartbeat.beat();
  }

  delay(20); //Without delay bt commands are not handled right
 
}


void pathTaker(Commands command) {

  switch (command.type) {
    
    case MANUAL:
      manualController(command.command);
      autonomousSM = FORWARD; // To be sure to start at FORWARD when going back to autonomous
      break;

    case AUTONOMOUS:
      autonomousStateMachine();
      break;

    case LINEFOLLOW:
      lineFollow();
      break;

    default:
      break;
  }
}



void manualController(char command) {

  static unsigned char prevCommand = ' ';

  if (command != prevCommand) {
    switch (command) {
      case MFORWARD:
        ledRing.fullCirlce(100, 100, 100); // White
        motor.moveSpeed(motorSpeed);
        break;

      case MREVERSE:
        ledRing.fullCirlce(0, 0, 0); // Off
        motor.moveSpeed(-motorSpeed);
        break;

      case MLEFT:
        ledRing.fullCirlce(0, 0, 100); // Blue
        motor.turnLeft(motorSpeed);
        break;

      case MRIGHT:
        ledRing.fullCirlce(100, 100, 0); // Yellow
        motor.turnRight(motorSpeed);
        break;

      case MSTOP:
        ledRing.fullCirlce(100, 0, 0); // Red
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

  switch (autonomousSM) {
    case FORWARD:
      motor.moveSpeed(motorSpeed);
      //bluetooth.println("forward");

      if (isLine() || isObstacle()) {

        motor.brake();
        //bluetooth.println("forward + line/obs");
        autonomousSM = REVERSE;

        motor.brake();
        delay(100);
        encoder.startMeasureLeft();
        
        motor.moveSpeed(-motorSpeed);
        
      }
      break;

    case REVERSE:
      //      if (isLine() || isObstacle()) {
      //        motor.brake();
      //        autonomousSM = FORWARD;
      //      }
      
      if (encoder.getDistanceLeft() <= reverseLength) {
        //bluetooth.println("Reverse");
        motor.brake();
        turnAngle = motor.turnAngle(30, 70);
        autonomousSM = TURN;
        motor.brake();
        delay(100);
        encoder.startMeasureLeft();
        motor.turnLeft(motorSpeed);
      }
      break;

    case TURN:
      //      if (isLine() || isObstacle()) {
      //        motor.brake();
      //        autonomousSM = REVERSE;
      //      }
          
      debugOnRpi(String(encoder.getAngleLeft()));
      if (encoder.getAngleLeft() >= turnAngle) {
        motor.brake();
        delay(100);
        autonomousSM = FORWARD;
      }
      break;
  }
}


bool isLine() {
  return lineFinder.readSensors() == S1_IN_S2_IN;
}

bool isObstacle() {
  return ultraSensor.distanceCm() < minObstacleDistance;
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
