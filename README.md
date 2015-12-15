
# IoT-MicroLocation
An end-to-end Internet of Things (IoT) software package for Micro-Location, Proximity Aware services and Geofencing using Apple's iBeacons and set of Cloud services. The project is a collaboration effort of the teams from Purdue University and North Carolina State University advised by Dr. Ioannis Papapanagiotou - ipapapa@ncsu.edu.

# Readme

Below you may find information about the Java server application, Bluemix installation, the iOS application and the Raspberry Pi.

The [Wiki](https://github.com/ipapapa/IoT-MicroLocation/wiki) contains further information about additional components that can be added to the project.

## Examples and Demos
[Proximity based interaction with iBeacons at NCSU Hunt Library](http://www.youtube.com/watch?v=AwckTkpN4-Y)
 
[Microlocation and Geofencing - Purdue University](http://www.youtube.com/watch?v=QCtc7z6PC70)

Our research paper of how iBeacons can be used for MicroLocation: F. Zafari, I. Papapanagiotou, ["Enhancing iBeacon based Micro-Location with Particle Filtering"](http://people.engr.ncsu.edu/ipapapa/Files/globecom2015.pdf), IEEE GLOBECOM 2015, San Diego CA.

[1mx1m Accuracy - North Carolina State University](https://www.youtube.com/watch?v=uQul5LDdpLc)

[Design Day Demo and Project Overview - North Carolina State University](https://www.youtube.com/watch?v=3bmHBxyk1qc)

[Senior Design Group ECE NC State University - Internet of Things with iBeacons]((https://www.youtube.com/watch?v=rZIDv4PnV2U)

<img src="https://github.com/idarwish1/images/blob/master/Architecture.png" alt="alt text" width="" height="250">


# Technical Summary

![IoT-MicroLocation Flow](https://github.com/idarwish1/images/blob/master/projectflowchart.png)

The Microlocation system utilizes Bluetooth Low Energy (BLE) technology of iBeacons to locate a user. Multiple beacons are placed in a room and based on trilateration algorithms the user's mobile device can be located. The iBeacon packets broadcasted have the essential data of each beacon such as UUID, Proximity, RSSI, Major and Minor numbers embedded into the payload of each packet. The iPhone receives this data and through HTTP POSTs the mobile device puts this data into a JSON object and sends it to a server whether it be local (Apache Tomcat) or in the cloud (Bluemix). The server is where the power house is where it processes the data coming through and based on RSSI values from multiple beacons coming in it will trilaterate the user's position and then run a particle filtering algorithm to make it more accurate. Once the user is located the server will reply back in the form of a JSON Response Object and the iPhone will do a iBeacon packet inspection to find the x and y coordinates that the server has calculated for the user's location. The iPhone plots this onto the map of the room and the user therefore will know where they are located. Now knowing the location of the user, the server simultaneously sends a signal to the cloudant database indicating that the user is nearby a device. The Raspberry Pi polls the Cloudant Database for the things that it knows about constantly until something changes in the database. Once a change has been made (i.e. a user is near or far from a device) the device state changes and turns on or off.


## Java application 

### Getting Started (WemoController3 & interactiveRoom) 

After successfully importing the Java project and running it on a local TomCat server or in the cloud on a BlueMix server, there are only a few parts of of the Java application that will change based on your environment. These will basically be how you always initialize your server to a new environment. 

#### WemoController3:

Open in the project in an IDE or open WemoController3.java in a text editor and scroll to line 105. Here we change the points of our landmarks (“beacons”). Insert the meter locations of the beacons within the room. On line 116, also change the array size of array variable ‘re’ to the amount of beacons the user has within the room. In lines 374, 378, 382, 386 also change the minor values of the beacons you are working with.

For example: (B1 is a beacon minor value of 1)



<img src="https://github.com/idarwish1/images/blob/master/beaconplacement.png" alt="alt text" width="" height="400">



```java
    final Point[] landmarks = new Point[]{new Point(0.00d, 0.00d), new Point(3.00d,0.00d), 
    new Point(0.00d,3.00d), new Point(3.00d,3.00d)};
```




Note: Feel free to use the provided FeetToMeter function within the Java functions if necessary


```java
    private iBeacon[] re = new iBeacon[4];
```




For lines 374, 378, 382, 386 change the location of where the rssi value is stored within the iBeacon array to index of the landmarks array.

So since B1 is at location ‘0’ within “landmarks” array, makes sure the rssi value of B1 is at location ‘0’ within “re” array.

#### interactiveRoom:

Starting at line 93, change the “landmark” locations as shown in the WemoController3 instructions.

Since we are interacting with Things based on proximity rather than the microlocation algorithm, the minor values of the iBeacons on lines 401 and 407 need to be changed. We created an array for each “Thing” we wanted to interact with. Within this array are RSSI values that come from the iBeacon that is near a “Thing”. Depending on the array size from lines 101 and 102, we took that many samples from the beacon and placed them into the array. Next we took the average – if it is within the threshold value we would turn the “Thing” on, otherwise we would turn the “Thing” off. Please see lines 433-507. 

To interact with CloudantDB, there are two changes that have to be made. In interactiveRoom.java you must change line 89 to your current revision number within your database. Secondly, in PostToDatabase.java, edit lines 40 and 46 to your username and password.

### IBM Softlayer

![Bluemix](https://www-304.ibm.com/connections/blogs/NordicMSP/resource/BLOGS_UPLOADED_IMAGES/IBM-BlueMix.jpg)



Bluemix™ is the latest cloud offering from IBM®. It enables organizations and developers to quickly and easily create, deploy, and manage applications on the cloud. Bluemix is an implementation of IBM's Open Cloud Architecture based on Cloud Foundry, an open source Platform as a Service (PaaS). Bluemix delivers enterprise-level services that can easily integrate with your cloud applications without you needing to know how to install or configure them. This article gives a high-level description of Cloud Foundry and Bluemix and outlines the features and services that were part of the open beta of Bluemix, which make it a compelling PaaS in the market today.

Instructions:

1.) Make a IBM Bluemix account

2.) Create on a Space

3.) Name your project

4.) Create App

5.) Specify whether you want to create a web or mobile application

6.) Select the library, Liberty for Java

7.) Choose a name for your app

After you name your app, Bluemix will generate your project space onto a server for public use. Since we have a completed Java Application, we will be pushing our project to the cloud through Eclipse Luna IDE. Here are the instructions below to push your project to the cloud:

<img src="https://github.com/idarwish1/images/blob/master/BluemixInstructions.png" alt="alt text" width="" height="200">


After this, now in Eclipse Luna IDE do the following:


<img src="https://github.com/idarwish1/images/blob/master/BluemixInstructions2.png" alt="alt text" width="" height="250">

### iOS application 
(everything within the iPhoneApp folder)

Since we have used cocoapods in the application, you need to install the pods in our application as well. For that you have to open up the terminal, go to the directory where your application project is located (You can use cd (path) to change the directory). Once you get to the specific directory, type pod install. This will install the podfiles required to compile the project. 

<img src="https://github.com/idarwish1/images/blob/master/appiconscreen.png" alt="alt text" width="" height="400">

<img src="https://github.com/idarwish1/images/blob/master/splashscreen.PNG" alt="alt text" width="" height="400">






#### Microlocation tab

<img src="https://github.com/idarwish1/images/blob/master/microlocation.PNG" alt="alt text" width="" height="400">

<img src="https://github.com/idarwish1/images/blob/master/microlocationthings.png" alt="alt text" width="" height="400">



In this tab the first thing we are observing in the code is the iBeacon UUID information and can change depending on the beacons you have. It starts out by prompting the user on whether or not they allow their location to be tracked via location services. It then goes into the ranging of the beacons. The code moves onto the locationManager() method to receive the beacon data that is being transmitted to the iPhone. Once beacons are detected the iPhone goes into the for loop and sends the iBeacon packets out to the server via the corresponding URL. Our app has utilized bluemix so the bluemix pathname for the data to be sent to is provided. Once the response object comes back in JSON, the app then parses the object for the necessary coordinates from the server to then plot in the proper space on the map of the room. The location is then removed and the loop starts back over again for the next user's location. 





#### Geofencing tab

<img src="https://github.com/idarwish1/images/blob/master/geofencing.PNG" alt="alt text" width="" height="400">

In this tab the first thing we are observing in the code is the iBeacon UUID information and can change depending on the beacons you have. It goes into the ranging of the beacons and then prompts the user on whether or not they allow their location to be tracked via location services. It then loads the table rows based on the number of beacons in the room which is displayed under the beacon proximity map. The code moves onto the locationManager() method to receive the beacon data that is being transmitted to the iPhone. The locationManager() method reloads the table each time it is called and updates the table data on the RSSI, accuracy, proximity and UUID in real time. This is displayed to the user as they are around the iBeacon and moving around.






#### Proximity tab


<img src="https://github.com/idarwish1/images/blob/master/proximity.PNG" alt="alt text" width="" height="400">

In this tab the first thing we are observing in the code is the iBeacon UUID information and can change depending on the beacons you have. It goes into the ranging of the beacons and then prompts the user on whether or not they allow their location to be tracked via location services. It then loads the table rows based on the number of beacons in the room which is displayed under the beacon proximity map. The code moves onto the locationManager() method to receive the beacon data that is being transmitted to the iPhone. The locationManager() method reloads the table each time it is called and updates the table data on the RSSI, accuracy, proximity and UUID in real time and moves on to plot the iBeacon location based on the proximity of the user's iPhone to the iBeacon.


## Raspberry Pi

<img src="https://github.com/idarwish1/images/blob/master/rpi1bplus.png" alt="alt text" width="" height="400">

Raspberry Pi is the actuator for the “Internet of Things” system. The Raspberry Pi will initiate the devices once a user is in a specific location. It will be running a python script that will be continuously checking the database to see if the server has sent a flag indicating that the user is in a specific location.

Instructions:

1.) Connect the WiFi Dongle to the Raspberry Pi

2.) Turn on the Raspberry Pi

3.) Download the QueryDatabase.py script

4.) Download the WeMo.py script

5.) Download the mp3 and mp4 files that you want to play

6.) Put these files into a directory and ensure lines 70 and 85 (found in QueryDatabase.py) are correlating correctly with the names of the mp3 and mp4 files

7.) Change wemo1ip and wemo2ip (lines 30 and 31 of QueryDatabase.py) to the current IP addresses of your WeMo switches

8.) Run the QueryDatabase.py script with username and password of your Cloudant Database








