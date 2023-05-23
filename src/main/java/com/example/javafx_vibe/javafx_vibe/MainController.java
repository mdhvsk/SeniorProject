package com.example.javafx_vibe.javafx_vibe;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.concurrent.ScheduledExecutorService;




public class MainController {
    private boolean stopFlag = false;
    private DataController dataController;
    private SerialPort comPort;
    private ScheduledExecutorService scheduledExecutorService;

    @FXML
    private MenuItem Exit;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnChart;

    @FXML
    private MenuItem menuCustom;

    @FXML
    private MenuItem menuPreset1;

    @FXML
    private ProgressBar progress;


//    @FXML
//    void initialize() {
//        // Add the series to the chart
//        accChart.getData().addAll(xSeries, ySeries, zSeries);
//    }

    @FXML
    void handle_Exit(ActionEvent event) {

    }

//
//    @FXML
//    void handle_btnStop(ActionEvent event) {
//        System.out.println("Stop button clicked");
//        stopFlag = true;
//        comPort.closePort();
//        System.out.print(xSeries);
//    }
    @FXML
    void handle_btnStart(ActionEvent event) throws IOException {
        SerialPort comPort = ArduinoUtils.findArduinoPort();
        setComPort(comPort);

        String filePath = "accelerometer_data.csv";
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));

        long startTime = System.currentTimeMillis();

        Thread dataThread = new Thread(() -> {
            try {
                InputStream inputStream = comPort.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while (!stopFlag) {
                    // Process the line of data (e.g., split it into x, y, z values)
                    var line = bufferedReader.readLine();
                    System.out.println(line);
                    long currentTime = System.currentTimeMillis();
                    double elapsedTime = (double) (currentTime - startTime) / 1000;
                    try {
                        var accelerationData = AccelerationData.from_arduino(line, elapsedTime);
                        csvWriter.writeNext(accelerationData.toCsvStrings());
                    } catch (Exception e) {
                        System.out.println("Failed to parse arduino data: " + e);
                    }
                }
                inputStream.close();
                inputStreamReader.close();
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dataThread.start();
    }

    @FXML
    void handle_btnStop(ActionEvent event) throws CsvValidationException, IOException {
        System.out.println("\nStop button clicked");
        stopFlag = true;
        comPort.closePort();
//        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
//            FileReader filereader = new FileReader("accelerometer_data.csv");
//
//            // create csvReader object passing
//            // file reader as a parameter
//            CSVReader csvReader = new CSVReader(filereader);
//            String[] nextData;
//
//            // we are going to read data line by line
//            while ((nextData = csvReader.readNext()) != null) {
//                System.out.print(nextData[0]);
//                double x = Double.parseDouble(nextData[0]);
//                double y = Double.parseDouble(nextData[1]);
//                double z = Double.parseDouble(nextData[2]);
//                double time = Double.parseDouble(nextData[3]);
//
//                Platform.runLater(() -> {
//                    xSeries.getData().add(new XYChart.Data<>(time, x));
//                    ySeries.getData().add(new XYChart.Data<>(time, y));
//                    zSeries.getData().add(new XYChart.Data<>(time, z));
//                });
//
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (!xSeries.getData().isEmpty()) {
//            // xSeries has data points
//            System.out.println("xSeries has data points\n");
//            System.out.println(xSeries.getData());
//        } else {
//            // xSeries is empty
//            System.out.println("xSeries is empty");
//        }
    }


    @FXML
    void handle_btnChart(ActionEvent event) {
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("file:///C:/Users/Owner/Code/javafx_vibe/src/main/resources/com/example/javafx_vibe/chart.fxml"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new URL("file:///C:/Users/Owner/Code/javafx_vibe/src/main/resources/com/example/javafx_vibe/charts.fxml"));
            AnchorPane pane = loader.load();

            DataController dataController = loader.getController();
            dataController.parseData("C:\\Users\\Owner\\Code\\javafx_vibe\\accelerometer_data.csv");

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
    void handle_menuCustom(ActionEvent event) {

    }

    @FXML
    void handle_menuPreset1(ActionEvent event) {

    }
    @FXML
    void handle_LEDStart(ActionEvent event) throws IOException
    {

        OutputStream outputStream1 = comPort.getOutputStream();
        outputStream1.write('s');
        outputStream1.flush();
    }

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
    void setComPort(SerialPort comPort){
        this.comPort = comPort;
    }
}




