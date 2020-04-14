import paho.mqtt.client as mqtt
import random
import time
import threading
import os
import glob
import RPi.GPIO as GPIO          
from time import sleep

in1 = 24
in2 = 23
en = 25
temp1=1

base_dir = '/sys/bus/w1/devices/'
device_folder = glob.glob(base_dir + '28*')[0]
device_file = device_folder + '/w1_slave'

GPIO.setmode(GPIO.BCM)
GPIO.setup(in1,GPIO.OUT)
GPIO.setup(in2,GPIO.OUT)
GPIO.setup(en,GPIO.OUT)
GPIO.output(in1,GPIO.LOW)
GPIO.output(in2,GPIO.LOW)
PIN = 18  
GPIO.setup(PIN, GPIO.IN, pull_up_down = GPIO.PUD_UP)
p=GPIO.PWM(en,1000)
p.start(10)

def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc)) 
    client.subscribe("actuator")

def on_message(client, userdata, msg):
    
    if msg.topic == "actuator":
        if(str(msg.payload) == "UP"):
            GPIO.output(in1,GPIO.LOW)
            GPIO.output(in2,GPIO.HIGH)
        elif(str(msg.payload) == "DOWN"):
            GPIO.output(in1,GPIO.HIGH)
            GPIO.output(in2,GPIO.LOW)
        elif(str(msg.payload) == "STOP"):
            GPIO.output(in1,GPIO.LOW)
            GPIO.output(in2,GPIO.LOW)

def temperature():
    read_temp()
    client.publish("temperature", payload = read_temp(), qos = 0, retain = False)
    threading.Timer(3, temperature).start()
    
        
def window():
    if GPIO.input(PIN) == True:
        client.publish("window", payload = "false", qos = 0, retain = False)
    else:
        client.publish("window", payload = "true", qos = 0, retain = False)
    threading.Timer(0.5, window).start()
    
os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')
 

 
def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines
 
def read_temp():
    lines = read_temp_raw()
    while lines[0].strip()[-3:] != 'YES':
        time.sleep(0.2)
        lines = read_temp_raw()
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        temp_c = float(temp_string) / 1000.0
        temp_f = temp_c * 9.0 / 5.0 + 32.0
        return temp_c;

client = mqtt.Client()
client.username_pw_set(username = "andrei", password = "admin")
client.on_connect = on_connect
client.on_message = on_message
client.connect("192.168.43.62", 1884, 60)
temperature()
window()
client.loop_forever()
