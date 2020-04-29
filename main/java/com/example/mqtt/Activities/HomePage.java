package com.example.mqtt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mqtt.Models.MqttClientModel;
import com.example.mqtt.Models.SensorData;
import com.example.mqtt.R;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.io.UnsupportedEncodingException;

public class HomePage extends AppCompatActivity {

    TextView temperature;
    TextView humidity;
    Button motorForward;
    Button motorBackward;
    Switch windowState;
    Switch fanSwitch;
    final MqttClientModel mqttClientModel = MqttClientModel.getInstance();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initState();
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        windowState = (Switch) findViewById(R.id.windowState);
        motorForward = (Button) findViewById(R.id.motorUp);
        motorBackward = (Button) findViewById(R.id.motorDown);
        fanSwitch = (Switch) findViewById(R.id.fanState);

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
                if(topic.equals("sensorData")){
                    Gson g = new Gson();
                    String decodedMessage = new String(message.getPayload());
                    SensorData sensorData = g.fromJson(decodedMessage, SensorData.class);
                    temperature.setText("Temperatura: " + sensorData.getTemperature()+"°C");
                    humidity.setText("Umiditate: " + sensorData.getHumidity()+"%");
                }
                else if(topic.equals("window")){
                    windowState.setChecked(Boolean.valueOf(new String(message.getPayload())));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

//        motorForward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendMessage("UP", "actuator");
//            }
//        });

        motorForward.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendMessage("forward", "actuator");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    sendMessage("stop", "actuator");
                }
                return true;
            }
        });

        motorBackward.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendMessage("backward", "actuator");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    sendMessage("stop", "actuator");
                }
                return true;
            }
        });

        fanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendMessage(String.valueOf(isChecked), "fan");
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
            client.subscribe("sensorData", 0);
            client.subscribe("window", 0);
        }
        catch(MqttException e){
            e.printStackTrace();
        }
    }
}