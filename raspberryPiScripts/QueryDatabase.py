import pycurl
import json
from WeMo import wemo
import subprocess
import time
from StringIO import StringIO
import sys
from subprocess import Popen, PIPE
from time import sleep
import os
import signal

#--------------------------------------------------------------------------------
# Establishes the connection to the cloudant database and checks the database for the values for the values of the "Things"

if (len(sys.argv) != 3):
    print 'Please include username and password'
    print ' e.g python QueryDatabase.py username password'
    sys.exit()


username = sys.argv[1]
password = sys.argv[2]
cloudantURL = 'https://' + username + ':' + password +'@' + username + '.cloudant.com/microlocation/ThingsParameters'

buffer = StringIO()  
c = pycurl.Curl()
c.setopt(c.URL, cloudantURL)
c.setopt(c.WRITEDATA, buffer)
wemo1ip = '10.139.196.244'
wemo2ip = '10.139.198.253'
wemo1 = wemo(wemo1ip)
wemo2 = wemo(wemo2ip)

Wemo1ON = False
Wemo2ON = False

TVON = False

BTON = False

#----------------------------------------------------------------------
while True:
    c.perform()
    x = json.loads(buffer.getvalue()) # takes whats in the database and stores into x
    buffer.truncate(0)
    #print x['value']
    y = x['value']   
    #print y['WeMoSwitch1']
    if y['WeMoSwitch1'] == "1" and Wemo1ON == False:   #if WeMo switch 1 is 1 turn wemo switch on otherwise turn it off
        wemo1.on()
        print "wemo1:ON"
        Wemo1ON = True
    elif y['WeMoSwitch1'] == "0" and Wemo1ON == True:
        wemo1.off()
        Wemo1ON = False
        print "wemo1:OFF"
    if y['WeMoSwitch2'] == "1" and Wemo2ON == False: #if WeMo switch 2 is 1 turn wemo switch on otherwise turn it off
        wemo2.on()
        print "wemo2:ON"
        Wemo2ON = True
    elif y['WeMoSwitch2'] == "0" and Wemo2ON == True:
        wemo2.off()
        Wemo2ON = False
        print "wemo2:OFF"
#----------------------------------------------------------------------------------------------------------------
        # this section is where we are playing a video  
    if y['TV'] == "1" and TVON == False:    # if TV is 1, play the video
        subprocess.call('xset dpms force on', shell = True)
        TV = subprocess.Popen(["omxplayer","-ohdmi", "./video1.mp4"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
        TVON = True
        print "TURN TV ON"        
    elif y['TV'] == "0" and TVON == True:  # otherwise it "should" stop playing the video, the comments underneath are what we tried
        try:
            TV.stdin.write(b'q')
        except IOError:
            print "woops"
            continue
        TV.stdin.flush()
        TVON = False
        subprocess.call('xset dpms force off', shell = True)
        print "TURN TV OFF"
#----------------------------------------------------------------------------------------------------------------------
    if y['Speakers'] == "1" and BTON == False: #if the bluetooth is 1 play the music on the speakers
         BT = subprocess.Popen(["omxplayer","-olocal", "./future.mp3"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
         BTON = True
         print "Speakers ON"
    elif y['Speakers'] == "0" and BTON == True:
        try:
            BT.stdin.write(b'q')
        except IOError:
            continue
        BT.stdin.flush()
        BTON = False
        print "Speakers OFF"
        
      
   
   
     
    
    
