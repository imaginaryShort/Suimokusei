import requests as rq
import json

from crontab import CronTab
from datetime import datetime, timedelta
import math
from multiprocessing import Pool
import random

import schedule
import time
import RPi.GPIO as IO
from neopixel import *

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
		
		#if status is 'still':
		#	test
		#elif status is 'walk':
		#	test
		#elif status is 'run':
		#	test
		#elif status is 'bicycle':
		#	test
		#elif status is 'sleep':
		#	test
		#elif status is 'meal':
		#	test
		#elif status is 'refrigerator':
		#	test

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
			#usr_stat_cache[usr['UserId']] = usr['Updated']
			#usr_stat_list[usr['UserId']] = usr['Status']
			##usr_order_list[usr['UserId']] = 
			#
			#usr_led_degree[usr['UserId']] = 1
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

# only debugging use function
#def get_all_user_data():
#	query = rq.get(SERVER_DOMAIN)
#	buf = json.loads(query.text)
#	return buf

# a function to get data of users in a house
def get_homer_data(homeID):
	query = rq.get(SERVER_DOMAIN + ALL_IN_DA_HOUSE + str(homeID))
	buf = json.loads(query.text)
	return buf

# a function to update user status
#def upd_user_status_data(userID, status):
#	query = rq.get(SERVER_DOMAIN + ABOUT_A_USER + str(userID)
#			+ UPD_A_USER_STAT + status)
#	if query.text is 'OK':
#		return True
#	else:
#		return False

# get one element from users info array
def parse_each_user_data(buf, userID):
	for x in len(buf['users']):
		if buf['users'][x]['Id'] is userID:
			return buf['users'][x]

def main():
	#jobConfigs = [
	#	JobConfig(CronTab("* * * * *"), stat_update),
	#	JobConfig(CronTab("*/2 * * * *"), led_update)
	#]
	#p = Pool(len(jobConfigs))
	#try:
	#	p.map(job_controller, jobConfigs)
	#except KeyboardInterrupt:
	#	neoPixelConfig.turnoff_all()
	#
	#stat_update()
	#led_update()


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
	main()
