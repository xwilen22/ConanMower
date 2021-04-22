#include "ledRing.h"

LedRing::LedRing(MeRGBLed* led):_pled(led){
  _pled->setpin(44);
  
  maxLeds = _pled->getNumber();

};
  
void LedRing::colorLoop(int r, int g, int b) {

      _pled->setColorAt((ledNbr-1+maxLeds)%maxLeds,0,0,0);
    
      _pled->setColorAt((ledNbr)  %maxLeds , r*0.05, g*0.05, b*0.05);
      _pled->setColorAt((ledNbr+1)%maxLeds , r*0.25, g*0.25, b*0.25);
      _pled->setColorAt((ledNbr+2)%maxLeds , r     , g     , b);
      _pled->show();
      ledNbr = (ledNbr + 1) % maxLeds;

}

void LedRing::fullCirlce(int r, int g, int b) {
  for (int i=0; i<maxLeds; i++) {
    _pled->setColorAt(i, r, g, b);
  }
  _pled->show();
}

void LedRing::startUpBlink(int r, int g, int b) {

  for (int i=0; i<5; i++) {
    fullCirlce(r,g,b);
    delay(100);
    fullCirlce(0,0,0);
    delay(100);  
  }
  
}
