package com.example.javafx_vibe.javafx_vibe;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.*;
import java.util.concurrent.ScheduledExecutorService;




public class MainController implements Initializable
{
    private boolean stopFlag = false;
    private ScheduledExecutorService scheduledExecutorService;
    private XYChart.Series<Number, Number> xSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> ySeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> zSeries = new XYChart.Series<>();
    @FXML
    private MenuItem Exit;
    private static SerialPort comPort;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnChart;


    @FXML
    private Spinner<Integer> time;
    @FXML
    private Spinner<Integer> intensity;
    private int currentTimeValue;
    private int currentIntensityValue;

//    Mac version
//    protected static SerialPort macArduinoPort = SerialPort.getCommPort("/dev/tty.usbserial-1130");
//    protected static SerialPort macArduinoPort = SerialPort.getCommPort("/dev/tty.usbmodem11301");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        SerialPort[] portList = SerialPort.getCommPorts();
        System.out.print(portList[0].getPortDescription());
        String arduinoDescription = "Arduino";

        for (SerialPort port : portList) {

            System.out.println(port.getSystemPortName() + ": " + port.getDescriptivePortName());
            String portDescription = port.getPortDescription();
            if (portDescription.contains(arduinoDescription)) {
                // Try to open the port
                try{port.openPort();
                    System.out.println("Connected to Arduino on port: " + port.getSystemPortName());
                    setComPort(port);
                    // You can perform further operations with the opened port here
                } catch(Exception e) {
                    System.out.println("Failed to open port: " + port.getSystemPortName());
                }
            }
        }
        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 240);
        timeValueFactory.setValue(0);
        time.setValueFactory(timeValueFactory);
        time.valueProperty().addListener((observableValue, integer, t1) -> setCurrentTimeValue(time.getValue()));
        SpinnerValueFactory<Integer> intensityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        timeValueFactory.setValue(0);
        intensity.setValueFactory(intensityValueFactory);
        intensity.valueProperty().addListener((observableValue, integer, t1) -> setCurrentIntensityValue(intensity.getValue()));
        comPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0 );
        comPort.openPort();
//        setComPort(SerialPort.getCommPort("/dev/tty.usbmodem11301"));
//        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120);
//        timeValueFactory.setValue(0);
//        time.setValueFactory(timeValueFactory);
//        time.valueProperty().addListener((observableValue, integer, t1) -> setCurrentTimeValue(time.getValue()));
//        SpinnerValueFactory<Integer> intensityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
//        timeValueFactory.setValue(0);
//        intensity.setValueFactory(intensityValueFactory);
//        intensity.valueProperty().addListener((observableValue, integer, t1) -> setCurrentIntensityValue(intensity.getValue()));
//        comPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
//        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0 );
//        comPort.openPort();
    }


    @FXML
    void handle_btnStart(ActionEvent event) throws IOException {
        handleMotorStart();
    }

    @FXML
    void handle_btnStop(ActionEvent event) throws IOException {
        System.out.println("\nStop button clicked");
        stopFlag = true;

        OutputStream outputStream1 = comPort.getOutputStream();
        String customOutput = "t";
//        String output = "s:3:1";
        outputStream1.write(customOutput.getBytes());
        outputStream1.flush();
    }


    @FXML
    void handle_btnChart(ActionEvent event) {
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("file:///C:/Users/Owner/Code/javafx_vibe/src/main/resources/com/example/javafx_vibe/chart.fxml"));
            FXMLLoader loader = new FXMLLoader();
            URL url = new File("src/main/resources/com/example/javafx_vibe/charts.fxml").toURI().toURL();
            loader.setLocation(url);
            AnchorPane pane = loader.load();

            DataController dataController = loader.getController();
            dataController.parseData("accelerometer_data.csv");

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Data Visualization");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void handleMotorStart() throws IOException
    {
        String timeStr = Integer.toString(currentTimeValue);
        String intensityStr = Integer.toString(currentIntensityValue);
        System.out.println(timeStr);
        System.out.println(intensityStr);
        OutputStream outputStream1 = comPort.getOutputStream();
        String customOutput = "\"s:" + timeStr + ":" + intensityStr + "\"";
//        String output = "s:3:1";
        System.out.println(customOutput);
        outputStream1.write(customOutput.getBytes());
        outputStream1.flush();
    }


    @FXML
    void handle_menuCustom(ActionEvent event) {

    }

    @FXML
    void handle_menuPreset1(ActionEvent event) {

    }

    void setComPort(SerialPort comPort){
        this.comPort = comPort;
    }

    public static void closeComPort(){
        comPort.closePort();

    }

    public int getCurrentTimeValue()
    {
        return currentTimeValue;
    }

    public void setCurrentTimeValue(int currentTimeValue)
    {
        this.currentTimeValue = currentTimeValue;
    }

    public int getCurrentIntensityValue()
    {
        return currentIntensityValue;
    }

    public void setCurrentIntensityValue(int currentIntensityValue)
    {
        this.currentIntensityValue = currentIntensityValue;
    }
}




