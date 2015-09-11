package microlocation.controllers;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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
	private PreparedStatement preparedStatement = null;
	
	
	
	public WemoController()
	{
		super();
	}
	
	Boolean turnOn;
	
	private final String  TABLE_NAME = "beacons";
	
	protected void doPost (HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{		
		

		
		
	//	String id=request.getParameter("ID");
	//	String minor=request.getParameter("minor");
		String user1=request.getParameter("user");
		alarmType=user1;
		
		String id="1";
		String minor="1";
		String minorAlarm=request.getParameter("minorAlarm");
	//	System.out.println("The minor Alarm is "+minorAlarm);
		String zero="0";
		String proximity=request.getParameter("onoroff");
		
		boolean yes ;
		String one="1";
	/*	try {
			alarmInfo(alarmType,alarmBeaconMinor);
		} catch (SQLException e) {
		
			e.printStackTrace();
		}*/
		
		//if(proximity.equals("on"))
		if("on".equals(proximity))
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
		
	//	else if (proximity.equals("off"))
		 if("off".equals(proximity))
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
	
	
		
	//	if(alarm)
	//	{
		
			try {
				alarmInfo(alarmType,minorAlarm);
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
		//} 
		
		
		
	}
	public void writeToDb(String id, String minor, String yes) throws ClassNotFoundException, SQLException, IOException
	{
		connectToSQLDatabase();
		
		 String query = "update " + TABLE_NAME + " set minor = ? , proximity=? where id ="+id ;//?";
		 
		 preparedStatement = connect.prepareStatement(query);
		 preparedStatement .setString(1, minor);
		 preparedStatement .setString(2, yes);
		 preparedStatement.executeUpdate();
	      connect.close();
		
	}
	public void removeFromDb(String id, String minor, String no) throws SQLException, IOException
	{
		try {
			connectToSQLDatabase();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		 String query = "update " + TABLE_NAME + " set minor = ? , proximity=? where id ="+id ;//?";
		 
		 preparedStatement = connect.prepareStatement(query);
		 preparedStatement .setString(1, minor);
		 preparedStatement .setString(2, no);
		 preparedStatement.executeUpdate();
	      connect.close();
	}
	
	
	public void connectToSQLDatabase() throws ClassNotFoundException, IOException
	{
		String user = "meow", password="meow";
		try {
		     
		      Class.forName("com.mysql.jdbc.Driver");
		      InputStream inputStream ; 
		      try {
					Properties prop = new Properties();
					String propFileName = "log.properties";
		 
					inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		 
					if (inputStream != null) {
						prop.load(inputStream);
					} else {
						throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
					}
		 
					Date time = new Date(System.currentTimeMillis());
		 
					// get the property value and print it out
					 user = prop.getProperty("jdbc.username");
					 password = prop.getProperty("jdbc.password");
				//	String company2 = prop.getProperty("company2");
				//	String company3 = prop.getProperty("company3");
		 
			//	System.out.println("The user id and password is "+user+" "+password);
				} catch (Exception e) {
					System.out.println("Exception: " + e);
				} 
		      
		      
		      Context initContext = new InitialContext();
	    		Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    		DataSource ds = (DataSource)envContext.lookup("jdbc/microlocation");
	    		
		      
		     
		      connect = ds.getConnection(); 
		    		  
		    		  // DriverManager
		          	    	//	.getConnection("jdbc:mysql://127.0.0.1:3306/microlocation_aws", user,password);//"root", "MDWY!(&&");//"MDWY!(&&");

		   }
		catch (SQLException e) 
    	{
    		System.out.println("Connection Failed! Check output console");
    		e.printStackTrace();
    	
    	} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void alarmInfo(String a, String b) throws SQLException, IOException
	{
		
		String[] response = new String[2];
		try {
			connectToSQLDatabase();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		try
    	{
     		//String sql = "SELECT * FROM microlocation_aws.iowa WHERE (minor = '"+b+"')";
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE (minor = '"+b+"')";
     			
     		PreparedStatement statement = connect.prepareStatement(sql); 
     		
    		ResultSet result = statement.executeQuery(sql); 
    		
    		while (result.next())
    		{
    			nurseId=result.getString(1);
    			Minor=result.getString(2);
    			proximity=result.getString(3);
    	
    		if(Minor.equals(b))	
    		{
    			
    		//	if(alarmType.equalsIgnoreCase("skill1"))// && (Minor.equalsIgnoreCase(alarmBeaconMinor)))
    			if("skill1".equals(alarmType))
    			{
    				alertType=result.getString(4);
    			//	System.out.println("The alert type for skill 1 is "+alertType);
    			}
    		//	else if(alarmType.equalsIgnoreCase("skill2"))//&& (Minor.equalsIgnoreCase(alarmBeaconMinor)))
    			else if("skill2".equals(alarmType))
    			{
    				alertType=result.getString(5);
    			
    			}
    			//else if(alarmType.equalsIgnoreCase("skill3"))//&& (Minor.equalsIgnoreCase(alarmBeaconMinor)))
    			else if("skill2".equals(alarmType))
    			{
    				alertType=result.getString(6);
    			
    			}
    		}
    		 
    		    if((Minor.equalsIgnoreCase(b))  &&(alertType.equalsIgnoreCase("1"))
    		    		&& (proximity.equalsIgnoreCase("1")))  
    		    {
    		    	success = true; 
    		    	response[0] = nurseId; 
    		    	//response[1] = alarmType; 
    		    	response[1]=a;
    		    	System.out.println("Nurse id is "+response[0]+"\n");
    		    	System.out.println("The beacon minor is "+b);
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
	
	

	