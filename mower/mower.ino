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
struct Commands prevCommand = btCommand;

autonomousSM_t autonomousSM = FORWARD;

int motorSpeed = 75;

long millisCounter = 0;

long heartBeatCounter = millis();
bool bluetoothConnected;

const int reverseLength = 500;

const int minObstacleDistance = 5;
int turnAngle = 30;


Motor motor(11, 49, 48, 10, 47, 46);

void setup() {

  randomSeed(analogRead(0));
  bluetooth.begin(115200);    //The factory default baud rate is 115200
  //piSerial.begin(115200);

  //buzzer.setpin(45);

  Serial2.begin(9600);

  ledRing.startUpBlink(50, 50 , 0);

}


void loop() {


  if (bluetooth.available()) {
    readBT(&btCommand , &bluetooth);
  }


  // buzzer.tone(65,0.25);

  //ledRing.colorLoop(100,25,0);
  if (btCommand.type == MANUAL) {




    if (prevCommand.command != btCommand.command) {

      if (prevCommand.type == AUTONOMOUS) {
        motor.brake();
      }

      manualController(btCommand.command);
      saveBtCommand(true);
    }
  }
  else if (btCommand.type == AUTONOMOUS) {


    if (prevCommand.type == MANUAL) {
      autonomousSM = FORWARD;
      ledRing.fullCirlce(0, 100, 0); // Green
    }

    autonomousStateMachine();
    saveBtCommand(false);
  }
  else if (btCommand.type == LINEFOLLOW) {
    lineFollow();
    saveBtCommand(false);
  }


  // HeartBeat stuff
  if (btCommand.heartBeat) {
    heartBeatCounter = millis();
    bluetoothConnected = true;
    btCommand.heartBeat = false;
  }
  if (millis() > heartBeatCounter + HEARTBEATTIMEOUT) {
    bluetoothConnected = false;
  }


  delay(20); //Without delay bt commands are not handled right

}


bool isLine() {
  return lineFinder.readSensors() == S1_IN_S2_IN;
}

bool isObstacle() {
  return ultraSensor.distanceCm() < minObstacleDistance;
}

void saveBtCommand(bool command) {
  prevCommand.type = btCommand.type;

  if (command) {
    prevCommand.command = btCommand.command;
  }
  else {
    prevCommand.command = 0;
  }
}


void manualController(char command) {

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


void autonomousStateMachine() {

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
