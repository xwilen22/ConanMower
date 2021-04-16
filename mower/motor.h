#ifndef Motor_h
#define Motor_h

#include "Arduino.h"

#include <MeEncoderOnBoard.h>

class Motor
{
  public:
    Motor(MeEncoderOnBoard* leftMotor, MeEncoderOnBoard* rightMotor);
 /**
 *   \Function
 *   break
 *   \Description
 *   stops the wheels
 **/
    void brake();
     /**
 *   \Function
 *   
 *   \Description
 *   
 *   \Param[in]
 *   
 *   \Ouput
 *   
 *   \Return
 *   
 *   \Others
 **/
    void moveLength(int moveLength, int motorSpeed);
     /**
 *   \Function
 *   
 *   \Description
 *   
 *   \Param[in]
 *   
 *   \Ouput
 *   
 *   \Return
 *   
 *   \Others
 **/
    void moveSpeed(int moveSpeed);
     /**
 *   \Function
 *   
 *   \Description
 *   
 *   \Param[in]
 *   
 *   \Ouput
 *   
 *   \Return
 *   
 *   \Others
 **/
    void turnLeft(int moveSpeed);
     /**
 *   \Function
 *   
 *   \Description
 *   
 *   \Param[in]
 *   
 *   \Ouput
 *   
 *   \Return
 *   
 *   \Others
 **/
    void turnRight(int moveSpeed);
     /**
 *   \Function
 *   
 *   \Description
 *   
 *   \Param[in]
 *   
 *   \Ouput
 *   
 *   \Return
 *   
 *   \Others
 **/
    int turnAngle(int minAngle,int maxAngle);

  private:
    MeEncoderOnBoard* _pLeftMotor;
    MeEncoderOnBoard* _pRightMotor;
};


#endif
