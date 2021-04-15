void readBT(unsigned char *btBuffer, MeBluetooth *bluetooth)
{ 
  int readdata = 0,count = 0;
  if (bluetooth->available())
  {
    while((readdata = bluetooth->read()) != (int)-1)
    { 
      btBuffer[count] = readdata;
      count++;
      
      delay(1);
    } 
  }
}
