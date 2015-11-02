package microlocation.models;

import java.sql.*;

public class BeaconAuthenticator 
{
	String p_uuid = "";
	String p_major = "";
	String p_minor = "";
	String name = "";
	String usecase = "";
	
	Boolean success = false;
	Connection connection = null;
	
	public void connectToSQLDatabase()
	{
		try 
    	{
    		Class.forName("com.mysql.jdbc.Driver");
    	} 
    	catch (ClassNotFoundException e) 
    	{
    		//System.out.println("Where is your MySQL JDBC Driver?");
    		e.printStackTrace();
    		//return;
    	}
    	
    	//System.out.println("MySQL JDBC Driver Registered!");
     
    	try 
    	{
    		connection = DriverManager
    		.getConnection("jdbc:mysql://localhost:3306/microlocation_aws", "root", "");
    	} 
    	catch (SQLException e) 
    	{
    		System.out.println("Connection Failed! Check output console");
    		e.printStackTrace();
    		//return;
    	}
    	
    	if (connection != null) 
    	{
    		//System.out.println("You made it, take control of your database now!");
    	} 
    	else 
    	{
    		System.out.println("Failed to make connection!");
    	}
	}
	
	public String[] authenticateBeacon(String uuid, String major, String minor)
	{
		String[] response = new String[5];
		
		connectToSQLDatabase();
    	
    	try
    	{
     		String sql = "SELECT * FROM beacons WHERE (uuid = '"+uuid+"' AND major = '"+major+"' AND minor = '"+minor+"')";
     		PreparedStatement statement = connection.prepareStatement(sql);
    		ResultSet result = statement.executeQuery(sql);
    		while (result.next())
    		{
    			p_uuid = result.getString(1);
    		    p_major = result.getString(2);
    		    p_minor = result.getString(3);
    		    name = result.getString(4);
    		    usecase = result.getString(5);
    		    
//    		    String newString1 = uuid + " " + result.getString(1);
//    		    System.out.println(newString1);
    		    
    		    if((p_uuid.equalsIgnoreCase(uuid)) && (p_major.equalsIgnoreCase(major)) 
    		    		&& (p_minor.equalsIgnoreCase(minor)))
    		    {
    		    	success = true;
    		    	response[0] = p_uuid;
    		    	response[1] = p_major;
    		    	response[2] = p_minor;
    		    	response[3] = name;
    		    	response[4] = usecase;
    		    }
    		}
		    result.close();
		    statement.close();
		    connection.close();
    	}
    	catch (SQLException e) 
    	{
    		e.printStackTrace();
    	}
		return response;
	}
}
