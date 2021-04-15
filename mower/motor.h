#ifndef Motor_h
#define Motor_h

#include "Arduino.h"

#include <MeEncoderOnBoard.h>

class Motor
{
  public:
    Motor(MeEncoderOnBoard* leftMotor, MeEncoderOnBoard* rightMotor);

    void brake();
    void moveLength(int moveLength, int motorSpeed);
    void moveSpeed(int moveSpeed);
    void turnLeft(int moveSpeed);
    void turnRight(int moveSpeed);
    int turnAngle(int minAngle,int maxAngle);

  private:
    MeEncoderOnBoard* _pLeftMotor;
    MeEncoderOnBoard* _pRightMotor;
};


#endif
