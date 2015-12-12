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


public class WemoController3 extends HttpServlet{
	private static final long serialVersionUID = 1L;
	 ParticleFilter filter;
	    Particle robot;
	  /*  final Point[] landmarks = new Point[]{new Point(1.5748000d, 0.00d), new Point(3.149599d,.850899d),
                new Point(3.149599d, 3.987779d), new Point (1.498600d,4.7497998d),new Point (0d,3.759199d),
                new Point(0d,.965200d)};*/
	  /*  final Point[] landmarks = new Point[]{new Point(FTM(3,8.5),0.0d ), new Point(FTM(17,4),0.0d),
                new Point (FTM(18,3.5),FTM(10,0)), new Point(FTM(12,7),FTM(17,11)), new Point(FTM(2,11.5),FTM(17, 11)),
                new Point(0.0d, FTM(12,0))};*/
	    //final Point[ landmarks = new Point[]{new Point(0.00d, 0.0d), new Point(3.00d,0.00d),
          //      new Point(1.5d, 3.00d)};
	    //final Point[] landmarks = new Point[]{new Point(0.00d, 0.00d), new Point(3.00d,3.00d)};
	    //final Point[] landmarks = new Point[]{new Point(FTM(0,10.5), 0.00d )};//, new Point(FTM(6, 11.7),0.0d),
               // new Point (FTM(5, 3),FTM(10, 0))}; //new Point(0.00d,3.00d)};
	    //final Point[] landmarks = new Point[]{new Point(0.00d, 0.00d), new Point(3.00d,0.00d), new Point(0.00d,1.50d),
	    //new Point(3.00d,1.50d), new Point(0.00d,3.00d), new Point(3.00d,3.00d)};
	    
	    final Point[] landmarks = new Point[]{new Point(0.00d, 0.00d), new Point(0.00d,2.00d), new Point(3.00d,2.00d), new Point(3.00d,0.00d)};
	    DataOutputStream dataOut = null;    
	boolean OUT = false;
	String homefolder = System.getProperty("user.home");
	
	//File file1 = new File("/Users/AllahBless/Desktop/beaconReadings.txt");
		
	//current 1.524,2.7432
	
	final int NUM_PARTICLES = 6000;//10000;
	final double WORLD_WIDTH = 3.00, WORLD_HEIGHT = 2.00;
	private iBeacon[] re = new iBeacon[4];
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
	
	
			
	
    public WemoController3() {
        super();
    }
    private void setUp() 
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
		
		//Depending on the minor value of iBeacon, collect rssi value and store it in proper location of array
		
		
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
		//****fill up array with rssi values
		
		if(identity.equals("5")){ 
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
		
		
		
		if (checkReFull(re)) //if im not sure which block I am in, then we use particle filtering
		{	
			try{
				filter.resample(robot.sense(re)); //changed the function
				Particle p = filter.getBestParticle();
			    System.out.println("Best Particle ={" +p.x+","+p.y+"}");
				//System.out.println("x= "+p.x);
			    //double [] [] test={{p.x},{p.y}}; //measurement obtained  i.e. z 
				/*double [] [] test={{p.x}, {0}};
				Matrix mat=new Matrix(test);
				//for(int i=0;i<3;i++)
				//{

				kalman1.Predict();
			
				kalman1.Correct(mat);
			
				//}
				Matrix t=kalman1.getState_post();
				//double tt=t.get(0, 0);
			//	System.out.println(t.get(0,0));
				
				 double [] [] test1={{p.y},{0}}; //measurement obtained  i.e. z 
					
					Matrix mat1=new Matrix(test1);
					for(int i=0;i<3;i++)
					{

					kalman2.Predict();
				
					kalman2.Correct(mat1);
				
					}
					Matrix t1=kalman2.getState_post();
					//double tt=t.get(0, 0);
					//System.out.println(t.get(0, 0)+" "+t1.get(0, 0));
					//System.out.println(kalman1.getError_cov_post());
					//System.out.println(kalman1.getProcess_noise_cov());
					//System.out.println(kalman2.getMeasurement_noise_cov());
					//System.out.println(t.get(0,0));
					/*    double [] [] test={{p.x},{p.y},{0},{0}}; //measurement obtained  i.e. z 
				//	double [] [] test={{p.x,p.y,0,0}}	;
						Matrix mat=new Matrix(test);
					//	for(int i=0;i<3;i++)
					//	{
					
						kalman.Predict();
					
						kalman.Correct(mat);
					
					//	}
						Matrix t=kalman.getState_post();
						System.out.println(t.get(0, 0)+" "+t.get(1,0));  */
			    String results[]=new String[2];
			    
			    //results[0]=new Double(p.x).toString(); //particle filter
			    //results[1]=new Double(p.y).toString(); //particle filter
			    //results[0]=new Double(t.get(0, 0)).toString(); //kalman filter
			    //results[1]=new Double(t1.get(0, 0)).toString(); //kalman filter
			    results[0]=new Double(p.x).toString();
			    results[1]=new Double(p.y).toString();
			   // System.out.println("Kalman: {"+results[0]+","+results[1]+"}");
			    rd = request.getRequestDispatcher("/beacon_success2.jsp");
			    request.setAttribute("xcoordinate", results[0]);
			    request.setAttribute("ycoordinate", results[1]);
				rd.forward(request, response);
			
			    re = new iBeacon[re.length]; 
				}
			catch (Exception e1){
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

