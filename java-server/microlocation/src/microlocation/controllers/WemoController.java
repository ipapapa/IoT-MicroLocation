package microlocation.controllers;


import java.io.IOException;

import sun.tools.jar.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		if (onOrOff.equals("on"))
		{
			turnOn = true;
		}
		else
		{
			turnOn = false;
		}
		
		try
		{
			System.out.println("Execute Wemo Script");
			executeWemoScript();
			
		}
		catch (InterruptedException ex)
		{
			
		}
	}
	
	public static String convertStreamToStr(InputStream is) throws IOException 
	{
		 
	if (is != null) 
	{
		Writer writer = new StringWriter();
	 
		char[] buffer = new char[1024];
		try 
		{
			Reader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) 
			{
				writer.write(buffer, 0, n);
			}
		} 
		finally 
		{
			is.close();
		}
		return writer.toString();
	}
	else 
	{
		return "";
	}
	 
}
	
	public String executeWemoScript() throws IOException, InterruptedException 
	{
		String command = "";
		if (turnOn.equals(true))
		{
			command = "/usr/share/tomcat8/webapps/microlocation/Scripts/wemo.sh 128.210.137.31 on";
		}
		else if (turnOn.equals(false))
		{
			command = "/usr/share/tomcat8/webapps/microlocation/Scripts/wemo.sh 128.210.137.31 off";
		}
			
		Boolean waitForResponse = true;
		String response = "";
		 
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		pb.redirectErrorStream(true);
		 
		System.out.println("Unix command: " + command);
		 
		try 
		{
			Process shell = pb.start();
		 
			if (waitForResponse) 
			{
				// To capture output from the shell
				InputStream shellIn = shell.getInputStream();
		 
				// Wait for the shell to finish and get the return code
				int shellExitStatus = shell.waitFor();
				System.out.println("Exit status" + shellExitStatus);
		 
				response = convertStreamToStr(shellIn);
		 
				shellIn.close();
			} 
		}
		catch (IOException e) 
		{
			System.out.println("Error occured while executing Unix command. Error Description: "
					+ e.getMessage());
		}
		 
		catch (InterruptedException e) 
		{
			System.out.println("Error occured while executing Unix command. Error Description: "
					+ e.getMessage());
		}
		 
		return response;
	}
		 
		/*
		* To convert the InputStream to String we use the Reader.read(char[]
		* buffer) method. We iterate until the Reader return -1 which means
		* there's no more data to read. We use the StringWriter class to
		* produce the string.
		*/
}
