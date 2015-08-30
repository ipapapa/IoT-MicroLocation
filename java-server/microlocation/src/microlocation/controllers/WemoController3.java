package microlocation.controllers;

import jama.Matrix;

import java.io.IOException;

import jkalman.*;
import jkalman.*;

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
//@WebServlet("/WemoController3")
public class WemoController3 extends HttpServlet{
	private static final long serialVersionUID = 1L;
	 ParticleFilter filter;
	    Particle robot;
	    final Point[] landmarks = new Point[]{new Point(0.2d, 0.25d), new Point(5.741d,0.20d ),
	                                         new Point(2.9255d, 10.24d), new Point (2.9255,5.12d)
	                 ,new Point (0.0d,10.12d),new Point(2.9255d,0.52d)};//, new Point(5f, 80f),
	    //final Point[] landmarks = new Point[]{new Point(0.0, 0.0), new Point(1.0,0.0 ),
          //      new Point(0.5, 1), new Point (1.0,1.0)};
	                                         //new Point(700 - 10f, 40f)};//, new Point(17f, 92f)};  // get these points from the mobile app
	    final int NUM_PARTICLES = 2000;//10000;
	   final double WORLD_WIDTH = 5.851, WORLD_HEIGHT = 10.24;
	    //final double WORLD_WIDTH = 1, WORLD_HEIGHT = 1;
	private String[] re = new String[6];
	JKalman kalman;
	
