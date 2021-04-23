
#include "motor.h"


Motor::Motor(int pwmMotor1, int dir_M1_1, int dir_M1_2, int pwmMotor2, int dir_M2_1, int dir_M2_2) {
    
    _pwmMotor1 = pwmMotor1;
    _dir_M1_1 = dir_M1_1;
    _dir_M1_2 = dir_M1_2;
    
    _pwmMotor2 = pwmMotor2;
    _dir_M2_1 = dir_M2_1;
    _dir_M2_2 = dir_M2_2;

    pinMode(_pwmMotor1,OUTPUT);    //We have to set PWM pin as output
    pinMode(_dir_M1_1,OUTPUT);  //Logic pins are also set as output
    pinMode(_dir_M1_2,OUTPUT);
    pinMode(_pwmMotor2,OUTPUT);    //We have to set PWM pin as output
    pinMode(_dir_M2_1,OUTPUT);  //Logic pins are also set as output
    pinMode(_dir_M2_2,OUTPUT);
}


void Motor::brake() {
  analogWrite(_pwmMotor1, 0);
  analogWrite(_pwmMotor2, 0);
}

void Motor::moveSpeed(int moveSpeed) {

  
  if (moveSpeed < 0) {
    digitalWrite(_dir_M1_1, HIGH);
    digitalWrite(_dir_M1_2, LOW);
  
    digitalWrite(_dir_M2_1, LOW);
    digitalWrite(_dir_M2_2, HIGH);

    moveSpeed = -moveSpeed;
  } 
  else {
    
    digitalWrite(_dir_M1_1, LOW);
    digitalWrite(_dir_M1_2, HIGH);
  
    digitalWrite(_dir_M2_1, HIGH);
    digitalWrite(_dir_M2_2, LOW);
  }

  analogWrite(_pwmMotor1,moveSpeed);
  analogWrite(_pwmMotor2,moveSpeed);
}

void Motor::moveLength(int moveLength, int motorSpeed) {  

  if (moveLength < 0) {
    moveSpeed(-motorSpeed);
    moveLength = -moveLength;
  }
  else {
    moveSpeed(motorSpeed);
  }
  
  delay(moveLength);
  brake();
}

void Motor::turnLeft(int moveSpeed) {
  digitalWrite(_dir_M1_1, LOW);
  digitalWrite(_dir_M1_2, HIGH);

  digitalWrite(_dir_M2_1, LOW);
  digitalWrite(_dir_M2_2, HIGH);

  analogWrite(_pwmMotor1,moveSpeed);
  analogWrite(_pwmMotor2,moveSpeed);
}


void Motor::turnRight(int moveSpeed) {
  digitalWrite(_dir_M1_1, HIGH);
  digitalWrite(_dir_M1_2, LOW);

  digitalWrite(_dir_M2_1, HIGH);
  digitalWrite(_dir_M2_2, LOW);

  analogWrite(_pwmMotor1,moveSpeed);
  analogWrite(_pwmMotor2,moveSpeed);
}


int Motor::turnAngle(int minAngle, int maxAngle){
  return random(minAngle ,maxAngle);
}
