package microlocation.controllers;

import java.util.Random;
import java.util.Scanner;

import microlocation.controllers.MathX;
import microlocation.controllers.Point;
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
/**
 *
 * @author Faheem Zafari
 */
public class Particle {
    
    public float forwardNoise, turnNoise, senseNoise;
    public double x, y, orientation;
    public double worldWidth;
    public double worldHeight;
    public double probability = 0;
    public Point[] landmarks;
    Random random;
	
	
    
    /**
     * Default constructor for a particle
     * 
     * @param landmarks Point array of landmark points for the particle
     * @param width width of the particle's world in pixels
     * @param height height of the particle's world in pixels
     */
    public Particle(Point[] landmarks,double width, double height) {
        this.landmarks = landmarks;
        this.worldWidth = width;
        this.worldHeight = height;
        random = new Random();
        x = random.nextFloat() * width; // randomly distribute the particles
        y = random.nextFloat() * height; // randomly distribute the particles
        orientation = random.nextFloat() * 2f * ((float)Math.PI);
        forwardNoise = 0f;
        turnNoise = 0f;
        senseNoise = 0f;        
    }
    
    /**
     * Sets the position of the particle and its relative probability
     * 
     * @param x new x position of the particle
     * @param y new y position of the particle
     * @param orientation new orientation of the particle, in radians
     * @param prob new probability of the particle between 0 and 1
     * @throws Exception 
     */
    public void set(double x, double y, double orientation, double prob) throws Exception {
        if(x < 0 || x >= worldWidth) {
            throw new Exception("X coordinate out of bounds");
        }
        if(y < 0 || y >= worldHeight) {
            throw new Exception("Y coordinate out of bounds");
        }
        if(orientation < 0 || orientation >= 2 * Math.PI) {
            throw new Exception("X coordinate out of bounds");
        }
        this.x = x;
       // System.out.println(x);
        this.y = y;
      //  System.out.println("y "+y);
        this.orientation = orientation;
        this.probability = prob;
    }
    
    /**
     * Sets the noise of the particles measurements and movements
     * 
     * @param Fnoise noise of particle in forward movement
     * @param Tnoise noise of particle in turning
     * @param Snoise noise of particle in sensing position
     */
    public void setNoise(float Fnoise, float Tnoise, float Snoise) {
        this.forwardNoise = Fnoise;
        this.turnNoise = Tnoise;
        this.senseNoise = Snoise;
    }
    
