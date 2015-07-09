package microlocation.controllers;

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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
public class WemoController2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public WemoController2() {
        super();
       
    }
    Boolean turnOn;
	
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		String onOrOff = request.getParameter("onoroff");
	//	String onOrOff="on";
		if (onOrOff.equals("on"))
				{
			String url = "http://lifeandscience.org/";

	        if(Desktop.isDesktopSupported()){
	            Desktop desktop = Desktop.getDesktop();
	            try {
	                desktop.browse(new URI(url));
	            } catch (IOException | URISyntaxException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }else{
	            Runtime runtime = Runtime.getRuntime();
	            try {
	                runtime.exec("xdg-open " + url);
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
				}
		
		else if(onOrOff.equals("off"))
		{
			
		}
	}

}
