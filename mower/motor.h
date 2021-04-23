#ifndef Motor_h
#define Motor_h

#include "Arduino.h"

class Motor
{
  public:
    Motor(int pwmMotor1, int dir_M1_1, int dir_M1_2, int pwmMotor2, int dir_M2_1, int dir_M2_2);
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
    int _pwmMotor1;
    int _dir_M1_1;
    int _dir_M1_2;
    
    int _pwmMotor2;
    int _dir_M2_1;
    int _dir_M2_2;
};


#endif
