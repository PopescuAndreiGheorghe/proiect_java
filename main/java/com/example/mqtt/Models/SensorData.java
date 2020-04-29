package com.example.mqtt.Models;

public class SensorData {

    private String temperature;
    private String humidity;

    public SensorData(String temperature, String umidity){
        this.temperature = temperature;
        this.humidity = umidity;
    }

    public String getTemperature(){
        return temperature;
    }

    public String getHumidity(){
        return humidity;
    }
}
