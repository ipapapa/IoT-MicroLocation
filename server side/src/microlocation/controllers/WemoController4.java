package microlocation.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

//import sun.tools.jar.CommandLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
/**
 * Servlet implementation class WemoController4
 */
public class WemoController4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	JKalman kalman1;
	 boolean k1Test=true;
    public WemoController4() {
        super();
  
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(k1Test)
		 {
				try {
					initKalman1();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		
		String identity=request.getParameter("landmark");
		String meters=request.getParameter("meters");
		double convert1=Double.parseDouble(meters);
		System.out.println("The rssi value is "+meters);
		double e1 = Double.parseDouble(meters);
      
  		    double re1 = 0.0003351 * Math.exp(0.1103220*-e1);
    
  		    re1 *= 1.75;
		 double content=calculateAccuracy(-47, e1);;
		 File file1= new File("/Users/FahimZafari/Desktop/nofilter.txt");
		 if(!file1.exists())
		 {
			 file1.createNewFile();
		 }
		 FileWriter fw = new FileWriter(file1.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content+"\n");
			bw.close();
		
		double [] [] test={{convert1},{0}}; //measurement obtained  i.e. z 
		
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
		 
			System.out.println("The RSSI value after kf is "+number);
			double e = Double.parseDouble(number);
	        
	  //		    double re = 0.0003351 * Math.exp(0.1103220*-e);
	     
	  	//	    re *= 1.75;
	  	
	
  		double d=  ((e+50.0)/(-18.4432));
  		 double re= Math.pow(10,d);
      
       double dist=re;
       System.out.println("The distance is "+dist);
       double f=calculateAccuracy(-47, e);
       System.out.println("Stack exchange formula is "+f);
 	  double content2=f;
 		 File file2= new File("/Users/FahimZafari/Desktop/filter.txt");
 		 if(!file2.exists())
 		 {
 			 file2.createNewFile();
 		 }
 		 FileWriter fw2 = new FileWriter(file2.getAbsoluteFile(),true);
 			BufferedWriter bw2 = new BufferedWriter(fw2);
 			bw2.write(content2+"\n");
 			bw2.close();
  
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
		double [][]error={{100000,0},{0,10000}};
		Matrix errorCov=new Matrix(error); // P matrix  
		kalman1.setError_cov_pre(errorCov);
		double [][] processCov={{0.0001,0},{0,0.0001}};// the increase in value of q makes the system rely more on measured value 
		Matrix processCovEr=new Matrix(processCov);  //Q
		kalman1.setProcess_noise_cov(processCovEr); //Q
		double [][]measurementError={{1.00010,0},{0,1.00010}}; //R 
	//	double [][]measurementError={{1.00010},{0}};
		Matrix measureCov=new Matrix(measurementError);
		kalman1.setMeasurement_noise_cov(measureCov); //R
		double [][] hello={{40,0}}; //initial value
		Matrix meow=new Matrix(hello); //initial value matrix 
		kalman1.setState_pre(meow); 
		double [][]trans={{1,0.01},{0,1}};
		Matrix transition=new Matrix(trans); // F matrix
	    kalman1.setTransition_matrix(transition);
	    k1Test=false;

	}
	protected static double calculateAccuracy(int txPower, double rssi) {
		  if (rssi == 0) {
		    return -1.0; // if we cannot determine accuracy, return -1.
		  }

		  double ratio = rssi*1.0/txPower;
		  if (ratio < 1.0) {
		    return Math.pow(ratio,10);
		  }
		  else {
		    double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;    
		    return accuracy;
		  }
		}

}
