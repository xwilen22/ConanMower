#ifndef Motor_h
#define Motor_h

#include "Arduino.h"
#include "motorEncoder.h"

class Motor
{
  public:
    Motor(int pwmMotor1, int dir_M1_1, int dir_M1_2, int pwmMotor2, int dir_M2_1, int dir_M2_2);

    void brake();

    void moveSpeed(int moveSpeed);

    void turnLeft(int moveSpeed);

    void turnRight(int moveSpeed);

    int turnAngle(int minAngle, int maxAngle);

  private:
    int _pwmMotor1;
    int _dir_M1_1;
    int _dir_M1_2;

    int _pwmMotor2;
    int _dir_M2_1;
    int _dir_M2_2;

};


#endif
