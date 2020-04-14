package com.example.mqtt.Services;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;

public class ConnectionService {
    Context context;

    public ConnectionService(Context _context){
        context = _context;
    }

    public MqttAndroidClient CreateContext(String url){
        String clientId = MqttClient.generateClientId(); //Returns a randomly generated client identifier
        MqttAndroidClient client = new MqttAndroidClient(context, url, clientId);
        return client;
    }
}
