package com.example.javafx_vibe.javafx_vibe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

import static com.example.javafx_vibe.javafx_vibe.Main.comPort;

public class AccelerometerController {

    private static final int BAUD_RATE = 9600;
    private SerialPort arduinoPort;
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
    private LineChart<Number, Number> accChart;
//    @FXML
//    void initialize() {
//        // Add the series to the chart
//        accChart.getData().addAll(xSeries, ySeries, zSeries);
//    }

    @FXML
    void handle_Exit(ActionEvent event) {

    }
    @FXML
    void handle_btnStart(ActionEvent event) throws IOException
    {
        SerialPort[] portList = SerialPort.getCommPorts();
        for (SerialPort port : portList) {
            System.out.println(port.getSystemPortName() + ": " + port.getDescriptivePortName());
        }
//        arduinoPort = SerialPort.getCommPort("COM3"); // Replace COM3 with your Arduino's port name
//        arduinoPort.openPort();
//        arduinoPort.setBaudRate(9600);
//        arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
//        arduinoPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

//        System.out.println(arduinoPort.getCommPort("COM3"));
        Thread dataThread = new Thread(() -> {
            try {
                InputStream inputStream = comPort.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                int character;
                while ((character = inputStreamReader.read()) != -1) {
                    // Process the line of data (e.g., split it into x, y, z values)
                    if (character == '\n') {
                        String line = bufferedReader.readLine();
                        System.out.print(line);
                        String[] values = line.split(",");

                        double x = Double.parseDouble(values[0]);
                        System.out.print(x);
                        double y = Double.parseDouble(values[1]);
                        double z = Double.parseDouble(values[2]);
                        Platform.runLater(() -> {
                            long now = System.currentTimeMillis();
                            xSeries.getData().add(new XYChart.Data<>(now, x));
                            ySeries.getData().add(new XYChart.Data<>(now, y));
                            zSeries.getData().add(new XYChart.Data<>(now, z));
                            if (xSeries.getData().size() > 10) {
                                xSeries.getData().remove(0);
                            }
                            if (ySeries.getData().size() > 10) {
                                ySeries.getData().remove(0);
                            }
                            if (zSeries.getData().size() > 10) {
                                zSeries.getData().remove(0);
                            }

                        });
                    }
                }
                inputStream.close();
                inputStreamReader.close();
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }); dataThread.start();
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

}
