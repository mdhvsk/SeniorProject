package com.example.javafx_vibe.javafx_vibe;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import com.example.javafx_vibe.javafx_vibe.ArduinoUtils;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:///C:/Users/Owner/Code/javafx_vibe/src/main/resources/com/example/javafx_vibe/GUI.fxml"));
        //loader.setLocation(new URL("file:///C:/Users/jonat/Desktop/seniorProject/src/main/resources/com/example/javafx_vibe/GUI.fxml"));

        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}

