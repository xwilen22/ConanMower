#ifndef Heartbeat_h
#define Heartbeat_h

#include "Arduino.h"

class Heartbeat
{
  public:
    Heartbeat(int timeout);

    void beat();

    bool isTimeout();

  private:
    int _timeout;
    long _timestamp;
};


#endif
