import json
import websocket
import random
from neopixel import *

def on_message(ws, message):
    data = json.loads(message)["results"][0]
    suzuran.set_color_by_order(int(data["Order"]))

def on_error(ws, error):
    print error

def on_close(ws):
    print "### closed ###"

def on_open(ws):
    ws.send("{\"UUID\": 1}")
    #ws.close()

# LED branches config:
LED_COUNT = 5
LED_PIN = 18
LED_FREQ_HZ = 800000
LED_DMA = 5
LED_BRIGHTNESS = 255
LED_INVERT = False

class NeoPixelConfig(object):
    def __init__(self):
        self.strip = Adafruit_NeoPixel(LED_COUNT, LED_PIN, LED_FREQ_HZ, LED_DMA, LED_INVERT, LED_BRIGHTNESS)
        self.strip.begin()

    def set_color_by_order(self, order):
        c = Color(random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))
        self.strip.setPixelColor(order-1, c)
        self.strip.show()

    def random_all(self):
        for x in range(LED_COUNT):
            self.strip.setPixelColor(x, Color(  random.randint(0, 255),
                                                random.randint(0, 255),
                                                random.randint(0, 255)))
            self.strip.show()


suzuran = NeoPixelConfig();
if __name__ == "__main__":
    f = open("env.json")
    env = json.load(f)
    f.close()

    #websocket.enableTrace(True)
    ws = websocket.WebSocketApp(env["server"],
            on_message = on_message,
            on_error = on_error,
            on_close = on_close)
    ws.on_open = on_open
    ws.run_forever()