	JKalman kalman1;
	JKalman kalman2;
	JKalman kalman3;
	JKalman kalman4;
	JKalman kalman5;
	JKalman kalman6;
	  boolean k1Test=true;
	  boolean k2Test=true;
	  boolean k3Test=true;
	  boolean k4Test=true;
	  boolean k5Test=true;
	  boolean k6Test=true;
	  boolean kTest=true;
	  boolean meow=true;
    public WemoController3() {
        super();
      
   
    }
    private void setUp() {
   
        filter = new ParticleFilter(NUM_PARTICLES, landmarks, WORLD_WIDTH, WORLD_HEIGHT);
        filter.setNoise(0.05f, 0.05f, 5f);
      //  filter.setNoise(0.05f, 0.05f, 105f);
        robot = new Particle(landmarks, WORLD_WIDTH, WORLD_HEIGHT); //robot also as a particle randomly placed in the world 
  
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	
	
	
	boolean checkReFull() {
		
		for (String s : re) {
			if (s == null) {
				return false;
			}
		}
		return true;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	if(k2Test)
		{
			try {
				initKalman2();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(k3Test)
		{
			try {
				initKalman3();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(k4Test)
		{
			try {
				initKalman4();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(k5Test)
		{
			try {
				initKalman5();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(k6Test)
		{
			try {
				initKalman6();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		setUp();
		//String[] re=new String [3];
		String identity=request.getParameter("landmark");
		String meters=request.getParameter("meters");
		
	//	System.out.println(identity+" "+meters);
	
		if(identity.equals("4"))
		{
	/*		double convert1=Double.parseDouble(meters);
			
			double [] [] test={{convert1}}; //measurement obtained  i.e. z 
			
			Matrix mat=new Matrix(test);
		//	for(int i=0;i<3;i++)
		//	{
		
			kalman1.Predict();
		
			kalman1.Correct(mat);
		
	//		}
			Matrix t=kalman1.getState_post();
			double tt=t.get(0, 0);
			DecimalFormat df=new DecimalFormat("0");
			String number=df.format(tt);
			
			//String meters1=Double.toString(number);
			re[0]=number;*/
			re[0]=meters;
		}
				if(identity.equals("5"))
		{
					
				/*	double convert1=Double.parseDouble(meters);
					double [] [] test={{convert1}}; //measurement obtained  i.e. z 
					
					Matrix mat=new Matrix(test);
		//			for(int i=0;i<3;i++)
		//			{
				
					kalman2.Predict();
				
					kalman2.Correct(mat);
				
		//			}
					Matrix t=kalman2.getState_post();
					double tt=t.get(0, 0);
					DecimalFormat df=new DecimalFormat("0");
					String number=df.format(tt);
				
					re[1]=number;
				//	String meters2=Double.toString(tt);
				//	re[1]=meters2;*/
			re[1]=meters;
			
		}
					if(identity.equals("6"))
		{
			/*	double convert1=Double.parseDouble(meters);
						double [] [] test={{convert1}}; //measurement obtained  i.e. z 
				
						Matrix mat=new Matrix(test);
			//			for(int i=0;i<3;i++)
				//		{
					
						kalman3.Predict();
					
						kalman3.Correct(mat);
					
				//		}
						Matrix t=kalman3.getState_post();
						double tt=t.get(0, 0);
						DecimalFormat df=new DecimalFormat("0");
						String number=df.format(tt);
						
						re[2]=number;
					//	String meters3=Double.toString(tt);
					//	re[2]=meters3;*/
			re[2]=meters;
		}
	if(identity.equals("1"))
		{
		/*double convert1=Double.parseDouble(meters);
		double [] [] test={{convert1}}; //measurement obtained  i.e. z 

		Matrix mat=new Matrix(test);
			for(int i=0;i<3;i++)
//		{
	
		kalman4.Predict();
	
		kalman4.Correct(mat);
	
//		}
		
		Matrix t=kalman4.getState_post();
		double tt=t.get(0, 0);
		DecimalFormat df=new DecimalFormat("0");
		String number=df.format(tt);
		
		re[3]=number;*/
		
			re[3]=meters;
		}
		
		
	if(identity.equals("3"))
	{
				
	/*			double convert1=Double.parseDouble(meters);
				double [] [] test={{convert1}}; //measurement obtained  i.e. z 
				
				Matrix mat=new Matrix(test);
	//			for(int i=0;i<3;i++)
	//			{
			
				kalman5.Predict();
			
				kalman5.Correct(mat);
			
	//			}
				Matrix t=kalman5.getState_post();
				double tt=t.get(0, 0);
				DecimalFormat df=new DecimalFormat("0");
				String number=df.format(tt);
			
				re[4]=number;
			//	String meters2=Double.toString(tt);
			//	re[1]=meters2;*/
		re[4]=meters;
		
	}
	if(identity.equals("2"))
	{
				
		/*		double convert1=Double.parseDouble(meters);
				double [] [] test={{convert1}}; //measurement obtained  i.e. z 
				
				Matrix mat=new Matrix(test);
	//			for(int i=0;i<3;i++)
	//			{
			
				kalman6.Predict();
			
				kalman6.Correct(mat);
			
	//			}
				Matrix t=kalman6.getState_post();
				double tt=t.get(0, 0);
				DecimalFormat df=new DecimalFormat("0");
				String number=df.format(tt);
			
				re[5]=number;*/
			//	String meters2=Double.toString(tt);
			//	re[1]=meters2;
		re[5]=meters;
		
	}
		//System.out.println(identity);
/*for(int i=0;i<re.length;i++)
{
System.out.println(re[i]);	
}*/
	//	System.out.println(result);
		   
		//setUp();
		if (checkReFull()) {
			try {
				filter.resample(robot.sense(re)); //changed the function
				 Particle p = filter.getBestParticle();
			       System.out.println("Best Particle "+p);
				 
	/*			 double [] [] test={{p.x},{p.y},{0},{0}}; //measurement obtained  i.e. z 
			//	double [] [] test={{p.x,p.y,0,0}}	;
					Matrix mat=new Matrix(test);
					for(int i=0;i<3;i++)
					{
				
					kalman.Predict();
				
					kalman.Correct(mat);
				
					}
					Matrix t=kalman.getState_post();
					System.out.println(t.get(0, 0)+" "+t.get(1,0));  */
			   //  p = filter.getAverageParticle();
				 //     System.out.println("Average Particle "+p);
			    	re = new String[6];
			    
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
	//	re = new String[3];
	
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
		double [][] processCov={{0,0,0,0},{0,0,0,0},{0,0,0.1,0},{0,0,0,0.1}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.1,0,0,0},{0,.1,0,0},{0,0,0.1,0},{0,0,0,0.1}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{0.2,0.2,0,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman.setState_pre(meow); 
		double [][]trans={{1,0,0.1,0},{0,1,0.1,0},{0,0,1,0},{0,0,0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman.setTransition_matrix(transition);
	    kTest=false;
		
	}
	void initKalman1() throws Exception
	{
		
		kalman1=new JKalman(1,1);
		double [][]error={{100000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman1.setError_cov_pre(errorCov);
		double [][] processCov={{0.00001}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman1.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.000001}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman1.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman1.setState_pre(meow); 
		double [][]trans={{1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman1.setTransition_matrix(transition);
	    k1Test=false;
	/*	kalman1=new JKalman(2,2);
		double [][]error={{100000,0},{0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman1.setError_cov_pre(errorCov);
		double [][] processCov={{1,0},{0,1}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman1.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.000010,0},{0,0.000010}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman1.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman1.setState_pre(meow); 
		double [][]trans={{1,0.1},{0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman1.setTransition_matrix(transition);
	    k1Test=false;*/
		
	}
	void initKalman2() throws Exception
	{

		kalman2=new JKalman(1,1);
		double [][]error={{100000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman2.setError_cov_pre(errorCov);
		double [][] processCov={{0.00011}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman2.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.00001}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman2.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman2.setState_pre(meow); 
		double [][]trans={{1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman2.setTransition_matrix(transition);
		k2Test=false;
	/*	kalman2=new JKalman(2,2);
		double [][]error={{100000,0},{0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman2.setError_cov_pre(errorCov);
		double [][] processCov={{1,0},{0,1}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman2.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.000010,0},{0,0.00010}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman2.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman2.setState_pre(meow); 
		double [][]trans={{1,0.1},{0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman2.setTransition_matrix(transition);
	    k2Test=false;*/
		
	}
	void initKalman3() throws Exception
	{
		
		kalman3=new JKalman(1,1);
		double [][]error={{100000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman3.setError_cov_pre(errorCov);
		double [][] processCov={{0.00011}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman3.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.00001}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman3.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman3.setState_pre(meow); 
		double [][]trans={{1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman3.setTransition_matrix(transition);
	    k3Test=false;
	/*	kalman3=new JKalman(2,2);
		double [][]error={{100000,0},{0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman3.setError_cov_pre(errorCov);
		double [][] processCov={{1,0},{0,1}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman3.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.000010,0},{0,000010}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman3.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman3.setState_pre(meow); 
		double [][]trans={{1,0.1},{0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman3.setTransition_matrix(transition);
	    k3Test=false;*/
	   
		
	}
	void initKalman4() throws Exception
	{
		kalman4=new JKalman(1,1);
		double [][]error={{100000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman4.setError_cov_pre(errorCov);
		double [][] processCov={{0.00001}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman4.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.00001}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman4.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman4.setState_pre(meow); 
		double [][]trans={{1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman4.setTransition_matrix(transition);
	    k4Test=false;
	/*	kalman4=new JKalman(2,2);
		double [][]error={{100000,0},{0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman4.setError_cov_pre(errorCov);
		double [][] processCov={{1,0},{0,1}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman4.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.00010,0},{0,0.00010}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman4.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman4.setState_pre(meow); 
		double [][]trans={{1,0.1},{0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman4.setTransition_matrix(transition);
	    k4Test=false;*/
		
	}
	void initKalman5() throws Exception
	{
		kalman5=new JKalman(1,1);
		double [][]error={{100000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman5.setError_cov_pre(errorCov);
		double [][] processCov={{.00001}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman5.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.000001}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman5.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman5.setState_pre(meow); 
		double [][]trans={{1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman5.setTransition_matrix(transition);
	    k5Test=false;
	/*	kalman5=new JKalman(2,2);
		double [][]error={{100000,0},{0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman5.setError_cov_pre(errorCov);
		double [][] processCov={{100,0},{0,100}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman5.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.00010,0},{0,0.00010}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman5.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman5.setState_pre(meow); 
		double [][]trans={{1,0.5},{0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman5.setTransition_matrix(transition);
	    k5Test=false;*/
		
	}
	void initKalman6() throws Exception
	{
		kalman6=new JKalman(1,1);
		double [][]error={{100000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman6.setError_cov_pre(errorCov);
		double [][] processCov={{0.00001}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman6.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.00001}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman6.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman6.setState_pre(meow); 
		double [][]trans={{1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman6.setTransition_matrix(transition);
	    k6Test=false;
		/*kalman6=new JKalman(2,2);
		double [][]error={{100000,0},{0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix 
		kalman6.setError_cov_pre(errorCov);
		double [][] processCov={{100,0},{0,100}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman6.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{0.0001,0},{0,0.0001}}; //R 
		Matrix measureCov=new Matrix(measurementError);
		kalman6.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman6.setState_pre(meow); 
		double [][]trans={{1,0.1},{0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman6.setTransition_matrix(transition);
	    k6Test=false;*/
		
	}
	

}


 