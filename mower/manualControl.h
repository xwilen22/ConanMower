
 /**
 *   \Function
 *     readBT
 *   \Description
 *   reads the bluetooth and writes the data to the btBuffer
 *   \Param[in]
 *   btBuffer *, MeBluetooth *
 **/


void readBT(struct Commands *command, MeBluetooth *bluetooth)
{ 
  
  int readdata = 0,count = 0;
  int nrOfBytes = bluetooth->available();
  
  unsigned char data = ' ';
  
  if (nrOfBytes == 1) { // kolla så att det är en giltig type också?
    
    data = bluetooth->read();  
    command->type = data;
  }
  else if (nrOfBytes == 2) {
    
    data = bluetooth->read(); // kolla så att det är en giltig type också?
    command->type = data;
    delay(1);
    data = bluetooth->read(); // kolla så att det är en giltigt command också?
    command->command = data;
  }

  while (bluetooth->available()) {
    bluetooth->read();  
  }
}

void sendToRbp(MeSerial *piSerial, boolean turnLeft, int degree, uint16_t distance, boolean obstacle){
    /*
    piSerial->write(turnLeft);
    piSerial->write(degree);
    uint8_t msb = (distance >> 8) & 0xff;
    uint8_t lsb = distance & 0xff;
    piSerial->write(msb);
    piSerial->write(lsb);
    piSerial->write(obstacle);
    */
    
    piSerial->println(turnLeft);
    piSerial->println(degree);
    uint8_t msb = (distance >> 8) & 0xff;
    uint8_t lsb = distance & 0xff;
    piSerial->println(msb);
    piSerial->println(lsb);
    piSerial->println(obstacle);
    
}
