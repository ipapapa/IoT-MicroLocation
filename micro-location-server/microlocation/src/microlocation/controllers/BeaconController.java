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
	
	public BeaconController() 
	{
		super();
	}
	
	protected void doPost (HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{	
		String[] results = new String[5];
		String nsuuid = request.getParameter("uuid");
		String uuid = nsuuid.substring(31,67);
		String major = request.getParameter("major");
		String minor = request.getParameter("minor");
		
		RequestDispatcher rd = null;
		
		BeaconAuthenticator beaconAuthenticator = new BeaconAuthenticator();
		results = beaconAuthenticator.authenticateBeacon(uuid, major, minor);
	
//		System.out.println(results[4]);
//		
		if (results[0].equals(uuid) && results[1].equals(major) && results[2].equals(minor))
		{
			rd = request.getRequestDispatcher("/beacon_success.jsp");
			//Beacon beacon = new Beacon (results[0],results[1],results[2],results[3],results[4]);
			request.setAttribute("uuid", results[0]);
			request.setAttribute("major", results[1]);
			request.setAttribute("minor", results[2]);
			request.setAttribute("name", results[3]);
			request.setAttribute("usecase",results[4]);
		}
		else 
		{
            rd = request.getRequestDispatcher("/beacon_error.jsp");
        }
        rd.forward(request, response);
	}
}
