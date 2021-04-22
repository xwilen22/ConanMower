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

#include "motor.h"
#include "ledRing.h"

struct Commands {
  unsigned char type;
  unsigned char command;
};

#include "manualControl.h"

MeBluetooth bluetooth(PORT_16);
//MeSerial piSerial(PORT_5);

MeEncoderOnBoard motorL(SLOT1);
MeEncoderOnBoard motorR(SLOT2);

MeLineFollower lineFinder(PORT_10);
MeUltrasonicSensor ultraSensor(PORT_9);
MeGyro gyro(1,0x69);

MeRGBLed led( 0, LEDNUM );


enum autonomousSM_t {
  FORWARD,
  REVERSE,
  TURN
};


struct Commands btCommand = {AUTONOMOUS, MSTOP};
struct Commands prevCommand = btCommand;


autonomousSM_t autonomousSM = FORWARD;

int motorSpeed = 50;

MeBuzzer buzzer;
const int reverseLength = 200;
const int turnLength = 300;

const int minObstacleDistance = 5;
int turnAngle = 30;

LedRing ledRing(&led);
Motor motor(&motorL, &motorR);

void setup() {

  Serial.begin(115200);
  
  attachInterrupt(motorL.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(motorR.getIntNum(), isr_process_encoder2, RISING);
  randomSeed(analogRead(0));
  bluetooth.begin(115200);    //The factory default baud rate is 115200
  //piSerial.begin(115200);

  //buzzer.setpin(45);

  ledRing.startUpBlink(50, 50 , 0);
  //sendToRbp(&piSerial, true, 30, 300, true);
}


void loop() { 
  readBT(&btCommand ,&bluetooth);
 
 // buzzer.tone(65,0.25);

  //ledRing.colorLoop(100,25,0);
  if(btCommand.type == MANUAL){

    if (prevCommand.command != btCommand.command) {
      
      if (prevCommand.type == AUTONOMOUS) {
        motor.brake();
      }
      
      manualController(btCommand.command);
    //  saveBtCommand(true);
    }
  }
  else if(btCommand.type == AUTONOMOUS){
     if (prevCommand.type == MANUAL) {
        autonomousSM = FORWARD;
      }

    autonomousStateMachine();
  //  saveBtCommand(false);
  }
  else if(btCommand.type == LINEFOLLOW){
    lineFollow();
   // saveBtCommand(false);
  }
 
  
  motorL.loop();
  motorR.loop();
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


void manualController(char command){
  
  switch(command){
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

void autonomousStateMachine() {
    switch (autonomousSM) {
    case FORWARD:
        motor.moveSpeed(motorSpeed);
        
        if (ultraSensor.distanceCm() < minObstacleDistance) {
          
          motor.brake();
          delay(100);
          
          autonomousSM = REVERSE;
          motor.moveLength(-reverseLength,motorSpeed);
        } 
        else if  (lineFinder.readSensors() == S1_IN_S2_IN) {
          motor.brake();
          delay(100);
                  
          autonomousSM = REVERSE;
          motor.moveLength(-reverseLength,motorSpeed);
        }
      break;
      
    case REVERSE:
      if (motorL.isTarPosReached() && motorR.isTarPosReached()) {
        
        motor.brake();
        delay(100);
        turnAngle = motor.turnAngle(45,100);
        autonomousSM = TURN;
        motor.turnLeft(motorSpeed);
        gyro.begin();
      }
    break;

    case TURN:
      gyro.update();
      
      if (abs(gyro.getAngleZ()) >= turnAngle) {
        motor.brake();
        delay(100);
        
        autonomousSM = FORWARD;
      }
    break;
  }
}

void lineFollow(){
    int lineState = lineFinder.readSensors();

    switch(lineState)
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

void isr_process_encoder1(void) {
  if(digitalRead(motorL.getPortB()) == 0) {
    motorL.pulsePosMinus();
  }
  else {
    motorL.pulsePosPlus();;
  }
}

void isr_process_encoder2(void) {
  if(digitalRead(motorR.getPortB()) == 0) {
    motorR.pulsePosMinus();
  }
  else {
    motorR.pulsePosPlus();
  }
}
