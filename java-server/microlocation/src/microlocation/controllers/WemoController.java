package microlocation.controllers;


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

import microlocation.models.BeaconAuthenticator;
public class WemoController extends HttpServlet
{
	
	
	private static final long serialVersionUID = 1L;
	
	public WemoController()
	{
		super();
	}
	
	Boolean turnOn;
	
	protected void doPost (HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{		
		
		String onOrOff = request.getParameter("onoroff");
		String use=request.getParameter("name");

		
		String resource ="";
		String deviceId=request.getParameter("deviceId");
	
		
		if (onOrOff.equals("on"))
		{
			if(!(doesExist(use)))
			{
			try {
				
				httpPost("https://alice.lib.ncsu.edu/api/v1/action/?format=json",use);
			
			} 
			catch (Exception e) 
			{
				
				e.printStackTrace();
			}
			}
		
			
		}
		else if(onOrOff.equals("off"))
		{
			if(doesExist(use))
			{
			resource=getResource(use);
			
				try {
					httpDelete("https://alice.lib.ncsu.edu"+resource);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			
		}}
	
	
	public static void httpDelete(String urlStr) throws Exception
	{
		String name = "faheem";
		String password = "f@h33m";

		String authString = name + ":" + password;

		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		conn.setRequestProperty( "Content-Type", "application/json" );
		conn.setRequestMethod("DELETE");
		conn.connect();
	
		InputStream is = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line, responseText = "";
		while ((line = br.readLine()) != null) {
		    System.out.println("LINE: "+line);
		    responseText += line;
		}
		br.close();
		conn.disconnect();
		
		System.out.println("deleting ");
	}
	public static void httpPost(String urlStr, String use) throws Exception {
			
		String name = "faheem";
		String password = "f@h33m";

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		String content="";
		String input="";
		if(use.equals("artw"))
		{ 
			content="\"https://www.google.com/search?q=ncsu+mascot&espv=2&biw=1011&bih=623&tbm=isch&imgil=D5cnf-Buq6hiLM%253A%253BihaOA2vNKspEEM%253Bhttps%25253A%25252F%25252Fwww.pinterest.com%25252Fsequoiars%25252Fmascot-style%25252F&source=iu&pf=m&fir=D5cnf-Buq6hiLM%253A%252CihaOA2vNKspEEM%252C_&usg=__bo5XSDyjgeRF3mhBV_d02wGlLH8%3D&ved=0CDkQyjc&ei=Nx2cVfulKImggwTy2LXgAQ#imgrc=D5cnf-Buq6hiLM%3A&usg=__bo5XSDyjgeRF3mhBV_d02wGlLH8%3D\"";
			input = "{\"user_id\":\"faheem\",\"device_id\":\""+use+"\",\"request_json\":{\"content\":"+content+",\"hint\":\"fullscreen\"},\"expires\":\"2015-06-25T10:58:58.715000\"}";
	
		
		}
		
		else if(use.equals("immw"))
		{
			content="\"https://www.google.com/search?q=ncsu+mascot&espv=2&biw=1011&bih=623&tbm=isch&imgil=D5cnf-Buq6hiLM%253A%253BihaOA2vNKspEEM%253Bhttps%25253A%25252F%25252Fwww.pinterest.com%25252Fsequoiars%25252Fmascot-style%25252F&source=iu&pf=m&fir=D5cnf-Buq6hiLM%253A%252CihaOA2vNKspEEM%252C_&usg=__bo5XSDyjgeRF3mhBV_d02wGlLH8%3D&ved=0CDkQyjc&ei=Nx2cVfulKImggwTy2LXgAQ#imgrc=D5cnf-Buq6hiLM%3A&usg=__bo5XSDyjgeRF3mhBV_d02wGlLH8%3D\"";
			input = "{\"user_id\":\"faheem\",\"device_id\":\""+use+"\",\"request_json\":{\"content\":"+content+",\"hint\":\"fullscreen\"},\"expires\":\"2015-06-25T10:58:58.715000\"}";
		}
		
		else if(use.equals("vizw"))
		{ 
			content="\"http://www.ces.ncsu.edu/plymouth/ent/pics/ncsu_logo_1.gif\"";
			input = "{\"user_id\":\"faheem\",\"device_id\":\""+use+"\",\"request_json\":{\"content\":"+content+",\"hint\":\"fullscreen\"},\"expires\":\"2015-06-25T10:58:58.715000\"}";
		}
		
		else if(use.equals("cmnw"))
			
		{ 
			content="\"http://www.ces.ncsu.edu/plymouth/ent/pics/ncsu_logo_1.gif\"";
			input = "{\"user_id\":\"faheem\",\"device_id\":\""+use+"\",\"request_json\":{\"content\":"+content+",\"hint\":\"fullscreen\"},\"expires\":\"2015-06-25T10:58:58.715000\"}";
		}
		else if(use.equals("gmlb"))
		{
			content="\"https://alice.lib.ncsu.edu/content/?usr=bob;msg=this is my message\"";
			input = "{\"user_id\":\"faheem\",\"device_id\":\""+use+"\",\"request_json\":{\"content\":"+content+",\"hint\":\"fullscreen\"},\"expires\":\"2015-06-25T10:58:58.715000\"}";
		}
		
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {

			System.out.println(output);
		}
		
		conn.disconnect();
		
	}
	public static String httpGet(String urlStr) throws IOException {
		String name = "faheem";
		String password = "f@h33m";

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Basic " + authStringEnc); 
	

		  if (conn.getResponseCode() != 200) {
		    throw new IOException(conn.getResponseMessage());
		  }

		  // Buffer the result into a string
		  BufferedReader rd = new BufferedReader(
		      new InputStreamReader(conn.getInputStream()));
		  StringBuilder sb = new StringBuilder();
		  String line;
		  while ((line = rd.readLine()) != null) {
		    sb.append(line);
		  }
		  rd.close();

		  conn.disconnect();
		  return sb.toString();
		}
	
	boolean doesExist(String useCase) throws IOException
	{
		JSONObject json;
		String device_id;
		boolean test=false;
		try {
			JSONParser parser=new JSONParser();
			json =(JSONObject)parser.parse(httpGet("https://alice.lib.ncsu.edu/api/v1/action/?format=json"));
			
		
			
			JSONArray aray=(JSONArray)json.get("objects");
			for(int j=0;j<aray.size();j++)
			{
				
			Iterator i=aray.iterator();
			 while (i.hasNext()) {
				 
				                  JSONObject innerObj = (JSONObject) i.next();
				 
				              //   System.out.println(innerObj.get("resource_uri"));
				                  device_id=(String) innerObj.get("device_id");
				               //   System.out.println(device_id);
				                  if(device_id.equals(useCase))
				                  {
				                	  test=true;
				                	  break;
				                  }
				                  else
				                  {
				                	 test=false;
				                  }
				             //     t++;
				             }
			}

		} 
			catch (ParseException e) {
		
			e.printStackTrace();
		}
		System.out.println(test);
		return test;
	}
	String getResource(String use) throws IOException
	
	{
		String resource="meow";
		JSONObject json;
		String device_id;
	
		try {
			JSONParser parser=new JSONParser();
			json =(JSONObject)parser.parse(httpGet("https://alice.lib.ncsu.edu/api/v1/action/?format=json"));
			
		
			
			JSONArray aray=(JSONArray)json.get("objects");
			for(int j=0;j<aray.size();j++)
			{
				
			
			
			Iterator i=aray.iterator();
			 while (i.hasNext()) {
				 
				                  JSONObject innerObj = (JSONObject) i.next();
				 
				            
				                  device_id=(String) innerObj.get("device_id");
				              //    System.out.println(device_id);
				                  if(device_id.equals(use))
				                  {
				                	  resource=(String) innerObj.get("resource_uri");
				                	break;
				                  }
				                  else
				                  {
				                	 resource="does not exist";
				                
				                  }
				           
				             }
			}

		} 
			catch (ParseException e) {
			
			e.printStackTrace();
		}
		return resource;

	}
}
	
	
	
	
	
