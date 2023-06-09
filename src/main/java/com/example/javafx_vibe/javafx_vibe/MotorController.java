package com.example.javafx_vibe.javafx_vibe;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;


public class MotorController {

    private static final int BAUD_RATE = 9600;
    private SerialPort comPort;
    private Thread serialThread;
    private boolean stopFlag = false;
    private ScheduledExecutorService scheduledExecutorService;
    private XYChart.Series<Number, Number> xSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> ySeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> zSeries = new XYChart.Series<>();
    static OutputStream outputStream;
    @FXML
    private MenuItem Exit;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private MenuItem menuCustom;

    @FXML
    private MenuItem menuPreset1;

    @FXML
    private ProgressBar progress;

    @FXML
    private Spinner<Integer> time;
    @FXML
    private Spinner<Integer> intensity;
    int currentTimeValue;
    int currentIntensityValue;
//    @Override
//    public void initialize(URL arg0, ResourceBundle arg1){
//        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120);
//        timeValueFactory.setValue(0);
//        time.setValueFactory(timeValueFactory);
//        time.valueProperty().addListener((observableValue, integer, t1) -> currentTimeValue = time.getValue());
//        SpinnerValueFactory<Integer> intensityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
//        timeValueFactory.setValue(0);
//        intensity.setValueFactory(intensityValueFactory);
//        intensity.valueProperty().addListener((observableValue, integer, t1) -> currentIntensityValue = intensity.getValue());
//
//    }






    @FXML
    private LineChart<Number, Number> accChart;

    @FXML
    void handle_Exit(ActionEvent event) {

    }

    @FXML
    void handle_btnStop(ActionEvent event) {
        System.out.println("Stop button clicked");
        stopFlag = true;
        comPort.closePort();
        System.out.print(xSeries);
    }

    @FXML
    void handle_menuCustom(ActionEvent event) {

    }

    @FXML
    void handle_menuPreset1(ActionEvent event) {

    }
//    @FXML
//    void handle_LEDStart(ActionEvent event) throws IOException
//    {
//        SerialPort comPort = ArduinoUtils.findArduinoPort();
//        setComPort(comPort);
//
//        String timeStr = Integer.toString(currentTimeValue);
//        String intensityStr = Integer.toString(currentIntensityValue);
//        System.out.println(timeStr);
//        System.out.println(intensityStr);
//        OutputStream outputStream1 = comPort.getOutputStream();
//        String customOutput = "\"s:" + timeStr + ":" + intensityStr + "\"";
////        String output = "s:3:1";
//        outputStream1.write(customOutput.getBytes());
//        outputStream1.flush();
//    }

    @FXML
    void handle_schedule(ActionEvent event) throws IOException
    {
        OutputStream outputStream2 = comPort.getOutputStream();
        if(((MenuItem)event.getSource()).getText() == "3 min 50"){
            String time = "3 x";
            outputStream2.write(time.getBytes());
            outputStream2.flush();
        }
    }

    private String findArduinoPort() {
        List<String> portNames = Arrays.asList(
                "/dev/tty.usbmodem", "/dev/tty.usbserial", // Mac OS X
                "/dev/usbdev", "/dev/ttyUSB", "/dev/ttyACM", "/dev/serial", // Linux
                "COM3", "COM4", "COM5", "COM6" // Windows
        );

        for (String portName : portNames) {
            SerialPort port = SerialPort.getCommPort(portName);
            if (port.openPort()) {
                // Wait for the Arduino to reboot
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // Ignore
                }

                // Check if the Arduino is sending data
                byte[] buffer = new byte[port.bytesAvailable()];
                int numRead = port.readBytes(buffer, buffer.length);
                if (numRead > 0) {
                    port.closePort();
                    return portName;
                } else {
                    port.closePort();
                }
            }
        }

        return null;
    }

    void setComPort(SerialPort comPort){
        this.comPort = comPort;
    }


}