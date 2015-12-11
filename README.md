
# IoT-MicroLocation
An end-to-end Internet of Things (IoT) software package for Micro-Location, Proximity Aware services and Geofencing using Apple's iBeacons and set of Cloud services. The project is a collaboration effort of the teams from Purdue University and North Carolina State University advised by Dr. Ioannis Papapanagiotou - ipapapa@ncsu.edu.

# Readme

Below you may find information about the Java server application, Amazon AWS installation, and the iOS application.

The [Wiki](https://github.com/ipapapa/IoT-MicroLocation/wiki) contains further information about additional components that can be added to the project.

## Examples and Demos
[Proximity based interaction with iBeacons at NCSU Hunt Library](http://www.youtube.com/watch?v=AwckTkpN4-Y)
 
[Microlocation and Geofencing - Purdue University](http://www.youtube.com/watch?v=QCtc7z6PC70)

Our research paper of how iBeacons can be used for MicroLocation: F. Zafari, I. Papapanagiotou, ["Enhancing iBeacon based Micro-Location with Particle Filtering"](http://people.engr.ncsu.edu/ipapapa/Files/globecom2015.pdf), IEEE GLOBECOM 2015, San Diego CA.


# Technical Summary

![IoT-MicroLocation Flow](http://ianscotthamilton.github.io/microlocation_flow.png)

The iPhone application utilizes the CoreLocation and CoreBluetooth Framework of the iOS to obtain the beacons (signals) from the ibeacons (BLE enabled devices). A Java Tomcat Servlet with a MySQL database can be deployed on Amazon's AWS. The iOS application ranges the beacons for the supplied UUID and then sends the corresponding information (uuid, major, minor) to the server. The server uses the sent information to query the database, and returns information about the specific beacon to the iOS app. The iOS app uses this information, along with a beacon's proximity to determine whether it should call a script on the server to perform an action, such as turn on an appliance plugged in with a Wemo switch. In order to use the code found on this repository to create your own iBeacons application, follow the guide below.

There are two different applications within the github repository, the Java servlet application and the iOS application. Both will require minor changes to work with your specific environment and those changes are detailed below. In addition to tweaking these two projects, this guide will finally show you how to create your database and deploy your server on AWS using its EC2 instance and Elastic Beanstalk.

## Java application 
(the project folder is the micro-location folder inside the java-server folder. Also, the logincontroller currently in the project has no use and is going to be included in the future versions. ) 

There are only two parts of the Java application that will change based on your environment and that is the IP address associated with your IoT device (in our case a Wemo switch) and the mysql database information in the BeaconAuthenticator class. 

Open in the project in an IDE or open WemoController.java in a text editor and scroll to lines 93 and 97 which are inside the executeWemoScript() method. On these two lines a string is defined that points to the location of your script and that path is followed by an IP address and either the word on or off. The only part of these two lines that should need to be changed is the IP address. This should be changed to the IP address of your connected Wemo switch. 

Next open BeaconAuthenticator.java in whatever editor you like and scroll to line 34 in the method connectToSQLDatabase(). The current line reads: 
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/microlocation_aws", "root", “”); You should replace microlocation_aws with the name of the database you are going to create in the next step. Root signifies that you are logging in as the root user and the following empty quotes are for the password. If you left the default blank password then it doesn’t need to change, however if you created a password, you will need to enter it inside those quotations. localhost can actually be left because when this is exported and deployed on the Elastic Beanstalk instance, the database will be on the same instance, therefore it is the localhost to which you want to connect.

In anticipation of the next step, once the server application is completely updated, open the project in Eclipse. Then go to File > Export and choose Web and then War. Follow the prompts and if you know what server you are building for choose it and then finish the prompt. This should have created a .war file wherever you chose. This is the file that you will use to deploy your application on AWS.

## Setting up your IaaS
### Amazon AWS Elastic Beanstalk EC2 instance

This is the most time consuming part of this process. This is because you need to create the instance, open the correct ports, install mysql, and finally create and populate the database within your instance.
Log into your Amazon AWS account. In the dashboard select Elastic Beanstalk under the “Deployment & Management” section. Follow the steps to create and launch a new instance and if you don’t already have one, create and associate a key pair. 

Once the instance has been instantiated, navigate to the instance in AWS and scroll to the security group column. Click on the security group to edit the open ports for this instance. Click on the Inbound tab and then the Edit button. Some of these may already be open however you will want to open TCP 80 (HTTP), TCP 8080 (Apache Tomcat), TCP 22 (SSH), and TCP 3306 (MySQL). 

Next open a command line interface session (Terminal or Command Prompt) and cd into the directory containing the downloaded .pem key file. (This is for ease, otherwise you would need to use the full path to the .pem file). To connect to the instance via ssh execute the following command:  ssh -i keyname.pem ec2-user@public-dns. If it works it will ask you if you want to connect. Type yes and you will connect to the instance.

Normally you would have to install tomcat, however the Elastic Beanstalk instance does this for you. Since it is installed for you, the first thing to do is install mysql. Do this by executing the following command: sudo yum install mysql-server . After it runs it will ask if you want to download the relevant files. Type y or yes to finish the download and installation.

Start the mysql service by executing the following command: sudo service mysqld start

Once the service is started, enter the mysql command line with the following command: mysql -u root -p  When prompted for a password hit Enter and you will be in the mysql command line.

To create your database, execute the following command: CREATE DATABASE database_name;

Now that your database has been created, you will want to access and populate it with your data.  The easiest way to do this is to connect through SSH via a GUI application. The application I am using is Sequel Pro. Open the application, choose the SSH tab, and fill in the following information
	Name: database_name
	MySQL Host: 127.0.0.1 (This assigns it to the localhost of the instance being ssh’d into)
	Username: root
	Password: (This can be left blank unless a password has been assigned)
	Database: database_name
	Port: 3306

	SSH Host: your_instance_public_dns
	SSH Username: ec2-user
	SSH Key: Click the key icon and select your .pem file
	SSH Port: 22

Test connection can be used to make sure it works. If it is successful, you can use Add to Favorites so that you don’t have to enter the information every time you connect. (If you haven’t configured a static (elastic) IP in AWS, the SSH Host name could change though). Finally click Connect and you will be able to create tables using the Query tab. Once your table(s) have been created, you can create the columns in the Structure tab, and then populate the columns with your data in the Content tab. The components that your table would contain are in 5 columns as specified below (uuid VARCHAR(40), major VARCHAR(20), minor VARCHAR(20), name VARCHAR(40), usecase VARCHAR(80)) with minor being the primary key.

In the Amazon Dashboard, navigate back to the Elastic Beanstalk dashboard. Create an Elastic Beanstalk environment and once it has loaded and is running (Health status is green). Click the Upload and Deploy button and a menu will appear. Click Choose File and select the .war file you created at the end of the Java application step. Give it a version label and choose Deploy. This will create a public URL that runs your application.

If you choose to use an auto-scaling group to manage load balancing, know that stopping the instance without removing it from the auto-scaling group will actually terminate the instance and require you to do the previous steps all over again.

### IBM Softlayer

### iOS application 
(everything within the iPhoneApp folder)

Since we have used cocoapods in the application, you need to install the pods in our application as well. For that you have to open up the terminal, go to the directory where your application project is located (You can use cd (path) to change the directory). Once you get to the specific directory, type pod install. This will install the podfiles required to compile the project. 

#### Microlocation tab

In this tab the first thing we are observing in the code is the iBeacon UUID information and can change depending on the beacons you have. It starts out by prompting the user on whether or not they allow their location to be tracked via location services. It then goes into the ranging of the beacons. The code moves onto the locationManager() method to receive the beacon data that is being transmitted to the iPhone. Once beacons are detected the iPhone goes into the for loop and sends the iBeacon packets out to the server via the corresponding URL. Our app has utilized bluemix so the bluemix pathname for the data to be sent to is provided. Once the response object comes back in JSON, the app then parses the object for the necessary coordinates from the server to then plot in the proper space on the map of the room. The location is then removed and the loop starts back over again for the next user's location. 

#### Geofencing tab

In this tab the first thing we are observing in the code is the iBeacon UUID information and can change depending on the beacons you have. It goes into the ranging of the beacons and then prompts the user on whether or not they allow their location to be tracked via location services. It then loads the table rows based on the number of beacons in the room which is displayed under the beacon proximity map. The code moves onto the locationManager() method to receive the beacon data that is being transmitted to the iPhone. The locationManager() method reloads the table each time it is called and updates the table data on the RSSI, accuracy, proximity and UUID in real time. This is displayed to the user as they are around the iBeacon and moving around.


#### Proximity tab

In this tab the first thing we are observing in the code is the iBeacon UUID information and can change depending on the beacons you have. It goes into the ranging of the beacons and then prompts the user on whether or not they allow their location to be tracked via location services. It then loads the table rows based on the number of beacons in the room which is displayed under the beacon proximity map. The code moves onto the locationManager() method to receive the beacon data that is being transmitted to the iPhone. The locationManager() method reloads the table each time it is called and updates the table data on the RSSI, accuracy, proximity and UUID in real time and moves on to plot the iBeacon location based on the proximity of the user's iPhone to the iBeacon.










