package com.example.myguijavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.fazecast.jSerialComm.*;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    public void onSendButtonClick() {
        //int i = 1;
        SerialPort comPort = SerialPort.getCommPort("/dev/cu.usbserial-1130");
        comPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING,0,0);

        comPort.openPort();
        comPort.writeBytes("h".getBytes(), "h".length());
        comPort.closePort();
    }

}