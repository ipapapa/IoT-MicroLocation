package microlocation.controllers;


/**
 *
 * @author Faheem Zafari
 */
public class MathX {
    // used to calculate distance between a point and the land mark, I will modify it
	//to use the RSSI value to calculate the distance between a landmark and the point
    public static double distance(double x1, double y1, double x2, double y2) { 
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
    public static double Gaussian(double mu, double sigma, double x) {       
        return Math.exp(-(Math.pow(mu - x, 2)) / Math.pow(sigma, 2) / 2.0) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2));
    	//return Math.exp(-(1/2)*((Math.pow(x-mu, 2)) / Math.pow(sigma, 2)) ) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2)); // my correction
    }
    
}
