import Fan
import Window
import HTSensor
import Motor
import paho.mqtt.client as mqtt
import time
import RPi.GPIO as GPIO          
from time import sleep

GPIO.setwarnings(False)

def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc)) 
    client.subscribe("actuator")
    client.subscribe("fan")
    
def on_message(client, userdata, msg):
    if msg.topic == "actuator":
        Motor.action(str(msg.payload.decode("utf-8")))
    elif msg.topic == "fan":
        Fan.action(str(msg.payload.decode("utf-8")))        
        
def Main():
    client = mqtt.Client()
    client.username_pw_set(username = "andrei", password = "admin")
    client.on_connect = on_connect
    client.on_message = on_message
    client.connect("192.168.43.62", 1884, 60)
    HTSensor.getTemperatureAndHumidity(client)
    Window.window(client)
    client.loop_forever()

if __name__ == '__main__':

    
    Main()