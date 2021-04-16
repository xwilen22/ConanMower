#include <MeAuriga.h>
#include <Wire.h>
#include <SoftwareSerial.h>

#define LEDNUM 12

#define AUTONOMOUS 'O'
#define LINEFOLLOW '_'
#define MANUAL 'M'
#define MSTOP 'S'
#define MLEFT 'L'
#define MRIGHT 'R'
#define MFORWARD 'F'
#define MREVERSE 'B'

#include "motor.h"
#include "ledRing.h"

struct Commands {
  unsigned char type;
  unsigned char command;
};

#include "manualControl.h"

MeBluetooth bluetooth(PORT_16);

MeEncoderOnBoard motorL(SLOT1);
MeEncoderOnBoard motorR(SLOT2);

MeLineFollower lineFinder(PORT_10);
MeUltrasonicSensor ultraSensor(PORT_9);
MeGyro gyro(1,0x69);

MeRGBLed led( 0, LEDNUM );


enum autonomousSM_t {
  FORWARD,
  LINE,
  REVERSE,
  TURN
};


struct Commands btCommand = {LINEFOLLOW, MSTOP};
struct Commands prevCommand = btCommand;


autonomousSM_t autonomousSM = FORWARD;

//unsigned char btBuffer[128] = {AUTONOMOUS};

int motorSpeed = 50;

const int reverseLength = 200;
const int turnLength = 300;

const int minObstacleDistance = 5;
int turnAngle = 30;

LedRing ledRing(&led);
Motor motor(&motorL, &motorR);

//char oldCommand[2] = {MSTOP};


void setup() {

  Serial.begin(115200);
  
  attachInterrupt(motorL.getIntNum(), isr_process_encoder1, RISING);
  attachInterrupt(motorR.getIntNum(), isr_process_encoder2, RISING);
  randomSeed(analogRead(0));
  bluetooth.begin(115200);    //The factory default baud rate is 115200

  //led.setpin( 44 );

  //fullCirlce(&led, 0,0,50);
  delay(1000);
  ledRing.fullCirlce(0,50,0);
  
}


void loop() { 
  readBT(&btCommand ,&bluetooth);

 
  ledRing.colorLoop(100,25,0);
  if(btCommand.type == MANUAL){

    if (prevCommand.command != btCommand.command) {

      Serial.println(char(btCommand.command));
      
      if (prevCommand.type == AUTONOMOUS) {
        motor.brake();
      }
      
      manualController(btCommand.command);
      saveBtCommand(true);
    }
  }
  else if(btCommand.type == AUTONOMOUS){
    autonomousStateMachine();
    saveBtCommand(false);
  }
  else if(btCommand.type == LINEFOLLOW){
    saveBtCommand(false);
  }
  else{
    btCommand.type = prevCommand.type;
    btCommand.command = prevCommand.command;
  }
   
  //Serial.print(char(oldCommand[0]));
  //Serial.println(char(oldCommand[1]));
  
  motorL.loop();
  motorR.loop();
   
}

void saveBtCommand(bool command) {
  prevCommand.type = btCommand.type;
  
  if (command) {
      prevCommand.command = btCommand.command;
  }
  else {
    prevCommand.command = 0;
  }
}


void manualController(char command){
  
  switch(command){
    case MFORWARD:
      motor.moveSpeed(motorSpeed);
    //  fullCirlce(&led, 0,0,50);
    break;

    case MREVERSE:
      motor.moveSpeed(-motorSpeed);
      //fullCirlce(&led, 0,50,0);
    break;
    
    case MLEFT:
      motor.turnLeft(motorSpeed);
     // fullCirlce(&led, 0,50,50);
    break;
    
    case MRIGHT:
      motor.turnRight(motorSpeed);
    break;
    
    case MSTOP:
      motor.brake();
    break;

    default:
    break;
  }
}

void autonomousStateMachine() {
   // fullCirlce(&led, 50,50,50);
    switch (autonomousSM) {
    case FORWARD:
        motor.moveSpeed(motorSpeed);
        
        if (ultraSensor.distanceCm() < minObstacleDistance) {
          
          motor.brake();
          delay(100);
          
          autonomousSM = REVERSE;
          motor.moveLength(-reverseLength,motorSpeed);
        } 
        else if  (lineFinder.readSensors() == S1_IN_S2_IN) {
          
          //fullCirlce(&led, 150,0,0);

          motor.brake();
          delay(100);
                  
          autonomousSM = REVERSE;
          motor.moveLength(-reverseLength,motorSpeed);
        }
      break;
      
    case REVERSE:
      if (motorL.isTarPosReached() && motorR.isTarPosReached()) {
      //  fullCirlce(&led, 0,0,0);
        
        motor.brake();
        delay(100);
        turnAngle = motor.turnAngle(45,100);
        autonomousSM = TURN;
        motor.turnLeft(motorSpeed);
        gyro.begin();
      }
    break;

    case TURN:
      gyro.update();
      
      if (abs(gyro.getAngleZ()) >= turnAngle) {
        motor.brake();
        delay(100);
        
        autonomousSM = FORWARD;
      }
    break;
  }
}

void lineFollow(){
    int lineState = lineFinder.readSensors();
    switch(lineState)
  {
    case S1_IN_S2_IN: 
      //Serial.println("Sensor 1 and 2 are inside of black line"); 
      motor.turnLeft(motorSpeed);
      break;
    case S1_IN_S2_OUT: 
      //Serial.println("Sensor 2 is outside of black line"); 
      motor.turnLeft(motorSpeed);
      break;
    case S1_OUT_S2_IN: 
      //Serial.println("Sensor 1 is outside of black line"); 
      motor.moveSpeed(motorSpeed);
      break;
    case S1_OUT_S2_OUT:
      //Serial.println("Sensor 1 and 2 are outside of black line"); 
      motor.moveSpeed(motorSpeed);
      break;
    default: break;
  }
}

void isr_process_encoder1(void) {
  if(digitalRead(motorL.getPortB()) == 0) {
    motorL.pulsePosMinus();
  }
  else {
    motorL.pulsePosPlus();;
  }
}

void isr_process_encoder2(void) {
  if(digitalRead(motorR.getPortB()) == 0) {
    motorR.pulsePosMinus();
  }
  else {
    motorR.pulsePosPlus();
  }
}
