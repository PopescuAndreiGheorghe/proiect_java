package com.example.mqtt.Models;

public class Credentials {
    public String _username;
    public String _password;
    public String _url;

    public Credentials(String username, String password, String url){
        _username = username;
        _password = password;
        _url = url;
    }
}
