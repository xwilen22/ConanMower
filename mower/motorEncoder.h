#ifndef MotorEncoder_h
#define MotorEncoder_h

#include "Arduino.h"

#define CMPULSES 17
#define DEGREEPULSES 2.4

class MotorEncoder
{
  public:
    MotorEncoder();

    void addPulseLeft();

    void subtractPulseLeft();

    void addPulseRight();

    void subtractPulseRight();
 
    void MotorEncoder::startMeasuring();

    int MotorEncoder::getDistance();

    int MotorEncoder::getAngle();
    
  private:
    void startMeasureLeft();
    void startMeasureRight();
    long getDistanceLeft();
    long getDistanceRight();
    int getAngleLeft();
    int getAngleRight();
    long posLeft;
    long posRight;
};


#endif
