
#include "Encoder.h"


Encoder::Encoder() {
  posLeft = 0;
  posRight = 0;
}

void Encoder::addPulseLeft() {
  posLeft += 1;
}


void Encoder::subtractPulseLeft() {
  posLeft -= 1;
}


void Encoder::addPulseRight() {
  posRight += 1;
}


void Encoder::subtractPulseRight() {
  posRight -= 1;
}


void Encoder::startMeasureLeft() {
  posLeft = 0;
}

void Encoder::startMeasureRight() {
  posRight = 0;
}


long Encoder::getDistanceLeft() {
  return posLeft / CMPULSES;
}


long Encoder::getDistanceRight() {
  return posRight / CMPULSES;
}

int Encoder::getAngleLeft() {
  return abs(posLeft / DEGREEPULSES);
}

int Encoder::getAngleRight() {
  return abs(posRight / DEGREEPULSES);
}
