#include <MeAuriga.h>
#include <Wire.h>
#include <SoftwareSerial.h>

#include "heartbeat.h"

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

#define degreeTime 14 // calculated on 150 speed. 12v.  OLD

#define HEARTBEATTIMEOUT 3000

#include "motor.h"
#include "ledRing.h"


struct Commands {
  unsigned char type;
  unsigned char command;
  bool heartBeat;
};

#include "manualControl.h"


Motor motor(11, 49, 48, 10, 47, 46);
int motorSpeed = 75;


MeBluetooth bluetooth(PORT_16);
MeSerial piSerial(PORT_5);

MeLineFollower lineFinder(PORT_10);
MeUltrasonicSensor ultraSensor(PORT_9);
//MeGyro gyro(1,0x69);
//MeBuzzer buzzer;

MeRGBLed led( 0, LEDNUM );
LedRing ledRing(&led);

enum autonomousSM_t {
  FORWARD,
  REVERSE,
  TURN
};

struct Commands btCommand = {AUTONOMOUS, MSTOP};

autonomousSM_t autonomousSM = FORWARD;

long millisCounter = 0;

const int reverseLength = 500;
const int minObstacleDistance = 5;

Heartbeat bluetoothHeartbeat(HEARTBEATTIMEOUT);


void setup() {

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
        motor.moveSpeed(-motorSpeed);
        millisCounter = millis();
      }
      break;

    case REVERSE:
      //      if (isLine() || isObstacle()) {
      //        motor.brake();
      //        autonomousSM = FORWARD;
      //      }

      if (millis() > millisCounter + reverseLength) {
        //bluetooth.println("Reverse");
        motor.brake();
        turnAngle = motor.turnAngle(30, 70);
        autonomousSM = TURN;
        //gyro.begin();
        motor.turnLeft(motorSpeed);
        millisCounter = millis();
      }
      break;

    case TURN:
      //      if (isLine() || isObstacle()) {
      //        motor.brake();
      //        autonomousSM = REVERSE;
      //      }
      //gyro.update();
      //bluetooth.println("Turn");
      if (millis() > millisCounter + turnAngle * degreeTime) {
        //if (abs(gyro.getAngleZ()) >= 90) {
        motor.brake();
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
