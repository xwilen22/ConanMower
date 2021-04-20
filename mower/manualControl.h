
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
