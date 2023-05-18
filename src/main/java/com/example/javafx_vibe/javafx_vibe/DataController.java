package com.example.javafx_vibe.javafx_vibe;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
public class DataController {

    @FXML
    private LineChart<?, ?> fourierChart;

    @FXML
    private LineChart<?, ?> timeChart;

    public void initialize() {
        // Initialization logic, if needed
    }

//    public void populateData() {
//        // Populate the LineCharts with data
//        populateTimeChart();
//        populateChart2();
//
//    }
//
//    private void populateTimeChart(series) {
//        // Create a NumberAxis for x and y axes
//        NumberAxis xAxis = new NumberAxis();
//        NumberAxis yAxis = new NumberAxis();
//
//        // Create the series for the chart
//        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        // Add data points to the series
//
//        // Add the series to the chart
//        timeChart.getData().add(series);
//    }
//
//    private void populateChart2() {
//        // Similar logic as populateChart1
//    }


}

