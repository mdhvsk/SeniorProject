package com.example.javafx_vibe.javafx_vibe;

import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AccelerometerReader extends Application {

    private static final int BAUD_RATE = 9600;
    private SerialPort arduinoPort;
    private Thread serialThread;
    private boolean stopFlag = false;
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
    private LineChart<Number, Number> accChart;
//    @FXML
//    void initialize() {
//        // Add the series to the chart
//        accChart.getData().addAll(xSeries, ySeries, zSeries);
//    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:///C:/Users/Owner/Code/javafx_vibe/src/main/resources/com/example/javafx_vibe/GUI.fxml"));
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        }
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Time (s)");
//        yAxis.setLabel("Acceleration (m/s^2)");
//        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        lineChart.setTitle("Accelerometer Data");
//
//        // create series for x, y, and z axes
//        xSeries.setName("X-axis");
//        lineChart.getData().add(xSeries);
//
//        ySeries.setName("Y-axis");
//        lineChart.getData().add(ySeries);
//
//        zSeries.setName("Z-axis");
//        lineChart.getData().add(zSeries);
//
//        StackPane root = new StackPane();
//        root.getChildren().add(lineChart);
//
//        Scene scene = new Scene(root, 800, 600);
//        stage.setScene(scene);
//        stage.show();
//
//
//    }

    @Override
    public void stop() {
        scheduledExecutorService.shutdown();
    }



    @FXML
    void handle_Exit(ActionEvent event) {

    }
    @FXML
    void handle_btnStart(ActionEvent event) {
//        System.out.println("Start button clicked");
//        final String serialPortName = findArduinoPort();
//        SerialPort [] AvailablePorts = SerialPort.getCommPorts();
//
//        // use the for loop to print the available serial ports
//        for(SerialPort S : AvailablePorts)
//            System.out.println("\n  " + S.toString());
//
//        try {
//            serialPort = SerialPort.getCommPort("COM6");
//            int BaudRate = 9600;
//            int DataBits = 8;
//            int StopBits = SerialPort.ONE_STOP_BIT;
//            int Parity   = SerialPort.NO_PARITY;
//
////Sets all serial port parameters at one time
//            serialPort.setComPortParameters(BaudRate,
//                    DataBits,
//                    StopBits,
//                    Parity);
//
////Set Read Time outs
//            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,
//                    1000,
//                    0);
//            serialPort.openPort();
//        } catch (Exception e){
//            System.out.print("Arduino port not found, check connection");
//        }
//
//        System.out.print(serialPort);
//
//
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                System.out.println("in call");
//                while (!isCancelled()) {
//                    System.out.println("in while l");
//                    // Read a line of data from the serial port
//                    byte[] buffer = new byte[serialPort.bytesAvailable()];
//                    int numRead = serialPort.readBytes(buffer, buffer.length);
//                    System.out.println(buffer);
//                    String data = new String(buffer, 0, numRead);
//
//
//                    if (data.isEmpty()) {
//                        continue;
//                    }
//                    System.out.println("hi");
//                    System.out.println(data);
//                    // Parse the data and extract the accelerometer values
//                    String[] values = data.split(",");
//                    if (values.length != 3) {
//                        continue;
//                    }
//                    double x = Double.parseDouble(values[0]);
//                    System.out.print(x);
//                    double y = Double.parseDouble(values[1]);
//                    double z = Double.parseDouble(values[2]);
//
//                    // Add the new accelerometer values to the series and update the chart
//                    Platform.runLater(() -> {
//                            System.out.println("In run");
//                            long now = System.currentTimeMillis();
//                            xSeries.getData().add(new XYChart.Data<Number, Number>(now, x));
//                            ySeries.getData().add(new XYChart.Data<Number, Number>(now, y));
//                            zSeries.getData().add(new XYChart.Data<Number, Number>(now, z));
//                    });
//                }
//                return null;
//            }
//        };
//        new Thread(task).start();
        SerialPort[] portList = SerialPort.getCommPorts();
        for (SerialPort port : portList) {
            System.out.println(port.getSystemPortName() + ": " + port.getDescriptivePortName());
        }
        arduinoPort = SerialPort.getCommPort("COM6"); // Replace COM3 with your Arduino's port name
        arduinoPort.openPort();
        arduinoPort.setBaudRate(9600);
        arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
        arduinoPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        Thread dataThread = new Thread(() -> {
            Scanner scanner = new Scanner(arduinoPort.getInputStream());
            while (scanner.hasNextLine() && !stopFlag) {
                String line = scanner.nextLine();
                // Process the line of data (e.g., split it into x, y, z values)
                String[] values = line.split(",");

                double x = Double.parseDouble(values[0]);
                System.out.print(x);
                double y = Double.parseDouble(values[1]);
                double z = Double.parseDouble(values[2]);

                long now = System.currentTimeMillis();
                xSeries.getData().add(new XYChart.Data<Number, Number>(now, x));
                ySeries.getData().add(new XYChart.Data<Number, Number>(now, y));
                zSeries.getData().add(new XYChart.Data<Number, Number>(now, z));

            }
            scanner.close();

        });
        dataThread.start();



        // Start a separate thread to read the accelerometer data
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            try {
//                Process process = Runtime.getRuntime().exec("python read_accelerometer.py");
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line = reader.readLine();
//                if (line != null) {
//                    String[] values = line.split(",");
//                    Platform.runLater(() -> {
//                        xSeries.getData().add(new XYChart.Data<>(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
//                        ySeries.getData().add(new XYChart.Data<>(Double.parseDouble(values[0]), Double.parseDouble(values[2])));
//                        zSeries.getData().add(new XYChart.Data<>(Double.parseDouble(values[0]), Double.parseDouble(values[3])));
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, 0, 100, TimeUnit.MILLISECONDS);

    }

    @FXML
    void handle_btnStop(ActionEvent event) {
        System.out.println("Stop button clicked");
        stopFlag = true;
//        try {
//            serialThread.join();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
        arduinoPort.closePort();
        System.out.print(xSeries);
    }

    @FXML
    void handle_menuCustom(ActionEvent event) {

    }

    @FXML
    void handle_menuPreset1(ActionEvent event) {

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

    public static void main(String[] args) {
        launch(args);
    }
}
