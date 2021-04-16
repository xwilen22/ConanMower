#ifndef LedRing_h
#define LedRing_h

#include "Arduino.h"
#include <MeRGBLed.h>


class LedRing
{
  public:
    LedRing(MeRGBLed* led);
   /*
 * colorLoop(int,int,int);
 * Input, MeRGBLed address, int for r,g,b light strength
 * runs a single color, 3 range loop on the machine
 * Must be called in a loop
 */
    void colorLoop(int r, int g, int b);
    /*
 * fullCircle(int,int,int);
 * Input, MeRGBLed address, int for red,green,blue light strength
 * Colors the machine in rgb input light
 * Can be called once
 */
    void fullCirlce(int r, int g, int b);
  private:
    int ledNbr = 0;
    int maxLeds;
    MeRGBLed* _pled;
  
};
#endif
