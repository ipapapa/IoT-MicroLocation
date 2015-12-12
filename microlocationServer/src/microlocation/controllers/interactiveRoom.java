package microlocation.controllers;

//import PostToDatabase;
import jama.Matrix;

import java.io.IOException;

import jkalman.*;
import jkalman.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;  

import sun.tools.jar.CommandLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import microlocation.controllers.Particle;

import java.util.Random;
import java.util.Scanner;

import microlocation.models.BeaconAuthenticator;
import microlocation.models.iBeacon;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class interactiveRoom extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private String iBeaconRevisionNumb = "1078-d954ce4f7dea501c47c1630ac33e0287";
	private static String RaspberryPi = "";   
	ParticleFilter filter;
	Particle robot;
	final Point[] landmarks = new Point[]{new Point(0.00d, 0.00d), new Point(0.00d,2.00d), new Point(3.00d,2.00d), new Point(3.00d,0.00d)};
	DataOutputStream dataOut = null;    
	boolean OUT = false;
	String homefolder = System.getProperty("user.home");
	
	final int NUM_PARTICLES = 6000;//10000;
	final double WORLD_WIDTH = 3.00, WORLD_HEIGHT = 2.00;
	private iBeacon[] re = new iBeacon[4];
	private String[] Thing1 = new String[3];
	private String[] Thing2 = new String[3];
	private int indexThing1 = 0;
	private int indexThing2 = 0;
	private int[] averageArrayThing1 = new int[Thing1.length];
	private int[] averageArrayThing2 = new int[Thing2.length];
	RequestDispatcher rd = null;
	boolean setUpTest=true;
	JKalman kalman;
	JKalman kalman1;
	JKalman kalman2;
	boolean kTest=false;
	boolean k1Test=false;
	boolean k2Test=false;
	int SampleSize = 1;
	String[] beacon1 = new String[SampleSize];
	String[] beacon2 = new String[SampleSize];
	String[] beacon3 = new String[SampleSize];
	String[] beacon4 = new String[SampleSize];
	String[] beacon5 = new String[SampleSize];
	String[] beacon6 = new String[SampleSize];
	//String[] beacon7 = new String[SampleSize];
	//String[] beacon8 = new String[SampleSize];
	//String[] beacon9 = new String[SampleSize];
	int beacon1StringPoint = 0;
	int beacon2StringPoint = 0;
	int beacon3StringPoint = 0;
	int beacon4StringPoint = 0;
	int beacon5StringPoint = 0;
	int beacon6StringPoint = 0;
	//int beacon7StringPoint = 0;
	//int beacon8StringPoint = 0;
	//int beacon9StringPoint = 0;
	int sum1 = 0;
	int sum2 = 0;
	int sum3 = 0;
	int sum4 = 0;
	int sum5 = 0;
	int sum6 = 0;
	//int sum7 = 0;
	//int sum8 = 0;
	//int sum9 = 0;
	int average1 = 0;
	int average2 = 0;
	int average3 = 0;
	int average4 = 0;
	int average5 = 0;
	int average6 = 0;
	//int average7 = 0;
	//int average8 = 0;
	//int average9 = 0;
	
	//parameters to interact with Things
	//these values are stored in a database where Raspberry Pi will query
	//values are set depending on user location within room
	//room is set up as shown in iphone app picture
	
	//Thing1 array is data collected for turning on TV
	//Thing2 array is data collected for turning on "Students Desk" (Desk Lamp & Fan)
	//If "not watching TV" or "not studying" then: 
		//1) christmas tree lights are on and christmas music is playing
		//2) Showing user current 1mX1m block he or she is in
			//returning particle filtering data
	
	private String WeMoSwitch1 = "0";
	private String WeMoSwitch2 = "0";
	private String TV = "0";
	private String Speakers = "0";
	private int tempRSSI = 0;
	boolean DESK_ON = false;
	boolean ENTERTAINMENT_ON = false;
	boolean CHRISTMAS_ON = false;
	boolean MICROLOCATION_ON_1 = true;
	boolean MICROLOCATION_ON_2 = true;
	
	boolean postLock = false; //helps deal with multi-threading problem, otherwise CLOUDANTDB -> 409 error
	
	
			
	
    public interactiveRoom() {
        super();
    }
    private void setUp() //Particle Filtering Setup
    {
        filter = new ParticleFilter(NUM_PARTICLES, landmarks, WORLD_WIDTH, WORLD_HEIGHT);
        filter.setNoise(0.05f, 0.05f, 500f); //original values
        //filter.setNoise(0.03f, 0.05f, 1000f);
        robot = new Particle(landmarks, WORLD_WIDTH, WORLD_HEIGHT); //robot also as a particle randomly placed in the world 
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	boolean checkReFull(iBeacon[] array) {
		//for (String s : re)
		for (iBeacon s : array)
		{
		if (s == null){
			return false;
		}
		}
		return true;
	}
	
	boolean checkThingArrayFull(String[] array) {
		//for (String s : re)
		for (String s : array)
		{
			if (s == null){
				return false;
			}
			else
				continue;
		}
		
		return true;
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("servlet called");
		if(setUpTest)
		{
		
		if(kTest)
	 {
			try {
				initKalman();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(k1Test)
			 {
					try {
						initKalman1();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
		if(k2Test)
			 {
					try {
						initKalman2();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
		setUp();
		setUpTest=false;
		}
		//grab parameters from iPhone
		String identity=request.getParameter("minor");//should be minor
		String meters=request.getParameter("rssi");
		//System.out.println(identity + "|||"+ meters);
		
		
		

		/*if(identity.equals("686")){ //used for taking multiple rssi values from 1 beacon and then averaging
		beacon1[beacon1StringPoint++] = meters; //place current incoming value into array index
		if(beacon1StringPoint >= (SampleSize)){ //once array is full, take average
			for(int i = 0; i < SampleSize; i++){
				sum1 = sum1 + Integer.parseInt(beacon1[i]);
			}
			average1 = sum1/SampleSize;
			beacon1StringPoint = 0;
			sum1 = 0;
			re[0] = Integer.toString(average1);
			//System.out.println("re[0]");
		}
			
	
		if(identity.equals("63375")){
			beacon2[beacon2StringPoint++] = meters;
			if(beacon2StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum2 = sum2 + Integer.parseInt(beacon2[i]);
				}
				average2 = sum2/SampleSize;
				beacon2StringPoint = 0;
				sum2 = 0;
				re[1] = Integer.toString(average2);
				//System.out.println("re[1]");
			}
		}
		
			
		
		if(identity.equals("22335")){
			beacon3[beacon3StringPoint++] = meters;
			if(beacon3StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum3 = sum3 + Integer.parseInt(beacon3[i]);
				}
				average3 = sum3/SampleSize;
				beacon3StringPoint = 0;
				sum3 = 0;
				re[2] = Integer.toString(average3);
				//System.out.println("re[2]");
			}
		}
		
		
		
		if(identity.equals("9815")){
			beacon4[beacon4StringPoint++] = meters;
			if(beacon4StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum4 = sum4 + Integer.parseInt(beacon4[i]);
				}
				average4 = sum4/SampleSize;
				beacon4StringPoint = 0;
				sum4 = 0;
				re[3] = Integer.toString(average3);
				//System.out.println("re[3]");
			}
		}
		
		
		if(identity.equals("5692"))	{
			beacon5[beacon5StringPoint++] = meters;
			if(beacon5StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum5 = sum5 + Integer.parseInt(beacon5[i]);
				}
				average5 = sum5/SampleSize;
				beacon5StringPoint = 0;
				sum5 = 0;
				re[4] = Integer.toString(average5);
				//System.out.println("re[4]");
			}
		}
	
		
		if(identity.equals("10353"))	{
			beacon6[beacon6StringPoint++] = meters;
			if(beacon6StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum6 = sum6 + Integer.parseInt(beacon6[i]);
				}
				average6 = sum6/SampleSize;
				beacon6StringPoint = 0;
				sum6 = 0;
				re[5] = Integer.toString(average6);
				//System.out.println("re[5]");
			}
		}
		
		/*if(identity.equals("60082")){
			beacon7[beacon7StringPoint++] = meters;
			if(beacon7StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum7 = sum7 + Integer.parseInt(beacon7[i]);
				}
				average7 = sum7/SampleSize;
				beacon7StringPoint = 0;
				sum7 = 0;
				re[6] = Integer.toString(average7);
				//System.out.println("re[6]");
			}
		}
		
		if(identity.equals("26060")){
			beacon8[beacon8StringPoint++] = meters;
			if(beacon8StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum8 = sum8 + Integer.parseInt(beacon8[i]);
				}
				average8 = sum8/SampleSize;
				beacon8StringPoint = 0;
				sum8 = 0;
				re[7] = Integer.toString(average8);
				//System.out.println("re[6]");
			}
		}
		
		if(identity.equals("10353")){
			beacon9[beacon9StringPoint++] = meters;
			if(beacon9StringPoint >= (SampleSize)){
				for(int i = 0; i < SampleSize; i++){
					sum9 = sum9 + Integer.parseInt(beacon9[i]);
				}
				average9 = sum9/SampleSize;
				beacon9StringPoint = 0;
				sum9 = 0;
				re[8] = Integer.toString(average8);
				//System.out.println("re[6]");
			}
		}
		*/

		
		if(identity.equals("1")){// Thing1 array to take in a Sample of pre-chosen size					
			Thing1[indexThing1++] = meters;
			if(indexThing1 > (Thing1.length - 1))
				indexThing1 = 0;
		}
		
		if(identity.equals("3")){// Thing2 array to take in a Sample of pre-chosen size	
			Thing2[indexThing2++] = meters;
			if(indexThing2 > (Thing2.length - 1))
				indexThing2 = 0;
		}
		
		
		if(identity.equals("5")){ //place iBeacon values in array for microlocation
			re[0] = new iBeacon("5", meters);
		}
		
		if(identity.equals("6")){
			re[1] = new iBeacon("6", meters);
		}

		if(identity.equals("2")){
			re[2] = new iBeacon("2", meters);
		}
		
		if(identity.equals("4")){
			re[3] = new iBeacon("4", meters);
		}
		
		//interacting with Things based on Proximity
		//To eleviate spikes in RSSI values, we took the average RSSI from a set of SAMPLE (SAMPLESIZE)
		//only if AVERAGE values is below threshold, we will turn on Thing
		if(checkThingArrayFull(Thing1)){ //when array is full, get average of RSSI values
			//Thing1ArrayCheckLock = false;
			int sum = 0;
			int average = 0;
			System.out.println("THING 1 ARRAY FULL");
			for(int i = 0; i < Thing1.length; i++ ) //to deal with spikes of RSSI, we will take an average of RSSI values from iBeacon
			{
				try{
					averageArrayThing1[i] = Integer.parseInt(Thing1[i]);
					if(averageArrayThing1[i] == 0){//this insures that we dont have any invalid data that will invalidate the "average"
						averageArrayThing1[i] = -1000; //really large value that will make sure average is not > -67
					}
				}
				catch(NumberFormatException NFE){
					averageArrayThing1[i] = -60;
				}
						
			}
			for(int i = 0; i<averageArrayThing1.length; i++) //get average
				sum += averageArrayThing1[i];
			
			average = sum/averageArrayThing1.length;
			
			if((average > -60)){ //only if average is less than a certain value, we will turn on device	
				MICROLOCATION_ON_1 = false; //if user is by TV, then dont do particle filtering calculation
				//set database parameters
				TV = "1";
				WeMoSwitch1 = "0";
				WeMoSwitch2 = "0";
				Speakers = "0";
				String results[]=new String[2];
				  
				results[0]= "20.0"; //sending hardcoded values so iPhone app can plot user next to TV
				results[1]="20.0";
				rd = request.getRequestDispatcher("/beacon_success2.jsp");
				request.setAttribute("xcoordinate", results[0]);
				request.setAttribute("ycoordinate", results[1]);
				rd.forward(request, response);
				
			 	if(!ENTERTAINMENT_ON && postLock == false){ //only post to database if we have a change of state
			 		postLock = true;
					ENTERTAINMENT_ON = true;
					DESK_ON = false;
					CHRISTMAS_ON = false;
					PostToDatabase send = new PostToDatabase("ThingsParameters",iBeaconRevisionNumb, WeMoSwitch1, WeMoSwitch2, TV, Speakers);
					iBeaconRevisionNumb = send.post();
					System.out.println("ENTERTAINMENT ON");
					postLock = false;

				}	 	
				
			}
			else{ //turn Thing OFF
				MICROLOCATION_ON_1 = true;
				TV = "0";
				WeMoSwitch1 = "0";
				WeMoSwitch2 = "0";
				Speakers = "0";
				if(ENTERTAINMENT_ON && postLock == false){
					postLock = true;
					ENTERTAINMENT_ON = false;
					DESK_ON = false;
					CHRISTMAS_ON = false;
					PostToDatabase send = new PostToDatabase("ThingsParameters",iBeaconRevisionNumb, WeMoSwitch1, WeMoSwitch2, TV, Speakers);
					iBeaconRevisionNumb = send.post();
					System.out.println("ENTERTAINMENT OFF");
					postLock = false;

				}
			}
			re = new iBeacon[re.length];
			Thing1 = new String[Thing1.length];
			averageArrayThing1 = new int[averageArrayThing1.length];
			//Thing1ArrayCheckLock = true;
		}
		
		//Thing2 is interacting with "StudentsDesk" -> Fan and Desk Lamp
		else if(checkThingArrayFull(Thing2)){ //interacting with Things ->Same as working with THING1
			//Thing2ArrayCheckLock = false;
			System.out.println("Thing2ArrayFull");
			int sum = 0;
			int average = 0;
			
			for(int i = 0; i < Thing2.length; i++ ) 
			{
				try{
					averageArrayThing2[i] = Integer.parseInt(Thing2[i]);
				
					if(averageArrayThing2[i] == 0){//this insures that we dont have any invalid data that will invalidate the "average"
						averageArrayThing2[i] = -1000;
					}
				}
				catch(NumberFormatException NFE){
					averageArrayThing2[i] = -60;
				}
			}
			for(int i = 0; i<averageArrayThing2.length; i++) //get average
				sum += averageArrayThing2[i];
			
			average = sum/averageArrayThing2.length;
			
			if((average > -60)){ //only if average is less than a certain value, we will turn on device	
				MICROLOCATION_ON_2 = false;
				TV = "0";
				WeMoSwitch1 = "0";
				WeMoSwitch2 = "1";
				Speakers = "0";
				String results[]=new String[2];
				results[0]= "40.0"; //hardcoded value for iPhone app to plot in Thing2 Area
				results[1]="40.0";
				rd = request.getRequestDispatcher("/beacon_success2.jsp");
				request.setAttribute("xcoordinate", results[0]);
				request.setAttribute("ycoordinate", results[1]);
				rd.forward(request, response);
				
				 if(!DESK_ON && postLock ==false){
					 postLock = true;
					DESK_ON = true;
					CHRISTMAS_ON = false;
					ENTERTAINMENT_ON = false;
					PostToDatabase send = new PostToDatabase("ThingsParameters",iBeaconRevisionNumb, WeMoSwitch1, WeMoSwitch2, TV, Speakers);
					iBeaconRevisionNumb = send.post();
					System.out.println("DESK ON");
					postLock = false;
					
				}	
				
					
			}
			else{
				MICROLOCATION_ON_2 = true;
				TV = "0";
				WeMoSwitch1 = "0";
				WeMoSwitch2 = "0";
				Speakers = "0";
				if(DESK_ON && postLock ==false){
					postLock = true;
					DESK_ON = false;
					ENTERTAINMENT_ON = false;
					CHRISTMAS_ON = false;
					PostToDatabase send = new PostToDatabase("ThingsParameters",iBeaconRevisionNumb, WeMoSwitch1, WeMoSwitch2, TV, Speakers);
					iBeaconRevisionNumb = send.post();
					System.out.println("DESK OFF");
					postLock = false;
					
				}
				
			}
			re = new iBeacon[re.length];
			Thing2 = new String[Thing2.length];
			averageArrayThing2 = new int[averageArrayThing2.length];
			//Thing2ArrayCheckLock = false;
		}
		
		//if not in Thing1 Area or Thing2 area, then use microlocation algorithm
		else if ((MICROLOCATION_ON_1 && MICROLOCATION_ON_2) && checkReFull(re)) //if im not sure which block I am in, then we use particle filtering
		{	
			boolean DONE = false;
			try {
				
				for(int i = 0; i< re.length; i++){ //if I am close to a certain beacon, then I know im in that block
					tempRSSI = Integer.parseInt(re[i].rssi);
					System.out.println(re[i].name+": "+tempRSSI);
					if(tempRSSI >= -67){ //if value is below threshold, then I know user is in a certain block
						String results[]=new String[2];
						switch(re[i].name){
							case "6": results[0]="0.1"; //dummy variable
									  results[1]="0.1";
									  System.out.println("BEACON"+re[i].name + "BLOCK");
									  break;
							case "2": results[0]="2.1"; //dummy variable
									  results[1]="0.1"; 
									  System.out.println("BEACON"+re[i].name + "BLOCK");
									  break;
							case "4": results[0]="2.1"; //dummy variable
							  		  results[1]="1.5"; 
							  		System.out.println("BEACON"+re[i].name + "BLOCK");
							  		  break;
							case "5": results[0]="0.1"; //dummy variable
							  		  results[1]="1.5";
							  		System.out.println("BEACON"+re[i].name + "BLOCK");
							  		  break;
							default: System.out.println("RANDOM BEACON #" + re[i].name + "in corner area Array");
									break;
						}
						
						rd = request.getRequestDispatcher("/beacon_success2.jsp");
						request.setAttribute("xcoordinate", results[0]);
						request.setAttribute("ycoordinate", results[1]);
						rd.forward(request, response);
						
						if(!CHRISTMAS_ON && postLock == false){
							postLock = true;
							TV = "0";
							WeMoSwitch2 = "0";
							WeMoSwitch1 = "1";
							Speakers = "1";
							PostToDatabase send = new PostToDatabase("ThingsParameters",iBeaconRevisionNumb, WeMoSwitch1, WeMoSwitch2, TV, Speakers);
							iBeaconRevisionNumb = send.post();
							System.out.println("ENTERTAINMENT OFF");
							CHRISTMAS_ON = true;
							DESK_ON = false;
							ENTERTAINMENT_ON = false;
							postLock = false;
						}
						re = new iBeacon[re.length];
						DONE = true;
						break;
							
					}
					else
						continue;
				}
				//System.out.println("checkReFull");
				if(DONE == false){ //If I am not sure which block user is in, then do particle filtering algorithm
				filter.resample(robot.sense(re)); //changed the function
				Particle p = filter.getBestParticle();
			    System.out.println("Best Particle ={" +p.x+","+p.y+"}");
				
			    String results[]=new String[2];
			
			    results[0]=new Double(p.x).toString();
			    results[1]=new Double(p.y).toString();
			 
			    rd = request.getRequestDispatcher("/beacon_success2.jsp");
			    request.setAttribute("xcoordinate", results[0]);
			    request.setAttribute("ycoordinate", results[1]);
				rd.forward(request, response);
				if(!CHRISTMAS_ON && postLock ==false){
					postLock = true;
					CHRISTMAS_ON = true;
					ENTERTAINMENT_ON = false;
					DESK_ON = false;
					TV = "0";
					WeMoSwitch2 = "0";
					WeMoSwitch1 = "1";
					Speakers = "1";
					PostToDatabase send = new PostToDatabase("ThingsParameters",iBeaconRevisionNumb, WeMoSwitch1, WeMoSwitch2, TV, Speakers);
					iBeaconRevisionNumb = send.post();
					System.out.println("CHRISTMAS OFF");
					postLock = false;
				}
			    re = new iBeacon[re.length]; 
				}
			} catch (Exception e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	
	}
	
	
	
	
	
	
	
	void initKalman() throws Exception
	{
		
	/*	kalman1=new JKalman(1,1);
		double [][]error={{100000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman1.setError_cov_pre(errorCov);
		double [][] processCov={{1}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman1.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.1}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman1.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman1.setState_pre(meow); 
		double [][]trans={{1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman1.setTransition_matrix(transition);
	    k1Test=false;*/ 
		kalman=new JKalman(4,4);
		double [][]error={{10000,0,0,0},{0,10000,0,0},{0,0,10000,0},{0,0,0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman.setError_cov_pre(errorCov);
		double [][] processCov={{0,0,0,0},{0,0,0,0},{0,0,1,0},{0,0,0,1}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{1000,0,0,0},{0,1000,0,0},{0,0,1000,0},{0,0,0,10}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{0.1,0.1,0,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman.setState_pre(meow); 
		double [][]trans={{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman.setTransition_matrix(transition);
	    kTest=false;
		
	}
	void initKalman1() throws Exception
		{
			
	/*		kalman1=new JKalman(1,1);
			double [][]error={{100000}};
			Matrix errorCov=new Matrix(error); // P matrix 
			kalman1.setError_cov_pre(errorCov);
			double [][] processCov={{0.0010}};// the increase in value of q makes the system rely more on measured value 
			Matrix processCovEr=new Matrix(processCov);  //Q
			kalman1.setProcess_noise_cov(processCovEr); //Q
			double [][]measurementError={{1}}; //R 
			Matrix measureCov=new Matrix(measurementError);
			kalman1.setMeasurement_noise_cov(measureCov); //R
			double [][] hello={{0}}; //initial value
			Matrix meow=new Matrix(hello); //initial value matrix 
			kalman1.setState_pre(meow); 
			double [][]trans={{1}};
			Matrix transition=new Matrix(trans); // F matrix
		    kalman1.setTransition_matrix(transition);
		    k1Test=false;*/
			kalman1=new JKalman(2,2);
			double [][]error={{3,0},{0,2}};
			Matrix errorCov=new Matrix(error); // P matrix  
			kalman1.setError_cov_pre(errorCov);
			double [][] processCov={{0.01,0},{0,0.01}};// the increase in value of q makes the system rely more on measured value 
			Matrix processCovEr=new Matrix(processCov);  //Q
			kalman1.setProcess_noise_cov(processCovEr); //Q
			double [][]measurementError={{0.04,0.00},{0.0,0.04}}; //R the decreased in the value of R causes a higher reliance on measured values
			Matrix measureCov=new Matrix(measurementError);
			kalman1.setMeasurement_noise_cov(measureCov); //R
			double [][] hello={{30,0}}; //initial value
			Matrix meow=new Matrix(hello); //initial value matrix 
			kalman1.setState_pre(meow); 
			double [][]trans={{1.0,0},{0,1.0}};
			Matrix transition=new Matrix(trans); // F matrix
		    kalman1.setTransition_matrix(transition);
		    k1Test=false;
		   

		}
	void initKalman2() throws Exception
		{
			
	/*		kalman1=new JKalman(1,1);
			double [][]error={{100000}};
			Matrix errorCov=new Matrix(error); // P matrix 
			kalman1.setError_cov_pre(errorCov);
			double [][] processCov={{0.0010}};// the increase in value of q makes the system rely more on measured value 
			Matrix processCovEr=new Matrix(processCov);  //Q
			kalman1.setProcess_noise_cov(processCovEr); //Q
			double [][]measurementError={{1}}; //R 
			Matrix measureCov=new Matrix(measurementError);
			kalman1.setMeasurement_noise_cov(measureCov); //R
			double [][] hello={{0}}; //initial value
			Matrix meow=new Matrix(hello); //initial value matrix 
			kalman1.setState_pre(meow); 
			double [][]trans={{1}};
			Matrix transition=new Matrix(trans); // F matrix
		    kalman1.setTransition_matrix(transition);
		    k1Test=false;*/
			kalman2=new JKalman(2,2);
			double [][]error={{0.0015,0.0},{0.0,0.0015}};
			Matrix errorCov=new Matrix(error); // P matrix  
			kalman2.setError_cov_pre(errorCov);
			double [][] processCov={{0.00001000,0},{0,0.000010}};// the increase in value of q makes the system rely more on measured value 
			Matrix processCovEr=new Matrix(processCov);  //Q
			kalman2.setProcess_noise_cov(processCovEr); //Q
			double [][]measurementError={{0.4010,0},{0,0.4010}}; //R 
			Matrix measureCov=new Matrix(measurementError);
			kalman2.setMeasurement_noise_cov(measureCov); //R
			double [][] hello={{10,0}}; //initial value
			Matrix meow=new Matrix(hello); //initial value matrix 
			kalman2.setState_pre(meow); 
			double [][]trans={{1.0,0},{0,1.0}};
			Matrix transition=new Matrix(trans); // F matrix
		    kalman2.setTransition_matrix(transition);
		    k2Test=false;

		}
	double FTM(double feet, double inches){ //feet to meter conversion
		double meterconversion = 0;
		
		meterconversion = (double) ( (feet + (inches/12) )*.3048);
		//System.out.print("In:" + feet +"ft,"+inches+"in =" + meterconversion );
		return meterconversion;
	}
}

