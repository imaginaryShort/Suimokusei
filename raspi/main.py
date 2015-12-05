import requests as rq
import json

from datetime import datetime, timedelta
import math
import random

import schedule
import time
import RPi.GPIO as IO
from neopixel import *

import websocket
import thread
import time



# LED branches config:
LED_COUNT	= 5		#number of LEDs
LED_PIN		= 18		#I/O pin definition to control LEDs
LED_FREQ_HZ	= 800000	#LED signal freqency in Hz
LED_DMA		= 5		#DMA channel to use for generating signal (try 5),
				#according to an example code
LED_BRIGHTNESS	= 255		#set brightness as brightest value
LED_INVERT	= False		#True to invert the signal (if you are using NPN transistor lv shift)

# User data cache
usr_stat_cache	= {}		#{'user_id':'updated_time'}
usr_stat_list	= {}		#{'user_id':'status'}
usr_order_list	= {}		#{'user_id':'order'}

# User LED cache
usr_led_degree = {0 : 1}


class NeoPixelConfig(object):
	def __init__(self):
		self.strip = Adafruit_NeoPixel(LED_COUNT, LED_PIN, LED_FREQ_HZ, LED_DMA, LED_INVERT, LED_BRIGHTNESS)
		self.strip.begin()

	def wheel(self, pos):
		if pos < 85:
			return Color(pos*3, 255-pos*3, 0)
		elif pos < 170:
			pos -= 85
			return Color(255-pos*3, 0, pos*3)
		else:
			pos -= 170
			return Color(0, pos*3, 255-pos*3)

	def rainbow(self):
		for j in range(256):
			for i in range(self.strip.numPixels()):
				self.strip.setPixelColor(i, self.wheel((i+j) & 255))
			self.strip.show()
			time.sleep(20/1000.0)

	def set_color_by_order(self, order, status):
		c = Color(random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))
		self.strip.setPixelColor(order-1, c)
		self.strip.show()

	def turnoff_all(self):
		for x in range(LED_COUNT):
			self.strip.setPixelColor(x, Color(0,0,0))
		self.strip.show()

	def random_all(self):
		for x in range(LED_COUNT):
			self.strip.setPixelColor(x, Color(random.randint(0, 255),
							  random.randint(0, 255),
							  random.randint(0, 255)))
		self.strip.show()

	def set_color_by_status(self):
		for x in range(100):
			c = 255 * math.sin(math.pi / 100 * x)
			x = math.fabs(c) + 1
			self.strip.setPixelColor(1, Color(int(255/x) ,int(0/x), int(0/x)))
			self.strip.show()
			time.sleep(0.1)

class JobConfig(object):
	def __init__(self, crontab, job):
		self._crontab = crontab
		self.job = job

	def schedule(self):
		crontab = self._crontab
		return datetime.now() + timedelta(seconds = math.ceil(crontab.next()))

	def next(self):
		crontab = self._crontab
		return math.ceil(crontab.next())

def job_controller(jobConfig):
	while True:
		try:
			print("coming up\tschedule:%s" %jobConfig.schedule().strftime("%Y-%m-%d %H:%M:%S"))
			time.sleep(jobConfig.next())
			print("execute")
			jobConfig.job()
			print("execute done")

		except KeyboardInterrupt:
			break

	print("well done")

def stat_update():
	print('stat_update is running')
	#neoPixelConfig.rainbow()
	dat = get_homer_data(1)

	for usr in dat['users']:
		if usr['UserId'] not in usr_stat_cache or usr_stat_cache[usr['UserId']] != usr['Updated']:
		    neoPixelConfig.set_color_by_order(usr['Order'], usr['UserId'])

def led_update():
	print('led_update is running')
	while True:
		neoPixelConfig.set_color_by_status()

# Server access information:
SERVER_DOMAIN	= 'http://imaginaryshort.com:7000'
ALL_IN_DA_HOUSE = '/hid?hid='
ABOUT_A_USER	= '/add?mynumber='
UPD_A_USER_STAT = '&status='

# a function to get data of users in a house
def get_homer_data(homeID):
	query = rq.get(SERVER_DOMAIN + ALL_IN_DA_HOUSE + str(homeID))
	buf = json.loads(query.text)
	return buf

# get one element from users info array
def parse_each_user_data(buf, userID):
	for x in len(buf['users']):
		if buf['users'][x]['Id'] is userID:
			return buf['users'][x]



def on_message(ws, message):
    print message

def on_error(ws, error):
    print error

def on_close(ws):
    print "### closed ###"

def on_open(ws):
    ws.send("{\"UUID\": 13261369}")
    #ws.close()

def main():
	schedule.every(0.5).minutes.do(stat_update)
	#schedule.every(1).minutes.do(led_update)
	while True:
		try:
			schedule.run_pending()
			time.sleep(0.5)
		except KeyboardInterrupt:
			neoPixelConfig.turnoff_all()
			break


neoPixelConfig = NeoPixelConfig()
if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp("ws://localhost:5000",
                                on_message = on_message,
                                on_error = on_error,
                                on_close = on_close)
    ws.on_open = on_open
    ws.run_forever()
    main()
