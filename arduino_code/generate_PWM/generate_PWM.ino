// Pin number for PWM output
const int pwmPin = 2;

// Variables for timing
unsigned long previousMillis = 0; // Stores the previous time
unsigned long intervalOn = 5000; // Default interval for ON time in milliseconds (5 seconds)
unsigned long intervalOff = 1000; // Default interval for OFF time in milliseconds (2 seconds)
int dutyCycle = 90; // Default Duty cycle in percentage (80%)
String incomingByte;
// boolean flagPWM = false; 
boolean flagPWM; 
String incomingString = "";
char incomingChar;

int timeVal = 0;
int cycleTotal = 0;
int cycleIndex = 0;
int[] intensity = {25, 50, 75, 75, 50, 25}
int intensityIndex = -1;

boolean motorRunning = false;


// flag to start signal

// Function to generate PWM signal
void generatePWM(unsigned long onTime, unsigned long offTime, int duty) {
  unsigned long currentMillis = millis(); // Get the current time
  unsigned long elapsedMillis = currentMillis - previousMillis; // Calculate elapsed time since last update

  // Check if it's time to turn ON the PWM signal
  if (elapsedMillis <= onTime && !digitalRead(pwmPin)) {
    // Set PWM signal ON with specified duty cycle
    analogWrite(pwmPin, duty * 255 / 100); // Convert duty cycle percentage to PWM value
  }

  // Check if it's time to turn OFF the PWM signal
  if (elapsedMillis > onTime && digitalRead(pwmPin)) {
    // Set PWM signal OFF
    analogWrite(pwmPin, 0); // 0% duty cycle (fully OFF)
  }

  // Check if it's time to update the PWM timing
  if (elapsedMillis >= onTime + offTime) {
    // Update the previousMillis and intervalOff
    previousMillis = currentMillis;
    intervalOff = offTime;
    
  }
}

void generateEaglePWM(unsigned long onTime, unsigned long offTime, int cycles){


}
// use this section for initializing code
void setup() {
  // Set the PWM pin as an OUTPUT
  pinMode(pwmPin, OUTPUT);
  pinMode(12,OUTPUT);
  pinMode(11,OUTPUT);
  Serial.begin(9600);

}

String getValue(String data, char separator, int index)
{
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;


    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}


// this section loops indefinitely
void loop() {

  if(Serial.available() > 0 ){
      incomingString = Serial.readString();

      String start = getValue(incomingString, ':', 0);
      String timeStr = getValue(incomingString, ':', 1);
      String cycleStr = getValue(incomingString, ':', 2);

      timeVal = timeStr.toInt() * 60 * 1000;
      cycleTotal = intenStr.toInt();

      if(start == "s" && timeVal == 3 && cycleVal == 50){
        flagPWM = true;
        intervalOn = cycleVal;
      }
  }




  if (flagPWM) {
    generatePWM(intervalOn, intervalOff, dutyCycle);
    digitalWrite(11,HIGH);
    digitalWrite(12,LOW);
  }
  else if (flagPWM == false) {
    generatePWM(intervalOn, intervalOff, 0);
    digitalWrite(11,LOW);
    digitalWrite(12,HIGH);
  }


}
