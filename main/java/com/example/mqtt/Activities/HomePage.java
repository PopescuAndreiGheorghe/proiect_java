package com.example.mqtt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mqtt.Models.MqttClientModel;
import com.example.mqtt.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HomePage extends AppCompatActivity {


    TextView temperature;
    TextView humidity;
    Button motorForward;
    Button motorBackward;
    Button motorStop;
    Switch windowOpened;
    final MqttClientModel mqttClientModel = MqttClientModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initState();
        temperature = (TextView) findViewById(R.id.temperature);
        windowOpened = (Switch) findViewById(R.id.windowOpened);
        motorForward = (Button) findViewById(R.id.motorUp);
        motorBackward = (Button) findViewById(R.id.motorDown);
        motorStop = (Button) findViewById(R.id.motorStop);

        final Context context = this.getApplicationContext();

        setSubscription(mqttClientModel.mqttAndroidClient);

        mqttClientModel.mqttAndroidClient.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                Intent intent = new Intent(context, LoginPage.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if(topic.equals("temperature")){
                    temperature.setText("Temperatura: " + new String(message.getPayload())+"°C");

                }
                else if(topic.equals("window")){
                    windowOpened.setChecked(Boolean.valueOf(new String(message.getPayload())));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        motorForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("UP", "actuator");
            }
        });

        motorBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("DOWN", "actuator");
            }
        });

        motorStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("STOP", "actuator");
            }
        });
    }

    private void initState(){
        sendMessage("", "initialize");
    }

    private void sendMessage(String message, String topic){
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = message.getBytes("UTF-8");
            MqttMessage mqttMessage = new MqttMessage(encodedPayload);
            mqttClientModel.mqttAndroidClient.publish(topic, mqttMessage);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setSubscription(MqttAndroidClient client){
        try{
            client.subscribe("temperature", 0);
            client.subscribe("window", 0);
        }
        catch(MqttException e){
            e.printStackTrace();
        }
    }
}