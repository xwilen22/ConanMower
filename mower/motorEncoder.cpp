
#include "MotorEncoder.h"


MotorEncoder::MotorEncoder() {
  posLeft = 0;
  posRight = 0;
}

void MotorEncoder::addPulseLeft() {
  posLeft += 1;
}


void MotorEncoder::subtractPulseLeft() {
  posLeft -= 1;
}


void MotorEncoder::addPulseRight() {
  posRight += 1;
}


void MotorEncoder::subtractPulseRight() {
  posRight -= 1;
}


void MotorEncoder::startMeasureLeft() {
  posLeft = 0;
}

void MotorEncoder::startMeasureRight() {
  posRight = 0;
}


long MotorEncoder::getDistanceLeft() {
  return posLeft / CMPULSES;
}


long MotorEncoder::getDistanceRight() {
  return posRight / CMPULSES;
}

int MotorEncoder::getAngleLeft() {
  return abs(posLeft / DEGREEPULSES);
}

int MotorEncoder::getAngleRight() {
  return abs(posRight / DEGREEPULSES);
}


void MotorEncoder::startMeasuring() {
  startMeasureLeft();
  startMeasureRight();
}

int MotorEncoder::getDistance() {
  return (getDistanceLeft() + getDistanceRight() ) / 2;
}

int MotorEncoder::getAngle() {
  return (getAngleLeft() + getAngleRight() ) / 2;
}
