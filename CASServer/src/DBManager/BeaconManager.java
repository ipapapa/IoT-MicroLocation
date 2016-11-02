package DBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BeaconManager {

	public Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cas_database", "root", "123qwe");
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
			return null;
		} 
	}
}
