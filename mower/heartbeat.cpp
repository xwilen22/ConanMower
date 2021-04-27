#include "heartbeat.h"

Heartbeat::Heartbeat(int timeout) {
  _timeout = timeout;
  _active = false;
  _timestamp = 0;
}

void Heartbeat::activate() {
  _active = true;
}

void Heartbeat::deactivate() {
  _active = false;
}

void Heartbeat::beat() {
  _timestamp = millis();
}

bool Heartbeat::isTimeout() {
  return millis() > _timestamp + _timeout && _active;
}
