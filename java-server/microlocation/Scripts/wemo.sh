#!/bin/sh
# IOT Belkin WeeMo Control Script
#First find out the IP address of the weemo device. Then using the script type ./wemo.sh on/off to turn the switch on and off.
# Usage: wemo IP_ADDRESS ON/OFF

#add static IP here
IP=$1
RUN=`echo $2 | tr '[a-z]' '[A-Z]'`
DPORT=0

#Typical Ports Belkin Uses
for i in 49154 49152 49153 49155
do
	FINDPORT=$(curl -s -m 3 $IP:$i | grep "404")
	if [ "$FINDPORT" != "" ]
	then
		DPORT=$i
		echo $i
		break
	fi
done

if [ $DPORT = 0 ]
	then
		echo "Error: No Port was found"
		exit
fi

echo "The device has been connected to" $1":"$DPORT

if [ "$1" = "" ]; then
	echo "Usage: IP_ADDRESS[:PORT] ON/OFF"
else
	if [ "$RUN" = "ON" ]; then
		echo "Connecting to" $IP":"$DPORT "and turning device on"
		curl -0 -A '' -X POST -H 'Accept: ' -H 'Content-type: text/xml; charset="utf-8"' -H "SOAPACTION: \"urn:Belkin:service:basicevent:1#SetBinaryState\"" --data '<?xml version="1.0" encoding="utf-8"?><s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"><s:Body><u:SetBinaryState xmlns:u="urn:Belkin:service:basicevent:1"><BinaryState>1</BinaryState></u:SetBinaryState></s:Body></s:Envelope>' -s http://$IP:$DPORT/upnp/control/basicevent1 |
		grep "<BinaryState"  | cut -d">" -f2 | cut -d "<" -f1
		echo "Device has been turned on"
	elif [ "$RUN" = "OFF" ]; then
	echo "Connecting to" $IP":"$DPORT "and turning device off"
		curl -0 -A '' -X POST -H 'Accept: ' -H 'Content-type: text/xml; charset="utf-8"' -H "SOAPACTION: \"urn:Belkin:service:basicevent:1#SetBinaryState\"" --data '<?xml version="1.0" encoding="utf-8"?><s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"><s:Body><u:SetBinaryState xmlns:u="urn:Belkin:service:basicevent:1"><BinaryState>0</BinaryState></u:SetBinaryState></s:Body></s:Envelope>' -s http://$IP:$DPORT/upnp/control/basicevent1 |
		grep "<BinaryState"  | cut -d">" -f2 | cut -d "<" -f1
		echo "Device has been turned off"
	else
		echo "There has been a invalid Command"
		echo ""
		echo "Usage:IP_ADDRESS[:PORT] ON/OFF"
		echo ""
	fi
fi
