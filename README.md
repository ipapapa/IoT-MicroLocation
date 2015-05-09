# IoT-MicroLocation
The following is an end-to-end Internet of Things  project that was carried out as part of an independent study at Department of Computer and Information Technology, Purdue University under the supervision of Dr. Ioannis Papapanagiotou. In this project, we used the gimbal ibeacons that run the proprietary Apple ibeacon protocol. Our goal of the project was to implement an end to end location aware solution as shown in the image below.

![IoT-MicroLocation Flow](ianscotthamilton.github.com/ianscotthamilton.github.io/img/microlocation_flow.png)

An iPhone application that utilized the CoreLocation and CoreBluetooth Framework of the iOS to obtain the beacon (signals) from the ibeacons (BLE enabled devices) and a Java Tomcat Servlet with a MySQL database were developed by the team to implement our solution. The iOS application initally ranges the beacons for the supplied UUID and then sends their information (uuid, major, minor) to the server. The server uses the sent information to query the database, and returns information about the specific beacon to the iOS app. The iOS app uses this information, along with a beacon's proximity to determine whether it should call a script on the server to do something such as turn on an applicance plugged in with a Wemo switch. In order to use the code found on this repository to create your own iBeacons application, follow the guide below.

There are two different applications within the github repository, the Java servlet application and the iOS application. Both will require minor changes to work with your specific environment and those changes are detailed below. In addition to tweaking these two projects, this guide will finally show you how to create your database and deploy your server on AWS using its EC2 instance and Elastic Beanstalk.

# Java application (the project folder is the micro-location folder inside the java-server folder)

There are only two parts of the Java application that will change based on your environment and that is the IP address associated with your IoT device (in our case a Wemo switch) and the mysql database information in the BeaconAuthenticator class. 

Open in the project in an IDE or open WemoController.java in a text editor and scroll to lines 93 and 97 which are inside the executeWemoScript() method. On these two lines a string is defined that points to the location of your script and that path is followed by an IP address and either the word on or off. The only part of these two lines that should need to be changed is the IP address. This should be changed to the IP address of your connected Wemo switch.

Next open BeaconAuthenticator.java in whatever editor you like and scroll to line 34 in the method connectToSQLDatabase(). The current line reads: 
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/microlocation_aws", "root", “”); You should replace microlocation_aws with the name of the database you are going to create in the next step. Root signifies that you are logging in as the root user and the following empty quotes are for the password. If you left the default blank password then it doesn’t need to change, however if you created a password, you will need to enter it inside those quotations. localhost can actually be left because when this is exported and deployed on the Elastic Beanstalk instance, the database will be on the same instance, therefore it is the localhost to which you want to connect.

In anticipation of the next step, once the server application is completely updated, open the project in Eclipse. Then go to File > Export and choose Web and then War. Follow the prompts and if you know what server you are building for choose it and then finish the prompt. This should have created a .war file wherever you chose. This is the file that you will use to deploy your application on AWS.

# Amazon AWS Elastic Beanstalk EC2 instance

This is the most time consuming part of this process. This is because you need to create the instance, open the correct ports, install mysql, and finally create and populate the database within your instance.
Log into your Amazon AWS account. In the dashboard select Elastic Beanstalk under the “Deployment & Management” section. Follow the steps to create and launch a new instance and if you don’t already have one, create and associate a key pair. 

Once the instance has been instantiated, navigate to the instance in AWS and scroll to the security group column. Click on the security group to edit the open ports for this instance. Click on the Inbound tab and then the Edit button. Some of these may already be open however you will want to open TCP 80 (HTTP), TCP 8080 (Apache Tomcat), TCP 22 (SSH), and TCP 3306 (MySQL). 

Next open a command line interface session (Terminal or Command Prompt) and cd into the directory containing the downloaded .pem key file. (This is for ease, otherwise you would need to use the full path to the .pem file). To connect to the instance via ssh execute the following command:  ssh -i keyname.pem ec2-user@public-dns. If it works it will ask you if you want to connect. Type yes and you will connect to the instance.

Normally you would have to install tomcat, however the Elastic Beanstalk instance does this for you. Since it is installed for you, the first thing to do is install mysql. Do this by executing the following command: sudo yum install mysql-server . After it runs it will ask if you want to download the relevant files. Type y or yes to finish the download and installation.

Start the mysql service by executing the following command: sudo service mysqld start

Once the service is started, enter the mysql command line with the following command: mysql -u root -p  When prompted for a password hit Enter and you will be in the mysql command line.

To create your database, execute the following command: CREATE DATABASE database_name;

Now that your database has been created, you will want to access and populate it with your data. The easiest way to do this is to connect through SSH via a GUI application. The application I am using is Sequel Pro. Open the application, choose the SSH tab, and fill in the following information
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

Test connection can be used to make sure it works. If it is successful, you can use Add to Favorites so that you don’t have to enter the information every time you connect. (If you haven’t configured a static (elastic) IP in AWS, the SSH Host name could change though). Finally click Connect and you will be able to create tables using the Query tab. Once your table(s) have been created, you can create the columns in the Structure tab, and then populate the columns with your data in the Content tab.

In the Amazon Dashboard, navigate back to the Elastic Beanstalk dashboard. Create an Elastic Beanstalk environment and once it has loaded and is running (Health status is green). Click the Upload and Deploy button and a menu will appear. Click Choose File and select the .war file you created at the end of the Java application step. Give it a version label and choose Deploy. This will create a public URL that runs your application.

If you choose to use an auto-scaling group to manage load balancing, know that stopping the instance without removing it from the auto-scaling group will actually terminate the instance and require you to do the previous steps all over again.

# iOS application (everything within the microlocation folder)

The first thing that might need to change is the second line of AppDelegate.m which declares your UUID. This will be unique to your beacons and so it should be altered to reflect your UUID. The next two changes will be in the two service classes (GetBeaconService.m and WemoScriptService.m). Within these two classes under the + (WemoScriptService *)sharedClient method, there is an initWithBaseURL line that takes a string with the base URL of the service call. Replace this line in both service classes listed above with the url of your Elastic Beanstalk instance. These lines should be the only things that need to change within the iOS application.







