/*
 * Beacon Controller is the class that is access by the iOS application
 * Basically the UUID is passed by the application to the beacon controller java class
 * This class is a subclass of HttpServlet
 */
package microlocation.controllers;
import microlocation.models.BeaconAuthenticator;
import microlocation.models.Beacon;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BeaconController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public BeaconController() // The constructor declared
	{
		super(); //calling the constructor of its superclass HttpServlet
	}
	
/*protected void doPost(HttpServletRequest req,
          HttpServletResponse resp)
               throws ServletException,
                      IOException
Called by the server (via the service method) to allow a servlet to handle a POST request.
 The HTTP POST method allows the client to send data of unlimited length to the Web server
  a single time and is useful when posting information such as credit card numbers.
When overriding this method, read the request data, write the response headers, get 
the response's writer or output stream object, and finally, write the response data. 
It's best to include content type and encoding. When using a PrintWriter object to 
return the response, set the content type before accessing the PrintWriter object.

The servlet container must write the headers before committing the response, because 
in HTTP the headers must be sent before the response body.
Where possible, set the Content-Length header 
(with the ServletResponse.setContentLength(int) method), to allow the servlet container
 to use a persistent connection to return its response to the client, improving performance. The content length is automatically set if the entire response fits inside the response buffer.

When using HTTP 1.1 chunked encoding (which means that the response has a Transfer-Encoding
 header), do not set the Content-Length header.

This method does not need to be either safe or idempotent. Operations requested through 
POST can have side effects for which the user can be held accountable, for example, 
updating stored data or buying items online.

If the HTTP POST request is incorrectly formatted, doPost returns an HTTP "Bad Request" 
message.

Parameters:
req - an HttpServletRequest object that contains the request the client has made of the 
servlet (the client has asked for beacon service information in our case)
resp - an HttpServletResponse object that contains the response the servlet sends to the
 client  (the response from database )
Throws:
IOException - if an input or output error is detected when the servlet handles the request
ServletException - if the request for the POST could not be handled
See Also:
ServletOutputStream, ServletResponse.setContentType(java.lang.String)
 
 */
	
	protected void doPost (HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
			// The function takes request and response as inputs 
			// 
	{	
		//String[] results = new String[5];
	/*	for(int i=0;i<results.length;i++)
		{
			results[i]="123";
		}*/
		//String uuid = request.getParameter("uuid"); //getParameter returns the value of 
		//request parameter as a string. So the client request contained the UUID, that is
		//returned here. 
	//	System.out.println(uuid);
	//	String uuid=uuid1.substring(30, 66);
	//	System.out.println(uuid);
		//String major = request.getParameter("major");
	
		//String minor = request.getParameter("minor");
		//System.out.println(uuid+" "+major+" "+minor);
	//	System.out.println(major+" "+minor);
		//RequestDispatcher rd = null; //RequestDispatcher defines an object that receives 
		//requests from the client (mobile phone)  and sends them to any resource i.e. a JSP
		//file on server. 
		
		//BeaconAuthenticator beaconAuthenticator = new BeaconAuthenticator();
		// for authenticating the beacon information. So the beaconauthenticator
		//object connects to the Database and confirms this information obtained
		//from the phone
		
		//--results = beaconAuthenticator.getNewRoomsIBeacons();
	// information passed to the authenticate beacon method of beacon Authenticator
		
     //  System.out.println(results[0]+ " "+ results[1]+ " "+ results[2]+ " " );
		//if(results[0]!=null)
	//	{
       //if ( results[0].equals(uuid) && results[1].equals(major) && results[2].equals(minor))
		//if the information obtained from phone matches the database,  then 
			
		//{ // System.out.println("inside");
			//rd = request.getRequestDispatcher("/beacon_success.jsp");
			//getRequestDispatcher returns a RD object that acts as a wrapper for the 
			//resource located at the given path
			//Beacon beacon = new Beacon (results[0],results[1],results[2],results[3],results[4]);
		//	request.setAttribute("uuid", "1"); //set the attribute in this request i.e. uuid should mean results[0]
		//	request.setAttribute("major", "2");// name of the attribute "string"
			//object to be stored is results[1]. This is done as the attributes are reset
			//between requests. 
		//	request.setAttribute("minor", "3");
		//	request.setAttribute("name", "4");
		//	request.setAttribute("usecase","5");
		//	rd = request.getRequestDispatcher("/beacon_success.jsp");
		//}
		//else 
		//{
        //    rd = request.getRequestDispatcher("/beacon_error.jsp");
        //}
		//}
        //rd.forward(request, response); //
        //forwards a request from a servlet to another source (serlvet, jsp file, or HTML file)
        //request is the object the represents the request the client makes to the servlet
        //reponse is the object that represents the response that the servlet returns to client
	System.out.println("BeaconController Called");
	
	}
}
