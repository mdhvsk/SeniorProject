package com.example.javafx_vibe.javafx_vibe;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoUtils {
    public static SerialPort findArduinoPort() {
        SerialPort[] portList = SerialPort.getCommPorts();
        String arduinoDescription = "Arduino";

        for (SerialPort port : portList) {

//            System.out.println(port.getSystemPortName() + ": " + port.getDescriptivePortName());
            String portDescription = port.getPortDescription();
            if (portDescription.contains(arduinoDescription)) {
                // Try to open the port
                try{port.openPort();
                    port.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
                    port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 3000, 0);
                    System.out.println("Connected to Arduino on port: " + port.getSystemPortName());
                    // You can perform further operations with the opened port here
                    return port; // Exit the loop after finding the Arduino
                } catch(Exception e) {
                    System.out.println("Failed to open port: " + port.getSystemPortName());
                }
            }
        }

        return null;
    }
}
