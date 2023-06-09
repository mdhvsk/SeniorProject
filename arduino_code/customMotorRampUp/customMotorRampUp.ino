#include <Adafruit_MPU6050.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>

Adafruit_MPU6050 mpu;
#define INTERVAL 5000
uint32_t lastMicros = 0; 
const int pwmPin = 2;
boolean flagPWM; 


String incomingString = "";
unsigned long previousMillis = 0;
int intervalOn;
int intervalOff = 1000;
int timeVal = 0;
int cycleTotal = 1;
int cycleIndex = 0;
int intensity[] = {25, 50, 75, 75, 50, 25};
// int intensityDummy[] = {15, 25};
int intensityIndex = -1;
String start;
String timeStr;
String cycleStr;
boolean end = false;

boolean motorRunning = false;

void generatePWM(unsigned long onTime, unsigned long offTime, int duty ){
  unsigned long currentMillis = millis(); // Get the current time
  unsigned long elapsedMillis = currentMillis - previousMillis; // Calculate elapsed time since last update

  // if (elapsedMillis <= onTime && !digitalRead(pwmPin)) {

// 10 second ramp up
  if(elapsedMillis > 0 && elapsedMillis < 10000){
    analogWrite(pwmPin, (0.6) * duty * 255 / 100);
    digitalWrite(53,HIGH);
    digitalWrite(52,LOW);
  }

  // if(elapsedMillis > 0 && elapsedMillis < 60000){
  //   analogWrite(pwmPin, (elapsedMillis/60000) * duty * 255 / 100);
  //   digitalWrite(53,HIGH);
  //   digitalWrite(52,LOW);
  // }

// 10 second ramp up run
  if (elapsedMillis <= onTime && elapsedMillis >= 10000 && onTime - elapsedMillis <= 10000) {
    // Set PWM signal ON with specified duty cycle
    analogWrite(pwmPin, duty * 255 / 100); // Convert duty cycle percentage to PWM value
    digitalWrite(53,HIGH);
    digitalWrite(52,LOW);
  }
  //   if (elapsedMillis <= onTime && elapsedMillis >= 60000 && onTime - elapsedMillis >= 60000) {
  //   // Set PWM signal ON with specified duty cycle
  //   analogWrite(pwmPin, duty * 255 / 100); // Convert duty cycle percentage to PWM value
  //   digitalWrite(53,HIGH);
  //   digitalWrite(52,LOW);
  // }

  //   if(onTime - elapsedMillis < 10000){
  //   analogWrite(pwmPin, ((onTime-elapsedMillis)/10000) * duty * 255 / 100);
  //   digitalWrite(53,HIGH);
  //   digitalWrite(52,LOW);
  // }

  //   if(onTime - elapsedMillis < 60000){
  //   analogWrite(pwmPin, ((onTime-elapsedMillis)/60000) * duty * 255 / 100);
  //   digitalWrite(53,HIGH);
  //   digitalWrite(52,LOW);
  // }

  // if (elapsedMillis > onTime && analogRead(pwmPin)) {
  if (elapsedMillis > onTime ) {

    // Set PWM signal OFF
    analogWrite(pwmPin, 0); // 0% duty cycle (fully OFF)
    digitalWrite(53,LOW);
    digitalWrite(52,HIGH);
  }

    if (elapsedMillis >= onTime + offTime) {
    // Update the previousMillis and intervalOff
    previousMillis = currentMillis;
    intervalOff = offTime;
    motorRunning = false;


  }

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


void setup() { // put your setup code here, to run once:
  pinMode(pwmPin, OUTPUT);
  pinMode(53,OUTPUT);
  pinMode(52,OUTPUT);
  Serial.begin(115200);
  	// Try to initialize!
	if (!mpu.begin()) {
		Serial.println("Failed to find MPU6050 chip");
		while (1) {
		  delay(10);
		}
	}

	// set accelerometer range to +-8G
	mpu.setAccelerometerRange(MPU6050_RANGE_8_G);

	// set gyro range to +- 500 deg/s
	mpu.setGyroRange(MPU6050_RANGE_500_DEG);

	// set filter bandwidth to 21 Hz
	mpu.setFilterBandwidth(MPU6050_BAND_21_HZ);

}

void loop () {
  sensors_event_t a, g, temp;
  mpu.getEvent(&a, &g, &temp);
  // Serial.print(a.acceleration.x);
  // Serial.print(",");
  // Serial.print(a.acceleration.y);
  // Serial.print(",");
  // Serial.print(a.acceleration.z);
  // Serial.println("");
  if (micros() - lastMicros > INTERVAL) {
    lastMicros = micros(); // do this first or your interval is too long!
    Serial.print(a.acceleration.x);
    Serial.print(",");
    Serial.print(a.acceleration.y);
    Serial.print(",");
    Serial.print(a.acceleration.z);
    Serial.println("");
  }

  if(Serial.available() > 0 ){
        incomingString = Serial.readString();


        if(incomingString == "t"){
          end = true;
          motorRunning = false;
        }
        else{
          start = getValue(incomingString, ':', 0);
          timeStr = getValue(incomingString, ':', 1);
          cycleStr = getValue(incomingString, ':', 2);

          timeVal = timeStr.toInt() * 1000;
          cycleTotal = cycleStr.toInt();
        }

        // if(start.length() > 0 && timeVal > 0 && cycleTotal > 1 ){
        //       digitalWrite(11,HIGH);
        //       digitalWrite(12,LOW);
        // }
    }

  if(!motorRunning){
//haven't recieved data 
    if(start.length() == 0 && timeVal == 0 && cycleTotal == 1) { 
              digitalWrite(53,LOW);
              digitalWrite(52,HIGH);
      return;
    }
// Stop button pressed
    if(end == true){
      start = "";
      timeVal = 0;
      cycleTotal=1;
      cycleIndex = 0;
      end=false;
      intensityIndex = -1;
      digitalWrite(pwmPin, LOW);
      digitalWrite(53,LOW);
      digitalWrite(52,HIGH);
      return;
    }
// Cycles completed
    if(cycleIndex >= cycleTotal) { //Stop signal
      start = "";
      timeVal = 0;
      cycleTotal = 1;
      cycleIndex = 0;
      intensityIndex = -1;
      digitalWrite(pwmPin, LOW);
      digitalWrite(53,LOW);
      digitalWrite(52,HIGH);
      return;
    }

    intensityIndex++; // changes intensity 
  // Turn on motor if input given or if new cycle starts
     motorRunning = true; //continues generatePWM
    // if(intensityIndex > 1){
    //   intensityIndex = 0;
    //   cycleIndex++;
    // }
    if(intensityIndex > 5){
      intensityIndex = 0;
      cycleIndex++;
    }
  }

// run motor
  if(motorRunning){
      // digitalWrite(11,HIGH);
      // digitalWrite(12,LOW);
    generatePWM(timeVal, 5000, intensity[intensityIndex]);
    analogWrite(pwmPin, 128);
  }

  // Any errors (hasn't been called on yet)
  else {
    digitalWrite(53,LOW);
    digitalWrite(52,HIGH);
  }

}