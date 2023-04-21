package com.example.javafx_vibe.javafx_vibe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AccelerometerReader extends Application {

    private XYChart.Series<Number, Number> series;
    private ScheduledExecutorService scheduledExecutorService;
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

    @Override
    public void start(Stage stage) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (s)");
        yAxis.setLabel("Acceleration (m/s^2)");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Accelerometer Data");

        // create series for x, y, and z axes
        XYChart.Series<Number, Number> xSeries = new XYChart.Series<>();
        xSeries.setName("X-axis");
        lineChart.getData().add(xSeries);

        XYChart.Series<Number, Number> ySeries = new XYChart.Series<>();
        ySeries.setName("Y-axis");
        lineChart.getData().add(ySeries);

        XYChart.Series<Number, Number> zSeries = new XYChart.Series<>();
        zSeries.setName("Z-axis");
        lineChart.getData().add(zSeries);

        StackPane root = new StackPane();
        root.getChildren().add(lineChart);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        // Start a separate thread to read the accelerometer data
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                Process process = Runtime.getRuntime().exec("python read_accelerometer.py");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();
                if (line != null) {
                    String[] values = line.split(",");
                    Platform.runLater(() -> {
                        series.getData().add(new XYChart.Data<>(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        scheduledExecutorService.shutdown();
    }



    @FXML
    void handle_Exit(ActionEvent event) {

    }

    @FXML
    void handle_btnStart(ActionEvent event) {

    }

    @FXML
    void handle_btnStop(ActionEvent event) {

    }

    @FXML
    void handle_menuCustom(ActionEvent event) {

    }

    @FXML
    void handle_menuPreset1(ActionEvent event) {

    }

    public static void main(String[] args) {
        launch();
    }
}
