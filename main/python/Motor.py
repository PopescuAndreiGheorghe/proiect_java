import RPi.GPIO as GPIO

in1 = 24
in2 = 23
en = 25
temp1=1
GPIO.setmode(GPIO.BCM)
GPIO.setup(in1,GPIO.OUT)
GPIO.setup(in2,GPIO.OUT)
GPIO.setup(en,GPIO.OUT)
GPIO.output(in1,GPIO.LOW)
GPIO.output(in2,GPIO.LOW)

p=GPIO.PWM(en,1000)
p.start(10)

def action(command):
    p.ChangeDutyCycle(100)
    
    if command == "forward":    
        GPIO.output(in1,GPIO.LOW)
        GPIO.output(in2,GPIO.HIGH)
    elif command == "backward":    
        GPIO.output(in1,GPIO.HIGH)
        GPIO.output(in2,GPIO.LOW)
    elif command == "stop":
        GPIO.output(in1,GPIO.LOW)
        GPIO.output(in2,GPIO.LOW)
