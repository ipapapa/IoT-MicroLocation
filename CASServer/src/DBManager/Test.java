package DBManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BeaconManager db = new BeaconManager();
		Connection conn = db.getConnection();
		if(conn == null) {
			System.out.println("Connection failed!");
		}
		else {
			System.out.println("Connected.");
		}
		
		try {
			Statement myStmt = conn.createStatement();
//			String sql = "INSERT into beacons (UUID, MAJOR, MINOR, X, Y) VALUES ('1234567891098765', 1, 12345, 100, 100)";
//			myStmt.executeUpdate(sql);

			ResultSet rs = myStmt.executeQuery("SELECT * FROM beacons");
			for(int i = 0; i < 10; i++)
			{
				rs.next();
				System.out.println(rs.getString("id") + "," + rs.getString("UUID"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
