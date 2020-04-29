import RPi.GPIO as GPIO
import threading

GPIO.setmode(GPIO.BCM)
PIN = 19  
GPIO.setup(PIN, GPIO.IN, pull_up_down = GPIO.PUD_UP)

def window(client):
    if GPIO.input(PIN) == True:
        client.publish("window", payload = "false", qos = 0, retain = False)
        print("true")
    else:
        client.publish("window", payload = "true", qos = 0, retain = False)
    threading.Timer(0.5, window, [client]).start()
    