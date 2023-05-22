package com.example.javafx_vibe.javafx_vibe;

import com.opencsv.CSVReader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.*;
import java.util.concurrent.ScheduledExecutorService;




public class MainController implements Initializable
{
    private boolean stopFlag = false;
    private SerialPort comPort;
    private ScheduledExecutorService scheduledExecutorService;
    private XYChart.Series<Number, Number> xSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> ySeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> zSeries = new XYChart.Series<>();
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
    private int currentTimeValue;
    private int currentIntensityValue;

//    Mac version
//    protected static SerialPort macArduinoPort = SerialPort.getCommPort("/dev/tty.usbserial-1130");
//    protected static SerialPort macArduinoPort = SerialPort.getCommPort("/dev/tty.usbmodem11301");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        setComPort(SerialPort.getCommPort("/dev/tty.usbmodem11301"));
        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120);
        timeValueFactory.setValue(0);
        time.setValueFactory(timeValueFactory);
        time.valueProperty().addListener((observableValue, integer, t1) -> setCurrentTimeValue(time.getValue()));
        SpinnerValueFactory<Integer> intensityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        timeValueFactory.setValue(0);
        intensity.setValueFactory(intensityValueFactory);
        intensity.valueProperty().addListener((observableValue, integer, t1) -> setCurrentIntensityValue(intensity.getValue()));
        comPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0 );
        comPort.openPort();
    }

    @FXML
    private LineChart<Number, Number> accChart;

    @FXML
    void handle_Exit(ActionEvent event) {

    }
    @FXML
    void handle_btnStart(ActionEvent event) throws IOException {
//        SerialPort comPort = ArduinoUtils.findArduinoPort();
//        setComPort(comPort);


        handleMotorStart(comPort);
//
//        String filePath = "accelerometer_data.csv";
//        CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));
//
//        long startTime = System.currentTimeMillis();
//
//        Thread dataThread = new Thread(() -> {
//            try {
//                InputStream inputStream = comPort.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                int character;
//                while ((character = inputStreamReader.read()) != -1 && !stopFlag) {
//                    // Process the line of data (e.g., split it into x, y, z values)
//                    if (character == '\n') {
//                        String line = bufferedReader.readLine();
//                        System.out.print(line);
//                        long currentTime = System.currentTimeMillis();
//                        long elapsedTime = (currentTime - startTime) / 1000;
//                        String[] values = line.split(",");
//                        // Create a new array with an additional element for the timestamp
//                        String[] valuesWithTime = new String[values.length + 1];
//
//                        // Copy the original values to the new array
//                        System.arraycopy(values, 0, valuesWithTime, 0, values.length);
//
//                        // Append the formatted timestamp to the new array
//                        valuesWithTime[values.length] = String.valueOf(elapsedTime);
//                        csvWriter.writeNext(valuesWithTime);
//                    }
//                }
//                inputStream.close();
//                inputStreamReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    csvWriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        dataThread.start();
    }

    @FXML
    void handle_btnStop(ActionEvent event) {
        System.out.println("\nStop button clicked");
        stopFlag = true;
        comPort.closePort();
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("accelerometer_data.csv");

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextData;

            // we are going to read data line by line
            while ((nextData = csvReader.readNext()) != null) {
                System.out.print(nextData[0]);
                double x = Double.parseDouble(nextData[0]);
                double y = Double.parseDouble(nextData[1]);
                double z = Double.parseDouble(nextData[2]);
                double time = Double.parseDouble(nextData[3]);

                Platform.runLater(() -> {
                    xSeries.getData().add(new XYChart.Data<>(time, x));
                    ySeries.getData().add(new XYChart.Data<>(time, y));
                    zSeries.getData().add(new XYChart.Data<>(time, z));
                });

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!xSeries.getData().isEmpty()) {
            // xSeries has data points
            System.out.println("xSeries has data points\n");
            System.out.println(xSeries.getData());
        } else {
            // xSeries is empty
            System.out.println("xSeries is empty");
        }
    }


    void handleMotorStart(SerialPort arduinoPort) throws IOException
    {

        String timeStr = Integer.toString(currentTimeValue);
        String intensityStr = Integer.toString(currentIntensityValue);
        System.out.println(timeStr);
        System.out.println(intensityStr);
        OutputStream outputStream1 = arduinoPort.getOutputStream();
        String customOutput = "\"s:" + timeStr + ":" + intensityStr + "\"";
//        String output = "s:3:1";
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




