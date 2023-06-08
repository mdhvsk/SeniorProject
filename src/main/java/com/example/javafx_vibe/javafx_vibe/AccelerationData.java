package com.example.javafx_vibe.javafx_vibe;


public record AccelerationData(double x, double y, double z, double time) {
    public String[] toCsvStrings() {
        return new String[] {Double.toString(x), Double.toString(y), Double.toString(z), Double.toString(time)};
    }

    public static AccelerationData from_arduino(String arduinoString, double time) throws Exception {
        var values = arduinoString.split(",");
        var x = Double.parseDouble(values[0]);
        var y = Double.parseDouble(values[1]);
        var z = Double.parseDouble(values[2]);
        return new AccelerationData(x, y, z, time);
    }

    public static AccelerationData fromCsv(String[] line){
        double x = Double.parseDouble(line[0]);
        double y = Double.parseDouble(line[1]);
        double z = Double.parseDouble(line[2]);
        double time = Double.parseDouble(line[3]);
        return new AccelerationData(x, y, z, time);
    }
}
