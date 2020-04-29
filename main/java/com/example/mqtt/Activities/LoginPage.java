package com.example.mqtt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mqtt.Models.MqttClientModel;
import com.example.mqtt.Services.ConnectionService;
import com.example.mqtt.Models.Credentials;
import com.example.mqtt.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class LoginPage extends AppCompatActivity {
    Button LoginButton;
    String url;
    String username;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        final Context context = this.getApplicationContext();
        LoginButton = (Button) findViewById(R.id.button);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttClientModel mqttClient = MqttClientModel.getInstance();

                username = ((EditText) findViewById(R.id.username)).getText().toString();
                password = ((EditText) findViewById(R.id.password)).getText().toString();
                url = ((EditText) findViewById(R.id.url)).getText().toString();

                ConnectionService connectionService = new ConnectionService(context);
                Credentials credentials = new Credentials(
                        username,
                        password,
                        url);
                mqttClient.mqttAndroidClient = connectionService.CreateContext("tcp://" + credentials._url +":1884");
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(credentials._username);
                options.setPassword(credentials._password.toCharArray());
                try {
                    mqttClient.token = mqttClient.mqttAndroidClient.connect(options);
                    mqttClient.token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            Intent intent = new Intent(context, HomePage.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            Log.w("TAG", "onFailure");

                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
