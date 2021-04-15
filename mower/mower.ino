#include <MeAuriga.h>
#include <Wire.h>

#include "motor.h"

MeEncoderOnBoard motorL(SLOT1);
MeEncoderOnBoard motorR(SLOT2);

MeLineFollower lineFinder(PORT_10);
MeUltrasonicSensor ultraSensor(PORT_9);
MeGyro gyro(1,0x69);

enum autonomousSM_t {
  FORWARD,
  LINE,
  REVERSE,
  TURN
};


autonomousSM_t autonomousSM = FORWARD;


int motorSpeed = 50;

const int reverseLength = 200;
const int turnLength = 300;

const int minObstacleDistance = 5;
int turnAngle = 30;

Motor motor(&motorL, &motorR);



void setup() {

  Serial.begin(115200);
  
  attachInterrupt(motorL.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(motorR.getIntNum(), isr_process_encoder2, RISING);
  randomSeed(analogRead(0));

}


void loop()
{ 

  autonomousStateMachine();
  
  motorL.loop();
  motorR.loop();

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
