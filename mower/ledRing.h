
int ledNbr = 0;

void colorLoop(MeRGBLed *led, int r, int g, int b) {
    led->setColorAt((ledNbr-1+LEDNUM)%LEDNUM,0,0,0);
  
    led->setColorAt((ledNbr)  %LEDNUM , r*0.05, g*0.05, b*0.05);
    led->setColorAt((ledNbr+1)%LEDNUM , r*0.25, g*0.25, b*0.25);
    led->setColorAt((ledNbr+2)%LEDNUM , r     , g     , b);
    led->show();
    ledNbr = (ledNbr + 1) % LEDNUM;
}

void fullCirlce(MeRGBLed *led, int r, int g, int b) {
  for (int i=0; i<LEDNUM; i++) {
    led->setColorAt(i, r, g, b);
  }
  led->show();
}
