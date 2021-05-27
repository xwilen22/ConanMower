
struct Commands {
  unsigned char type;
  unsigned char command;
  bool heartBeat;
};


void readBT(struct Commands *command, MeBluetooth *bluetooth) {

  int readdata = 0, count = 0;
  int nrOfBytes = bluetooth->available();

  unsigned char data = ' ';

  if (nrOfBytes == 1) { // kolla så att det är en giltig type också

    data = bluetooth->read();
    if (data == HEARTBEAT) {
      command->heartBeat = true;
    }
    else {
      command->type = data;
    }
  }
  else if (nrOfBytes == 2) {

    data = bluetooth->read(); // kolla så att det är en giltig type också?
    command->type = data;

    data = bluetooth->read(); // kolla så att det är en giltigt command också?
    command->command = data;

  }

  while (bluetooth->available()) {
    bluetooth->read();
  }
}

void sendToRbp(MeSerial *piSerial, boolean turnLeft, int degree, uint16_t distance, boolean obstacle, boolean newSession) {

  piSerial->write(254); // startbyte
  piSerial->write(turnLeft);
  piSerial->write(degree);
  uint8_t msb = (distance >> 8) & 0xff;
  uint8_t lsb = distance & 0xff;
  piSerial->write(msb);
  piSerial->write(lsb);
  piSerial->write(obstacle);
  piSerial->write(newSession);

}
