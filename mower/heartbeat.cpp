#include "heartbeat.h"

Heartbeat::Heartbeat(int timeout) {
  _timeout = timeout;
  _timestamp = -timeout;
}

void Heartbeat::beat() {
  _timestamp = millis();
}

bool Heartbeat::isTimeout() {
  return (millis() > _timestamp + _timeout);
}
