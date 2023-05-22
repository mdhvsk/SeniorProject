package com.example.javafx_vibe.javafx_vibe;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Path;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.lang.Math;

public class DataController {

    @FXML
    private LineChart<Number, Number> fourierChart;

    @FXML
    private LineChart<Number, Number> timeChart;

    private XYChart.Series<Number, Number> xSeriesTime = new XYChart.Series<>();
    private XYChart.Series<Number, Number> ySeriesTime = new XYChart.Series<>();
    private XYChart.Series<Number, Number> zSeriesTime = new XYChart.Series<>();
    private XYChart.Series<Number, Number> magSeriesTime = new XYChart.Series<>();
    private XYChart.Series<Number, Number> xSeriesFourier = new XYChart.Series<>();
    private XYChart.Series<Number, Number> ySeriesFourier = new XYChart.Series<>();
    private XYChart.Series<Number, Number> zSeriesFourier = new XYChart.Series<>();
    private XYChart.Series<Number, Number> magSeriesFourier = new XYChart.Series<>();

    public void initialize() {

    }

    public void parseData(String filename) throws CsvValidationException, IOException {
        var rawAccelerationData = new ArrayList<AccelerationData>();
        var filereader = new FileReader(filename);
        // Reset the reader to start from the beginning
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(filereader);
            csvReader.close();

            // Read data line by line and populate the arrays
            for (String[] line: csvReader) {
                double x = Double.parseDouble(line[0]);
                double y = Double.parseDouble(line[1]);
                double z = Double.parseDouble(line[2]);
                double time = Double.parseDouble(line[3]);
                rawAccelerationData.add(new AccelerationData(x, y, z, time));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            csvReader.close();
        }
        // Update the series
        for (AccelerationData accData:rawAccelerationData){
            xSeriesTime.getData().add(new XYChart.Data<>(accData.time(), accData.x()));
            ySeriesTime.getData().add(new XYChart.Data<>(accData.time(), accData.y()));
            zSeriesTime.getData().add(new XYChart.Data<>(accData.time(), accData.z()));
            double mag = Math.sqrt(accData.x() * accData.x() + accData.y() * accData.y() + accData.z() * accData.z());
            magSeriesTime.getData().add(new XYChart.Data<>(accData.time(), mag));
        }

        timeChart.getData().addAll(xSeriesTime, ySeriesTime, zSeriesTime, magSeriesTime);

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