    /**
     * Senses the distance of the particle to each of its landmarks
     * 
     * @return a float array of distances to landmarks
     */
   // public float[] sense(String[] result)
    public double[] sense(iBeacon[] result)
    {
       // float[] ret = new float[landmarks.length];
    	double[] ret = new double[landmarks.length];
        //this is where we should use the RSSI values and convert them to distance
        // we can get the RSSI values from the beacons and calculate the distance
        //indeed we should first use RSSI values to estimate position of the robot
        //and use that only
    	double e = 0;
    	double dist = 0;
        for(int i=0;i<landmarks.length;i++){
        	 e = Double.parseDouble(result[i].rssi);
  		 //   double re = 0.0003351 * Math.exp(0.1103220*-e);
  		  //   	    re *= 1.73;
       // 	double dist=re;
  	//	float d= (float) ((e+61.0)/(-26.4432));
  			//	float d= (float) ((e+55.0)/(-26.4432)); //this is the one
  	//	float d= (float) ((e+61.0)/(-30.4432));
  	//	    float re=(float) Math.pow(10,d); //ths is the one
       //  Scanner in=new Scanner(System.in);
        ///  System.out.println("Enter distance");
         // float dist=in.nextFloat();
       	
        	// if(result[i].name == "3")
       System.out.println("iBeacon#=" + result[i].name + " , " + e);
        		 dist = distance(e);
        		 System.out.println("iBeacon#=" + result[i].name + " , " +dist);
        /*	 else if(result[i].name == "1")
        		 dist = distance(e);
        	 else if(result[i].name == "4")
        		 dist = distance(e);
        	 else if(result[i].name == "6")
        		 dist = distance(e);
        	 else dist = distance(e);
       //if(result[i].name == "6")*/
       //System.out.println(result[i].name+"--The distance is-- "+dist );
       
        	//  float dist = (float) MathX.distance(x, y, landmarks[i].x, landmarks[i].y);
            ret[0] = dist+ random.nextGaussian() * senseNoise;
       //     System.out.println(ret[i]);
        }      
        
        return ret;
    }    
    protected double distance(double x)
    	{
    		
    		//return (119.6*Math.sin(0.3226*x+3.321)+78.68*Math.sin(0.5154*x-0.4908)+21.69*Math.sin(0.8296*x+1.272)+6.59*Math.sin(1.289*x+1.768));
//    	return (-29.19-11.409*x+1.1864*Math.pow(x,2)-0.039053*Math.pow(x,3));
    	//return (0.605*(-.064893*(1-Math.exp(-x/13.928))));	
//    		return (0.60*(105.6*Math.sin(0.04935*x-2.262)+103.8*Math.sin(0.05135*x-5.348)));
    	//	return ((12.43*Math.exp(-((x+2.618)/2.21)*(x+2.618)/2.21)));
    	//	return (0.605*(8.583+1.159*Math.cos(x*.04715)+8.2*Math.sin(x*0.04715)));
    		//return (0.605*(0.006099*Math.pow(x,3)+0.5318*Math.pow(x,2)+15.1*Math.pow(x,1)+136)/(x+24.84));
    		//return 6.848*Math.pow(10,-6)*Math.exp(-0.1602*x);
    		/*if(x > -49 ){
    			//return 0.5*(x+49.2857)/(-10.0779);
    			return -0.0496*x - 2.4452;
    		}
    		if((x < -49)&&(x > -59 ))
    			return -0.0848*x - 4.5331;
    		if((x<-59)&&(x > ))*/
    		//return 3.119*Math.exp( -1*Math.pow(((x-(-1.387))/(1.698)),2));
    		//return 1.693 -0.003841*Math.cos(x*.09297)-1.716*Math.sin(x*0.09297);
    		 //double p1 = 0.157;
    		 //double p2 = 20.3;
    		 //double p3 = 626.2;
    		 //double p4 =-484.00;
    		 //double p5 = 30.4;
    		 //double q1 = 272.8;
    		 //double q2 = 1.492*Math.pow(10, 4);
    		 //double q3 = -412.4;
    		//double a1 = 12.55;
    		//double a2 = 1.615 ;
    		//double b1 = 0.01783;
    		//double b2 = 0.5013;
    		//double c1 = -8.318;
    		//double c2 = -2.337;
    		//double answer =  a1*Math.sin(b1*x+c1) + a2*Math.sin(b2*x+c2);
    		
    		//double answer = (p1*Math.pow(x, 4) + p2*Math.pow(x, 3) + p3*Math.pow(x,2) + p4*x + p5) /
    	      //         (Math.pow(x,3) + q1*Math.pow(x,2) + q2*x + q3);
    	return -4.587*Math.pow(10, -5)*Math.pow(x, 3) + -0.005281*Math.pow(x,2) + -0.1894*x+-1.982;
    		//if(x>-63)
    		//	answer = 0;
    		//return 1.781*Math.pow(10, -6)*Math.exp(-.2012*x);
    		//return 0.003583*Math.exp(-0.08394*x) + 5.291*Math.pow(10, -18)*Math.exp(-0.5396*x);
    		//return answer;
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
		   // double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;    
			  double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;  
		    return accuracy;
		  }
		}
    /**
     * Moves the particle's position
     * 
     * @param turn turn value, in degrees
     * @param forward move value, must be >= 0
     */
   /* public void move(float turn, float forward) throws Exception {
        if(forward < 0) {
            throw new Exception("Robot cannot move backwards");
        }
        //in our case movement will b on its own
        orientation = orientation + turn + (float)random.nextGaussian() * turnNoise;
        orientation = circle(orientation, 2f * (float)Math.PI);
        
        double dist = forward + random.nextGaussian() * forwardNoise;
        
        x += Math.cos(orientation) * dist;
        y += Math.sin(orientation) * dist;
        x = circle(x, worldWidth);
        y = circle(y, worldHeight); 
    }*/
    
    /**
     * Calculates the probability of particle based on another particle's sense()
     * 
     * @param measurement distance measurements from another particle's sense()
     * @return the probability of the particle being correct, between 0 and 1
     */
   // public double measurementProb(float[] measurement)
    public double measurementProb(double[] measurement)
    {
        double prob = 1.0;
        for(int i=0;i<landmarks.length;i++) {
           float dist = (float) MathX.distance(x, y, landmarks[i].x, landmarks[i].y);     
        
            prob *= MathX.Gaussian(dist, senseNoise, measurement[i]);            
        }      
        
        probability = prob;
        
        return prob;
    }

    @SuppressWarnings("unused")
	private float circle(float num, float length) {         
        while(num > length - 1) num -= length;
        while(num < 0) num += length;
        return num;       
    }
    
    @Override
    public String toString() {
    //    return "[x=" + x + " y=" + y + " orient=" + Math.toDegrees(orientation) + " prob=" +probability +  "]";
    return "["+ x+" "+y+" ]";
    }
    
}