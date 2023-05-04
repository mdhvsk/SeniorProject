int ledPin = 13; // Pin number for the LED

void setup() {
  Serial.begin(9600); // Initialize serial communication at 9600 baud rate
  pinMode(ledPin, OUTPUT); // Set LED pin as output
}

void loop() {
  if (Serial.available() > 0) {
    byte incomingByte = Serial.read();
    if (incomingByte != -1) {
      digitalWrite(ledPin, HIGH);
      delay(500);
      digitalWrite(ledPin, LOW);
    }
  }
}

