package microlocation.controllers;

import java.io.IOException;  

import sun.tools.jar.CommandLine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import microlocation.models.BeaconAuthenticator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WemoListener implements ServletContextListener{

	
	//private MyThread t = null;
	private ExecutorService ex1;
	private ExecutorService ex2;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("I am starting");
		ex1 = Executors.newSingleThreadExecutor();
		ex2 = Executors.newSingleThreadExecutor();
		ex1.submit(new MyThread());
		ex2.submit(new MyThread1());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("I am ending");
	}
	

}


class MyThread implements Runnable{
	
	public void run(){
		Random rand = new Random();		
		while(true){
	//		int value = rand.nextInt(1000)%3+1;
int value=1;
String minorAlarm="1";
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	try {
			System.out.println("Thread 1");
			sendPost("skill"+value,minorAlarm);
		} 
	
	catch (Exception e) 
	{
	
		e.printStackTrace();
	}
	

		}
	}
	private void sendPost(String alarm, String minorAlarm) throws Exception {

		//String url = "http://localhost:8080/microlocation/WemoController";
		
		String url = "http://www-dev.icts.uiowa.edu/microlocation/WemoController";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "user="+alarm+"&minorAlarm="+minorAlarm;
		//String urlMinor="minorAlarm="+minorAlarm;
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		//wr.writeBytes(urlMinor);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
	//	System.out.println("\nSending 'POST' request to URL : " + url);
	//	System.out.println("Post parameters : " + urlParameters);
	//	System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
//		System.out.println(response.toString());

	}}
class MyThread1 implements Runnable{
	
	public void run(){
		Random rand = new Random();		
		while(true){
	//		int value = rand.nextInt(1000)%3+1;
int value=2;
String minorAlarm="2";
try {
	Thread.sleep(10000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

try {
	System.out.println("Thread 2");
	sendPost1("skill"+value,minorAlarm);
} 

catch (Exception e) 
{

e.printStackTrace();
}


}
	}
	private void sendPost1(String alarm,String minorAlarm) throws Exception {

		//String url = "http://localhost:8080/microlocation/WemoController";
		String url = "http://www-dev.icts.uiowa.edu/microlocation/WemoController";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "user="+alarm+"&minorAlarm="+minorAlarm;
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
	//	System.out.println("\nSending 'POST' request to URL : " + url);
	//	System.out.println("Post parameters : " + urlParameters);
	//	System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
//		System.out.println(response.toString());

	}}
	

	
	
	

	

	
	
