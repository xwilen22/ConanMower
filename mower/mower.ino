#include <MeAuriga.h>
#include <Wire.h>

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

enum direction_t {
  LEFT,
  RIGHT
};

autonomousSM_t autonomousSM = FORWARD;


int motorSpeed = 50;

const int reverseLength = 200;
const int turnLength = 300;

const int minObstacleDistance = 5;

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

void setup() {

  Serial.begin(115200);
   
  attachInterrupt(motorL.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(motorR.getIntNum(), isr_process_encoder2, RISING);

  
  encoderMotorSetup();

}

void encoderMotorSetup() {
  //Set PWM 8KHz
  TCCR1A = _BV(WGM10);
  TCCR1B = _BV(CS11) | _BV(WGM12);

  TCCR2A = _BV(WGM21) | _BV(WGM20);
  TCCR2B = _BV(CS21);

  motorL.setPulse(9);
  motorR.setPulse(9);
  motorL.setRatio(39.267);
  motorR.setRatio(39.267);
  motorL.setPosPid(1.8,0,1.2);
  motorR.setPosPid(1.8,0,1.2);
  motorL.setSpeedPid(0.18,0,0);
  motorR.setSpeedPid(0.18,0,0);
}

void autonomousStateMachine() {
  
    switch (autonomousSM) {
    case FORWARD:
        motorL.runSpeed(-motorSpeed);
        motorR.runSpeed(motorSpeed);
        Serial.println("Forward");

        if (ultraSensor.distanceCm() < minObstacleDistance) {
          autonomousSM = REVERSE;

          motorL.runSpeed(0);
          motorR.runSpeed(0);
          delay(100);
          
          
          motorL.move(reverseLength,motorSpeed);
          motorR.move(-reverseLength,motorSpeed);
        } 
        else if  (lineFinder.readSensors() == S1_IN_S2_IN) {

          motorL.runSpeed(0);
          motorR.runSpeed(0);
          delay(100);
          
          
          autonomousSM = REVERSE;
          motorL.move(reverseLength,motorSpeed);
          motorR.move(-reverseLength,motorSpeed);
        }
      break;
      
    case REVERSE:
      Serial.println("Reverse");
      if (motorL.isTarPosReached() && motorR.isTarPosReached()) {
        autonomousSM = TURN;

        motorL.runSpeed(0);
        motorR.runSpeed(0);
        delay(100);
        
          
        motorL.runSpeed(motorSpeed);
        motorR.runSpeed(motorSpeed);
        gyro.begin();
      }
    break;

    case TURN:
      //if (motorL.isTarPosReached() && motorR.isTarPosReached()) {
      gyro.update();
      Serial.println(gyro.getAngleZ());
      
      if (abs(gyro.getAngleZ()) >= 30) {
        motorL.runSpeed(0);
        motorR.runSpeed(0);
        delay(100);
        
        autonomousSM = FORWARD;
        
      }
    break;
  }

}

void loop()
{ 

  autonomousStateMachine();
  
  motorL.loop();
  motorR.loop();

}
