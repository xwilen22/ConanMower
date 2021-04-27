#ifndef Heartbeat_h
#define Heartbeat_h

#include "Arduino.h"

class Heartbeat
{
  public:
    Heartbeat(int timeout);
   
    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    void activate();

    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    void deactivate();

    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    void beat();
    
    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    bool isTimeout();

  private:
    bool _active;
    int _timeout;
    long _timestamp;
};


#endif
