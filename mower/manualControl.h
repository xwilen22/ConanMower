/*
 * readBT(unsigned char *, MeBluetooth *)
 * Input, char array, MeBluetooth address 
 * reads the bluetooth and writes the data to the 
 * unsigned char array
 */

void readBT(unsigned char *btBuffer, MeBluetooth *bluetooth)
{ 
  int readdata = 0,count = 0;
  if (bluetooth->available())
  {
    while((readdata = bluetooth->read()) != (int)-1)
    { 
      btBuffer[count] = readdata;
      count++;
      //bluetooth->write(readdata);
      
      delay(1);
    } 
  }
}
