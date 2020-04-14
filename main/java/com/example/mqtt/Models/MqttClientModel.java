package com.example.mqtt.Models;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class MqttClientModel {
    public static MqttAndroidClient mqttAndroidClient;
    public static IMqttToken token;

    static MqttClientModel mqttClient = new MqttClientModel();
    private MqttClientModel(){

    }

    public static MqttClientModel getInstance(){
        return mqttClient;
    }
}
