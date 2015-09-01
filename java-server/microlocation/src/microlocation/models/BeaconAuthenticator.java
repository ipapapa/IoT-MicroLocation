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
	
	public void connectToSQLDatabase() // method to connect to SQL data base 
	{
		try 
    	{
    		Class.forName("com.mysql.jdbc.Driver"); // forcing the class
    		//that represents MySQLDriver to load and initialize
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
    		//When the method getConnection is called, the DriverManager will 
    		//attempt to locate a suitable driver from amongst those loaded at 
    		//initialization and those loaded explicitly using the same class-loader 
    		//as the current applet or application.
    	//	connection = DriverManager
    		//.getConnection("jdbc:mysql://localhost:3306/microlocation_aws", "root", "");
    		connection = DriverManager
    	    		.getConnection("jdbc:mysql://127.0.0.1:3306/microlocation_aws", "root", "");
    		
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
	//to authenticate beacons from database and then return
	{
		String[] response = new String[5]; // String array of 5 components
		
		connectToSQLDatabase(); //connect to SQL database
    	
    	try
    	{
     		String sql = "SELECT * FROM beacons WHERE (uuid = '"+uuid+
     				"' AND major = '"+major+"' AND minor = '"+minor+"')"; //statement for
     		//datbase i.e search within database
     		PreparedStatement statement = connection.prepareStatement(sql); //sends the 
     		//sql statement to the database that is to make the database search for
     		// uuid major and minor
    		ResultSet result = statement.executeQuery(sql); //stores the values obtained
    		// as a result of database query
    		while (result.next()) // loop runs as long as the result set has some data (rows)
    		{
    			p_uuid = result.getString(1); //(within a specific row, get the
    			// column 1 value and set ur p_uuid to it and so on.
    		    p_major = result.getString(2);
    		    p_minor = result.getString(3);
    		    name = result.getString(4);
    		    usecase = result.getString(5);
    		   
    		    
//    		    String newString1 = uuid + " " + result.getString(1);
//    		    System.out.println(newString1);
    		    
    		    if((p_uuid.equalsIgnoreCase(uuid)) && (p_major.equalsIgnoreCase(major)) 
    		    		&& (p_minor.equalsIgnoreCase(minor)))  //if there is data found in the
    		    	//database or if it matches it
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
