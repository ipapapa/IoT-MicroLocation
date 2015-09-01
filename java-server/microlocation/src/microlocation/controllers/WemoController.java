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

import java.sql.*;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import microlocation.models.BeaconAuthenticator;
public class WemoController extends HttpServlet
{
	
	
	private static final long serialVersionUID = 1L;
	Boolean success = false;
	boolean alarm=true; // to be modified later
	String alarmType ="skill2";  //what is the alarm type
	String alarmBeaconMinor="1"; //what is the minor of the beacon affiliated with patient
	String nurseId; //nurse id that would be used for sending the message 
	String alertType; //what is the alert ty
	String Minor; // again minor value of the beacon
	String proximity; //the value of the proximity either 1 or 0 for the beacon
	boolean alarm1;
	boolean alarm2;
	boolean alarm3;
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	/*{new java.util.Timer().schedule( 
	        new java.util.TimerTask() {
	            @Override
	            public void run() {
	            	alarm1=true;
	            	alarm2=true;
	            	alarm3=true;
	            	System.out.println("alarm 1 inside is "+alarm1);
	        		System.out.println("alarm 2 inside is "+alarm2);
	        		System.out.println("alarm 3  inside is "+alarm3);
	                // your code here
	            }
	        }, 
	        500 
	        
	);
	System.out.println("alarm 1 is "+alarm1);
	System.out.println("alarm 2 is "+alarm2);
	System.out.println("alarm 3 is "+alarm3);
	}*/
	
	public WemoController()
	{
		super();
	}
	
	Boolean turnOn;
	
