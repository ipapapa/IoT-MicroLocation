/*
 * Five different data members of type string.
 * A Boolean data type success initialized to be false
 * Connection data type declared to be null
 * There is a function used to connect to the SQL database (check function for comments)
 * The authenticateBeacon connects to SQL database to verify the UUID, major and minor value
 * along with the name and usecase of the beacons. Then the values after verification
 * are stored in the response array and returned back
 */
package microlocation.models;

import java.io.IOException;
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
	
	/*
	 * Connect with SQL Database
	 */
	public void connectToSQLDatabase() 
	{
		// JDBC driver name and database URL
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		String DB_URL = "jdbc:mysql://127.0.0.1:3306/microlocation_aws";
		   
		try 
    	{
    		Class.forName(JDBC_DRIVER);
    	} 
    	catch (ClassNotFoundException e) 
    	{
    		e.printStackTrace();
    	}
    	
    	System.out.println("JDBC Driver Registered!");
     
    	try 
    	{
    		connection = DriverManager.getConnection(DB_URL, "root", "");
    	} 
    	catch (SQLException e) 
    	{
    		System.out.println("Connection Failed! Check output console");
    		e.printStackTrace();
    	}
    	
    	if (connection == null) 
    	{
    		System.out.println("Failed to make connection!");
    	}
	}
	
	/*
	 * Authenticate Beacons with the DBss
	 */
	public String[] authenticateBeaconCloudant(String uuid, String major, String minor)
	{
		String[] response = new String[5]; 	
		connectToSQLDatabase(); //connect to SQL database
	    	
	    try
	    {
	     	String sql = "SELECT * FROM beacons WHERE (uuid = '"+uuid+
	     				"' AND major = '"+major+"' AND minor = '"+minor+"')";
	     	PreparedStatement statement = connection.prepareStatement(sql); 
	    	ResultSet result = statement.executeQuery(sql); 
	    	while (result.next()) // loop runs as long as the result set has some data (rows)
	    	{
	    		p_uuid = result.getString(1);
	    		p_major = result.getString(2);
	    		p_minor = result.getString(3);
	    		name = result.getString(4);
	    		usecase = result.getString(5);
	    		
	    	    /* check matching uuid, major and minor */
	    		if((p_uuid.equalsIgnoreCase(uuid)) && (p_major.equalsIgnoreCase(major)) 
	    		    		&& (p_minor.equalsIgnoreCase(minor)))
	    		{
	    		    success = true; //if it matches it then set the success=true
	    		    response[0] = p_uuid; // response array stores the values
	    		    response[1] = p_major; // the UUID and other values are stored in 
	    		    	//the response array and returned back.
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