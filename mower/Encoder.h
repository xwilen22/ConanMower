#ifndef Encoder_h
#define Encoder_h

#include "Arduino.h"

#define CMPULSES 17
#define DEGREEPULSES 2.2

class Encoder
{
  public:
    Encoder();
    /**
            \Function

            \Description

            \Param[in]

            \Ouput

            \Return

            \Others
        **/
    void addPulseLeft();

    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    void subtractPulseLeft();

    /**
            \Function

            \Description

            \Param[in]

            \Ouput

            \Return

            \Others
        **/
    void addPulseRight();

    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    void subtractPulseRight();


    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    void startMeasureLeft();



    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    void startMeasureRight();

    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    long getDistanceLeft();

    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    long getDistanceRight();


    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    int getAngleLeft();

    /**
        \Function

        \Description

        \Param[in]

        \Ouput

        \Return

        \Others
    **/
    int getAngleRight();

  private:
    long posLeft;
    long posRight;
};


#endif
