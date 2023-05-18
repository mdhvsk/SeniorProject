package com.example.javafx_vibe.javafx_vibe;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
//    protected static SerialPort comPort = SerialPort.getCommPort("COM3");
    protected static SerialPort comPort = SerialPort.getCommPort("/dev/tty.usbserial-1130");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

//        loader.setLocation(new URL("file:///C:/Users/Owner/Code/javafx_vibe/src/main/resources/com/example/javafx_vibe/GUI.fxml"));
//        loader.setLocation(new URL("file:///C:/Users/jonat/Desktop/seniorProject/src/main/resources/com/example/javafx_vibe/GUI.fxml"));

        URL url = new File("src/main/resources/com/example/javafx_vibe/GUI.fxml").toURI().toURL();
        System.out.println(url);
//        Parent root = FXMLLoader.load(url);
        loader.setLocation(url);
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
//        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
        comPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0 );
        comPort.openPort();
    }
    }

