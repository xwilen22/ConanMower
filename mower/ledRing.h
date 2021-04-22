#ifndef LedRing_h
#define LedRing_h

#include "Arduino.h"
#include <MeRGBLed.h>


class LedRing
{
  public:
    LedRing(MeRGBLed* led);

 /**
 *   \Function
 *   colorLoop
 *   \Description
 *   runs a single color in a circle, 3 long
 *   \Param[in]
 *   r,g,b, color values
 *   \Others
 *   Must be called in loop
 **/
    void colorLoop(int r, int g, int b);

  /**
 *   \Function
 *   fullCircle
 *   \Description
 *   colors the machine
 *   \Param[in]
 *   r,g,b, color values to use
 **/
    void fullCirlce(int r, int g, int b);

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
 *   Is blocking
 **/
  void startUpBlink(int r, int g, int b);
    
  private:
    int ledNbr = 0;
    int maxLeds;
    MeRGBLed* _pled;
  
};
#endif
