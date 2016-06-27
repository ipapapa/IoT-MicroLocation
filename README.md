# IoT-MicroLocation

[![Build Status](https://travis-ci.org/ipapapa/IoT-MicroLocation.svg)](https://travis-ci.org/ipapapa/IoT-MicroLocation)


An end-to-end Internet of Things (IoT) software package for Micro-Location, Proximity Aware services and Geofencing using Apple's iBeacons and set of Cloud services. The project is a collaboration effort of the teams from Purdue University and North Carolina State University advised by Dr. Ioannis Papapanagiotou - ipapapa@ncsu.edu.

# Technical Summary

![IoT-MicroLocation Flow](https://github.com/idarwish1/images/blob/master/projectflowchart.png)

The Microlocation system utilizes Bluetooth Low Energy (BLE) technology of iBeacons to locate a user. Multiple beacons are placed in a room and based on trilateration algorithms the user's mobile device can be located. The iBeacon packets broadcasted have the essential data of each beacon such as UUID, Proximity, RSSI, Major and Minor numbers embedded into the payload of each packet. The iPhone receives this data and through HTTP POSTs the mobile device puts this data into a JSON object and sends it to a server whether it be local (Apache Tomcat) or in the cloud (Bluemix). The server is where the power house is where it processes the data coming through and based on RSSI values from multiple beacons coming in it will trilaterate the user's position and then run a particle filtering algorithm to make it more accurate. Once the user is located the server will reply back in the form of a JSON Response Object and the iPhone will do a iBeacon packet inspection to find the x and y coordinates that the server has calculated for the user's location. The iPhone plots this onto the map of the room and the user therefore will know where they are located. Now knowing the location of the user, the server simultaneously sends a signal to a MySQL database indicating that the user is nearby a device. The Raspberry Pi polls the MySQL Database for the things that it knows about constantly until something changes in the database. Once a change has been made (i.e. a user is near or far from a device) the device state changes and turns on or off.

# Readme

Below you may find information about the Java server application, Bluemix installation, the iOS application and the Raspberry Pi.

The [Wiki](https://github.com/ipapapa/IoT-MicroLocation/wiki) contains further information about additional components that can be added to the project.

## Examples and Demos

### Research Papers

Our research paper of how iBeacons can be used for MicroLocation: F. Zafari, I. Papapanagiotou, ["Enhancing iBeacon based Micro-Location with Particle Filtering"](http://people.engr.ncsu.edu/ipapapa/Files/globecom2015.pdf), IEEE GLOBECOM 2015, San Diego CA.


### Videos
[Senior Design Group ECE NC State University - Internet of Things with iBeacons](https://www.youtube.com/watch?v=rZIDv4PnV2U)

[Proximity based interaction with iBeacons at NCSU Hunt Library](http://www.youtube.com/watch?v=AwckTkpN4-Y)
 
[Microlocation and Geofencing - Purdue University](http://www.youtube.com/watch?v=QCtc7z6PC70)

[1mx1m Accuracy - North Carolina State University](https://www.youtube.com/watch?v=uQul5LDdpLc)

[Design Day Demo and Project Overview - North Carolina State University](https://www.youtube.com/watch?v=3bmHBxyk1qc)


<img src="images/Architecture.png" alt="alt text" width="" height="250">
