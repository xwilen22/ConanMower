
#include "motor.h"

Motor::Motor(MeEncoderOnBoard* leftMotor, MeEncoderOnBoard* rightMotor) :
  _pLeftMotor(leftMotor), _pRightMotor(rightMotor) {

  
  TCCR1A = _BV(WGM10);
  TCCR1B = _BV(CS11) | _BV(WGM12);

  TCCR2A = _BV(WGM21) | _BV(WGM20);
  TCCR2B = _BV(CS21);

  _pLeftMotor->setPulse(9);
  _pRightMotor->setPulse(9);
  _pLeftMotor->setRatio(39.267);
  _pRightMotor->setRatio(39.267);
  _pLeftMotor->setPosPid(1.8,0,1.2);
  _pRightMotor->setPosPid(1.8,0,1.2);
  _pLeftMotor->setSpeedPid(0.18,0,0);
  _pRightMotor->setSpeedPid(0.18,0,0);
  
}


void Motor::brake() {
  _pLeftMotor->runSpeed(0);
  _pRightMotor->runSpeed(0);
}

void Motor::moveSpeed(int moveSpeed) {
  _pLeftMotor->runSpeed(-moveSpeed);
  _pRightMotor->runSpeed(moveSpeed);
}

void Motor::moveLength(int moveLength, int motorSpeed) {
  
  _pLeftMotor->move(-moveLength,motorSpeed);
  _pRightMotor->move(moveLength,motorSpeed);
          
}

void Motor::turnLeft(int moveSpeed) {
  _pLeftMotor->runSpeed(-moveSpeed);
  _pRightMotor->runSpeed(-moveSpeed);
}


void Motor::turnRight(int moveSpeed) {
  _pLeftMotor->runSpeed(moveSpeed);
  _pRightMotor->runSpeed(moveSpeed);
}
