import RPi.GPIO as GPIO
import time

FAN_1 = 22
FAN_2 = 27

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(FAN_1, GPIO.OUT)
GPIO.setup(FAN_2, GPIO.OUT)

def action(command):
    if command == "true":
        print("true")
        runFan()
    elif command == "false":
        print("false")
        stopFan()
        

def runFan():

    GPIO.output(FAN_1, GPIO.HIGH)
    GPIO.output(FAN_2, GPIO.LOW)

def stopFan():

    GPIO.output(FAN_1, GPIO.LOW)
    GPIO.output(FAN_2, GPIO.LOW)

