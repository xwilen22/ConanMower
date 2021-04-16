
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
  
  if (nrOfBytes == 1) { // kolla så att det är en giltig type också?
    
    command->type = bluetooth->read();  
  }
  else if (nrOfBytes == 2) {
    command->type = bluetooth->read(); // kolla så att det är en giltig type också?
    delay(1);
    command->command = 'H'; // kolla så att det är en giltigt command också?
  }
  bluetooth->flush(); // funkar flush?
}
