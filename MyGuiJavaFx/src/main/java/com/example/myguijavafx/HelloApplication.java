package com.example.myguijavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import java.io.IOException;

public class HelloApplication extends Application
{

    private SerialPort serialPort;

    @Override
    public void start(Stage stage) throws Exception
    {
        VBox root = new VBox();
        Button button = new Button("Send data to Arduino");
        button.setOnAction(event -> {
            try {
                serialPort.writeString("Hello, Arduino!");
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        });
        root.getChildren().add(button);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        serialPort = new SerialPort("/dev/ttyACM0"); // Replace with the correct port name for your system
        serialPort.openPort();
        serialPort.setParams(9600, 8, 1, 0);
        serialPort.addEventListener(new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.isRXCHAR()) {
                    try {
                        String data = serialPort.readString();
                        // Process the received data from Arduino
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void stop() throws Exception {
        // Close the serial port when the JavaFX application stops
        serialPort.closePort();
        super.stop();
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}