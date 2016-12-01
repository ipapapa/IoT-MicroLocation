package Servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DBManager.BeaconManager;

/**
 * Servlet implementation class MobileListener
 */
@WebServlet("/MobileListener")
public class MobileListener extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BeaconManager db = null;
	private Connection conn = null;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileListener() {
		super();
		db = new BeaconManager();
		conn = db.getConnection();
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.getOutputStream().println("OK! I GOT IT!");
		
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			// get input stream
			int length = request.getContentLength();
			byte[] input = new byte[length];
			ServletInputStream sin = request.getInputStream();
			int c, count = 0;
			while ((c = sin.read(input, count, input.length - count)) != -1) {
				count += c;
			}
			
			//the following are the email id and password combination.
			//u_name and password should contain the value parsed. 
//			String auth_res;
//			if(u_name == "pmajith@ncsu.edu" && password == "11111") {
//			auth_res = "Login Sucessfull" ;
//			}
//
//			else if(u_name == "xma5@ncsu.edu" && password == "22222") {
//			auth_res = "Login Sucessfull" ; 
//			}
//
//			else if(u_name == "tmcrawfo@ncsu.edu" && password == "33333") {
//			auth_res = "Login Sucessfull" ; 
//			}
//
//			else if(u_name == "gpuz0@ncsu.edu" && password == "44444") {
//			auth_res = "Login Sucessfull" ; 
//			}
//			else {
//			auth_res = "Invalid Combination" ; 
//			}
			
			
			sin.close();

			String receivedString = new String(input);
			String[] parseString = receivedString.split("/");

			if(parseString[0].equals("UUID")){
				// Select beacon from debug fragment
				System.out.println("Beacon UUID received: " + parseString[1]);
				
				Statement myStmt;
				myStmt = conn.createStatement();
				ResultSet rs = myStmt.executeQuery("SELECT * FROM beacons WHERE UUID = " + parseString[1]);
				if (rs.next())
				{
					System.out.println(rs.getString("id") + "," + rs.getString("UUID"));
					
					response.setStatus(HttpServletResponse.SC_OK);
					OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
					
					// writer.write(auth_res); // The output stream for authentication response. 
					// writer.write("auth_res + X: " + rs.getString("X") + " , " + "Y: " + rs.getString("Y"));
					writer.write("X: " + rs.getString("X") + " , " + "Y: " + rs.getString("Y"));
					writer.flush();
					writer.close();
				}
			}
			else {
				// Micro-location algorithm
				// Parse the 3 distance out
				double D1 = Double.parseDouble(parseString[0]);
				double D2 = Double.parseDouble(parseString[1]);
				double D3 = Double.parseDouble(parseString[2]);
				
				double X1 = 1, Y1 = 0;
				double X2 = 0, Y2 = 1.732;
				double X3 = 2, Y3 = 1.732;
				
				System.out.println("D1: " + D1 + "D2: " + D2 + "D3: " + D3);
				// calculate
				String intersec = calculateThreeCircleIntersection(X1, Y1, D1, // circle 1 (center_x, center_y, radius)
						                        X2, Y2, D2, // circle 2 (center_x, center_y, radius)
						                        X3, Y3, D3);// circle 3 (center_x, center_y, radius)
//				String intersec = null;
//				
//				if(D1< D2 && D1 < D3 && D1 < 0.5){
//					intersec = "1/Shirts - Brooks Brothers -  20 % off" ;
//				}
//				else if(D2< D1 && D2 < D3 && D2 < 0.5){
//					intersec = "2/Shoes - Nike -  25 % off" ;
//				}
//				else if(D3< D2 && D3 < D1 && D3 < 0.5){
//					intersec = "3/Jeans - Levi's -  10 % off" ; 
//				}
//				else {
//					intersec = "4/Currently sales in shirts, shoes, jeans "   ; 
//				}
				
				
				response.setStatus(HttpServletResponse.SC_OK);
				OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
	
				writer.write(intersec);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			try {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().print(e.getMessage());
				response.getWriter().close();
			} catch (IOException ioe) {
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Calculate Three Circle Intersection
	private final double EPSILON = 0.1;
	
	private String calculateThreeCircleIntersection(double x0, double y0, double r0, double x1, double y1, double r1,
			double x2, double y2, double r2) {
		double a, dx, dy, d, h, rx, ry;
		double point2_x, point2_y;

		/*
		 * dx and dy are the vertical and horizontal distances between the
		 * circle centers.
		 */
		dx = x1 - x0;
		dy = y1 - y0;

		/* Determine the straight-line distance between the centers. */
		d = Math.sqrt((dy * dy) + (dx * dx));

		/* Check for solvability. */
		if (d > (r0 + r1)) {
			/* no solution. circles do not intersect. */
			return "none";
		}
		if (d < Math.abs(r0 - r1)) {
			/* no solution. one circle is contained in the other */
			return "none";
		}

		/*
		 * 'point 2' is the point where the line through the circle intersection
		 * points crosses the line between the circle centers.
		 */

		/* Determine the distance from point 0 to point 2. */
		a = ((r0 * r0) - (r1 * r1) + (d * d)) / (2.0 * d);

		/* Determine the coordinates of point 2. */
		point2_x = x0 + (dx * a / d);
		point2_y = y0 + (dy * a / d);

		/*
		 * Determine the distance from point 2 to either of the intersection
		 * points.
		 */
		h = Math.sqrt((r0 * r0) - (a * a));

		/*
		 * Now determine the offsets of the intersection points from point 2.
		 */
		rx = -dy * (h / d);
		ry = dx * (h / d);

		/* Determine the absolute intersection points. */
		double intersectionPoint1_x = point2_x + rx;
		double intersectionPoint2_x = point2_x - rx;
		double intersectionPoint1_y = point2_y + ry;
		double intersectionPoint2_y = point2_y - ry;

		System.out.println("INTERSECTION Circle1 AND Circle2:(" + intersectionPoint1_x + "," + intersectionPoint1_y + ")"
				+ " AND (" + intersectionPoint2_x + "," + intersectionPoint2_y + ")");

		/*
		 * Lets determine if circle 3 intersects at either of the above
		 * intersection points.
		 */
		dx = intersectionPoint1_x - x2;
		dy = intersectionPoint1_y - y2;
		double d1 = Math.sqrt((dy * dy) + (dx * dx));

		dx = intersectionPoint2_x - x2;
		dy = intersectionPoint2_y - y2;
		double d2 = Math.sqrt((dy * dy) + (dx * dx));
		
//		if (Math.abs(d1 - r2) < Math.abs(d2 - r2)) {
//			System.out.println("INTERSECTION Circle1 AND Circle2 AND Circle3:(" + intersectionPoint1_x + "," + intersectionPoint1_y + ")");
//			return intersectionPoint1_x + "/" + intersectionPoint1_y;
//		} else if (Math.abs(d2 - r2) < EPSILON) {
//			System.out.println("INTERSECTION Circle1 AND Circle2 AND Circle3:(" + intersectionPoint2_x + "," + intersectionPoint2_y + ")"); // here
//																					// was
//																					// an
//																					// error
//			return intersectionPoint2_x + "/" + intersectionPoint2_y;
//		} else {
//			System.out.println("INTERSECTION Circle1 AND Circle2 AND Circle3: NONE");
//			return intersectionPoint1_x + "/" + intersectionPoint1_y;
//		}

		if (Math.abs(d1 - r2) < EPSILON) {
			System.out.println("INTERSECTION Circle1 AND Circle2 AND Circle3:(" + intersectionPoint1_x + "," + intersectionPoint1_y + ")");
			return intersectionPoint1_x + "/" + intersectionPoint1_y;
		} else if (Math.abs(d2 - r2) < EPSILON) {
			System.out.println("INTERSECTION Circle1 AND Circle2 AND Circle3:(" + intersectionPoint2_x + "," + intersectionPoint2_y + ")"); // here
																					// was
																					// an
																					// error
			return intersectionPoint2_x + "/" + intersectionPoint2_y;
		} else {
			System.out.println("INTERSECTION Circle1 AND Circle2 AND Circle3: NONE");
			return intersectionPoint1_x + "/" + intersectionPoint1_y;
		}
	}

}