	protected void doPost (HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{		
		

		
		
	//	String id=request.getParameter("ID");
	//	String minor=request.getParameter("minor");
		String user1=request.getParameter("user");
		//System.out.println("the user is "+user1 );
		alarmType=user1;
		String id="1";
		String minor="1";
		String zero="0";
	//	String proximity=request.getParameter("onoroff");
		String proximity="on";
		boolean yes ;
		String one="1";
		
		if(proximity.equals("on"))
		{
			yes=true;
			if(yes)
			{
				try {
					writeToDb(id, minor,one);
				} catch (ClassNotFoundException | SQLException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		else if (proximity.equals("off"))
		{
			yes=false;
			if(!yes)
			{
				try {
					removeFromDb(id,minor,zero);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
	
	/*	new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	
		            	alarm1=true;
		          //  	alarm2=true;
		          //  	alarm3=true;
		            	System.out.println("alarm 1 inside is "+alarm1);
		            	System.out.println("");
		        	//	System.out.println("alarm 2 inside is "+alarm2);
		        	//	System.out.println("alarm 3  inside is "+alarm3);
		        		if(alarm1)
		        		{
		        			try {
		        				alarmType="skill1";
		        				alarmInfo(alarmType,alarmBeaconMinor);
		        			} catch (SQLException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
		        		}
		                // your code here
		            }
		        }, 
		        5000 
		        
		        
		);System.out.println("alarm 1 outside is "+alarm1);
		System.out.println("");
		
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	
		          //  	alarm1=true;
		            	alarm2=true;
		          //  	alarm3=true;
		            	System.out.println("alarm 2 inside is "+alarm2);
		            	System.out.println("");
		        	//	System.out.println("alarm 2 inside is "+alarm2);
		        	//	System.out.println("alarm 3  inside is "+alarm3);
		        		if(alarm2)
		        		{
		        			try {
		        				alarmType="skill2";
		        				alarmInfo(alarmType,alarmBeaconMinor);
		        			} catch (SQLException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
		        		}
		                // your code here
		            }
		        }, 
		        3000 
		        
		        
		);
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	
		          //  	alarm1=true;
		           // 	alarm2=true;
		            	alarm3=true;
		            	System.out.println("alarm 3 inside is "+alarm3);
		            	System.out.println("");
		        	//	System.out.println("alarm 2 inside is "+alarm2);
		        	//	System.out.println("alarm 3  inside is "+alarm3);
		        		if(alarm3)
		        		{
		        			try {
		        				alarmType="skill3";
		        				alarmInfo(alarmType,alarmBeaconMinor);
		        			} catch (SQLException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
		        		}
		                // your code here
		            }
		        }, 
		        4000 
		    
		);*/
		
		
	//	if(alarm)
	//	{
		
			try {
				alarmInfo(alarmType,alarmBeaconMinor);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//} 
		
		
		
	}
	public void writeToDb(String id, String minor, String yes) throws ClassNotFoundException, SQLException
	{
		connectToSQLDatabase();
		// preparedStatement = connect
		       //   .prepareStatement("update into  microlocation_aws.iowa values (, ?, ?, ?, ? , ?, ?)");
	//	String query="UPDATE microlocation_aws.iowa SET minor="+2+"proximity="+1+" WHERE (id="+1+")";	      
		 String query = "update iowa set minor = ? , proximity=? where id ="+id ;//?";
		 
		 preparedStatement = connect.prepareStatement(query);
		 preparedStatement .setString(1, minor);
		 preparedStatement .setString(2, yes);
		 preparedStatement.executeUpdate();
	      connect.close();
		
	}
	public void removeFromDb(String id, String minor, String no) throws SQLException
	{
		try {
			connectToSQLDatabase();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 String query = "update iowa set minor = ? , proximity=? where id ="+id ;//?";
		 
		 preparedStatement = connect.prepareStatement(query);
		 preparedStatement .setString(1, minor);
		 preparedStatement .setString(2, no);
		 preparedStatement.executeUpdate();
	      connect.close();
	}
	
	
	public void connectToSQLDatabase() throws ClassNotFoundException
	{
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      Class.forName("com.mysql.jdbc.Driver");
		      // Setup the connection with the DB
		      connect = DriverManager
		          	    		.getConnection("jdbc:mysql://127.0.0.1:3306/microlocation_aws", "root", "MDWY!(&&");//"MDWY!(&&");

		   }
		catch (SQLException e) 
    	{
    		System.out.println("Connection Failed! Check output console");
    		e.printStackTrace();
    		//return;
    	}
	}
	
	public void alarmInfo(String a, String b) throws SQLException
	{
		
		String[] response = new String[2];
		try {
			connectToSQLDatabase();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
    	{
     		String sql = "SELECT * FROM microlocation_aws.iowa WHERE (minor = '"+alarmBeaconMinor+"')";
     			//	"' AND major = '"+major+"' AND minor = '"+minor+"')"; //statement for

     		PreparedStatement statement = connect.prepareStatement(sql); 
     		
    		ResultSet result = statement.executeQuery(sql); 
    		
    		while (result.next())
    		{
    			nurseId=result.getString(1);
    			Minor=result.getString(2);
    			proximity=result.getString(3);
    		//	System.out.println("The proximity is "+proximity);
    		//	System.out.println("The minor is "+Minor+" the alarm beacon is "+alarmBeaconMinor);
    		if(Minor.equals(alarmBeaconMinor))	
    		{
    			
    			if(alarmType.equalsIgnoreCase("skill1"))// && (Minor.equalsIgnoreCase(alarmBeaconMinor)))
    			{
    				alertType=result.getString(4);
    			//	System.out.println("The alert type for skill 1 is "+alertType);
    			}
    			else if(alarmType.equalsIgnoreCase("skill2"))//&& (Minor.equalsIgnoreCase(alarmBeaconMinor)))
    			{
    				alertType=result.getString(5);
    			//	System.out.println("The alert type for skill 2 is "+alertType);
    			}
    			else if(alarmType.equalsIgnoreCase("skill3"))//&& (Minor.equalsIgnoreCase(alarmBeaconMinor)))
    			{
    				alertType=result.getString(6);
    			//	System.out.println("The alert type for skill 3 is "+alertType);
    			}
    		}
    		 //   if ((alertType.equalsIgnoreCase("1")))
    		  //  {
    		    if((Minor.equalsIgnoreCase(alarmBeaconMinor))  &&(alertType.equalsIgnoreCase("1"))
    		    		&& (proximity.equalsIgnoreCase("1")))  //if there is data found in the
    		    	//database or if it matches it
    		    {
    		    	success = true; 
    		    	response[0] = nurseId; 
    		    	response[1] = alarmType; 
    		    	System.out.println("Nurse id is "+response[0]+"\n");
    	    		System.out.println("alarm Type is "+ response[1]+"\n");
    		   
    		    }//}
    		    
    		    	
    		}
    		for(String s:response)
    		{
    			if(s==null)
    			{
    				System.out.println("No nurse available"+"\n");
    				break;
    			}
    		}
		//    result.close();
		    
		//    statement.close();
		//    connect.close();
    		
    	
    	}
    	catch (SQLException e) 
    	{
    		e.printStackTrace();
    	}
		
		
	}
	
	
}
	
	

	