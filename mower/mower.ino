#include <MeAuriga.h>
#include <Wire.h>
#include <SoftwareSerial.h>

#define LEDNUM 12

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

unsigned char btBuffer[128] = {'A'};
int motorSpeed = 50;

const int reverseLength = 200;
const int turnLength = 300;

const int minObstacleDistance = 5;
int turnAngle = 30;

Motor motor(&motorL, &motorR);
char oldCommand[2] = {'B'};


void setup() {

  Serial.begin(115200);
  
  attachInterrupt(motorL.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(motorR.getIntNum(), isr_process_encoder2, RISING);
  randomSeed(analogRead(0));
  bluetooth.begin(115200);    //The factory default baud rate is 115200

  led.setpin( 44 );
  
}


void loop()
{ 
  readBT(btBuffer,&bluetooth);


  if(btBuffer[0] == 'M'){
    oldCommand[0] = btBuffer[0];
    oldCommand[1] = btBuffer[1];
    manualController(btBuffer[1]);
  }
  else if(btBuffer[0] == 'A'){
    oldCommand[0] = btBuffer[0];
    oldCommand[1] = btBuffer[1];
    autonomousStateMachine();
  }
  else{
    btBuffer[0] = oldCommand[0];
    btBuffer[1] = oldCommand[1];
  }
   
  Serial.print(char(oldCommand[0]));
  Serial.println(char(oldCommand[1]));
  
  
  motorL.loop();
  motorR.loop();

}

void manualController(char command){
  motor.brake();
  
  switch(command){
    case 'F':
      motor.moveSpeed(motorSpeed);
      fullCirlce(&led, 0,0,50);
    break;

    case 'B':
      motor.moveSpeed(-motorSpeed);
      fullCirlce(&led, 0,50,0);
    break;
    
    case 'L':
      motor.turnLeft(motorSpeed);
      fullCirlce(&led, 0,50,50);
    break;
    
    case 'R':
      motor.turnRight(motorSpeed);
    break;
    
    case 'S':
      motor.brake();
    break;
  }
}

void autonomousStateMachine() {
    fullCirlce(&led, 50,50,50);
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

          fullCirlce(&led, 150,0,0);

          motor.brake();
          delay(100);
          
          
            
          autonomousSM = REVERSE;
          motor.moveLength(-reverseLength,motorSpeed);
        }
      break;
      
    case REVERSE:
      if (motorL.isTarPosReached() && motorR.isTarPosReached()) {
        fullCirlce(&led, 0,0,0);
        
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
