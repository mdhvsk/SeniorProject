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
int intensityIndex = -1;
String start;
String timeStr;
String cycleStr;

boolean motorRunning = false;

void generatePWM(unsigned long onTime, unsigned long offTime, int duty ){
  unsigned long currentMillis = millis(); // Get the current time
  unsigned long elapsedMillis = currentMillis - previousMillis; // Calculate elapsed time since last update

  if (elapsedMillis <= onTime && !digitalRead(pwmPin)) {
  // if (elapsedMillis <= onTime) {

    // Set PWM signal ON with specified duty cycle
    analogWrite(pwmPin, duty * 255 / 100); // Convert duty cycle percentage to PWM value
    digitalWrite(11,HIGH);
    digitalWrite(12,LOW);
  }

  if (elapsedMillis > onTime && digitalRead(pwmPin)) {
  // if (elapsedMillis > onTime ) {

    // Set PWM signal OFF
    analogWrite(pwmPin, 0); // 0% duty cycle (fully OFF)
    digitalWrite(11,LOW);
    digitalWrite(12,HIGH);
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
  pinMode(12,OUTPUT);
  pinMode(11,OUTPUT);
  Serial.begin(9600);

}

void loop () {
  if(Serial.available() > 0 ){
        incomingString = Serial.readString();

        start = getValue(incomingString, ':', 0);
        timeStr = getValue(incomingString, ':', 1);
        cycleStr = getValue(incomingString, ':', 2);

        timeVal = timeStr.toInt() * 1000;
        cycleTotal = cycleStr.toInt();
        if(start == "s" || timeVal == 3 || cycleTotal == 1){
              // digitalWrite(11,LOW);
              // digitalWrite(12,HIGH);

        }
        
     
    }
  
  if(!motorRunning){

    if(start.length() == 0 && timeVal == 0 && cycleTotal == 1) { //haven't recieved data 
              digitalWrite(11,LOW);
              digitalWrite(12,HIGH);
      return;
    }
    if(cycleIndex >= cycleTotal) { //Stop signal
      start = "";
      timeVal = 0;
      cycleTotal = 0;
      digitalWrite(11,LOW);
      digitalWrite(12,HIGH);

      return;
    }

    motorRunning = true; //continues generatePWM
    intensityIndex++; // changes intensity 

    if(intensityIndex > 5){
      intensityIndex = 0;
      cycleIndex++;
    }
  }

  if(motorRunning){
    // digitalWrite(11,HIGH);
    // digitalWrite(12,LOW);
    generatePWM(timeVal, 1000, intensity[intensityIndex]);
  }

  else {
    digitalWrite(11,LOW);
    digitalWrite(12,HIGH);
  }

}
