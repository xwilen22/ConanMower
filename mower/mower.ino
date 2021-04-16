#include <MeAuriga.h>
#include <Wire.h>
#include <SoftwareSerial.h>

#define LEDNUM 12

#define AUTONOMOUS 'O'
#define MANUAL 'M'
#define MSTOP 'S'
#define MLEFT 'L'
#define MRIGHT 'R'
#define MFORWARD 'F'
#define MREVERSE 'B'



#include "motor.h"
#include "ledRing.h"
#include "manualControl.h"

MeBluetooth bluetooth(PORT_16);

MeEncoderOnBoard motorL(SLOT1);
MeEncoderOnBoard motorR(SLOT2);

MeLineFollower lineFinder(PORT_10);
MeUltrasonicSensor ultraSensor(PORT_9);
MeGyro gyro(1,0x69);

MeRGBLed led( 0, LEDNUM );


enum autonomousSM_t {
  FORWARD,
  LINE,
  REVERSE,
  TURN
};


autonomousSM_t autonomousSM = FORWARD;

unsigned char btBuffer[128] = {AUTONOMOUS};
int motorSpeed = 50;

const int reverseLength = 200;
const int turnLength = 300;

const int minObstacleDistance = 5;
int turnAngle = 30;

LedRing ledRing(&led);
Motor motor(&motorL, &motorR);
char oldCommand[2] = {MSTOP};


void setup() {

  Serial.begin(115200);
  
  attachInterrupt(motorL.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(motorR.getIntNum(), isr_process_encoder2, RISING);
  randomSeed(analogRead(0));
  bluetooth.begin(115200);    //The factory default baud rate is 115200

  //led.setpin( 44 );

  //fullCirlce(&led, 0,0,50);
  delay(1000);
  ledRing.fullCirlce(0,50,0);
  
}


void loop() { 
  readBT(btBuffer,&bluetooth);

 
  ledRing.colorLoop(100,25,0);
  if(btBuffer[0] == MANUAL){

    if (oldCommand[1] != btBuffer[1]) {

      Serial.println(char(btBuffer[1]));
      
      if (oldCommand[0] == AUTONOMOUS) {
        motor.brake();
      }
      
      manualController(btBuffer[1]);
      oldCommand[0] = btBuffer[0];
      oldCommand[1] = btBuffer[1];
    }
  }
  else if(btBuffer[0] == AUTONOMOUS){
    autonomousStateMachine();
    oldCommand[0] = btBuffer[0];
    oldCommand[1] = 0;
  }
  else{
    btBuffer[0] = oldCommand[0];
    btBuffer[1] = oldCommand[1];
  }
   
  //Serial.print(char(oldCommand[0]));
  //Serial.println(char(oldCommand[1]));
  
  
  motorL.loop();
  motorR.loop();
   
}

void manualController(char command){
  
  switch(command){
    case MFORWARD:
      motor.moveSpeed(motorSpeed);
    //  fullCirlce(&led, 0,0,50);
    break;

    case MREVERSE:
      motor.moveSpeed(-motorSpeed);
      //fullCirlce(&led, 0,50,0);
    break;
    
    case MLEFT:
      motor.turnLeft(motorSpeed);
     // fullCirlce(&led, 0,50,50);
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
   // fullCirlce(&led, 50,50,50);
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
          
          //fullCirlce(&led, 150,0,0);

          motor.brake();
          delay(100);
                  
          autonomousSM = REVERSE;
          motor.moveLength(-reverseLength,motorSpeed);
        }
      break;
      
    case REVERSE:
      if (motorL.isTarPosReached() && motorR.isTarPosReached()) {
      //  fullCirlce(&led, 0,0,0);
        
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
