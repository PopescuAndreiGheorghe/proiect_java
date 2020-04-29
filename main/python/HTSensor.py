import Adafruit_DHT
import threading
import json

DHT_SENSOR = Adafruit_DHT.DHT11
DHT_PIN = 4

def getTemperatureAndHumidity(client):
    humidity, temperature = Adafruit_DHT.read(DHT_SENSOR, DHT_PIN)
    if humidity is not None and temperature is not None:
        sensorData = {
            "temperature": "{0:0.1f}".format(temperature),
            "humidity": "{0:0.1f}".format(humidity)
            }
        client.publish("sensorData", payload = json.dumps(sensorData), qos = 0, retain = False)        
    else:
        print("Sensor failure. check wiring.")
    
    threading.Timer(3, getTemperatureAndHumidity, [client]).start()