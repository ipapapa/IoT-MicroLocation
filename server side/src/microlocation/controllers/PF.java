package microlocation.controllers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import microlocation.controllers.Particle;
public class PF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ParticleFilter filter;
    Particle robot;
    double temp1x=0;
    double temp2x=0;
    double temp1y=0;
    double temp2y=0;
    final Point[] landmarks = new Point[]{new Point(0.345d,0.325d,1.9d),new Point(6.370d,0.365d,1.62d),
    		new Point(3.015d,4.735d,1.325d),new Point(0d,1.747d,1.36d),
    		/*new Point (5.20d, 3.90d,1.2d)*/new Point(4.524d,2.676d,1.45d), new Point(5.088d,4.0d,0.92d),
    		new Point (2.977d, 0.52d,1.365), new Point(6.558d,3.8297d,0.81d)
    };
    	//	,new Point(4.8175d,3.21627d,3.5d),new Point(0d,5.2285d,1.8d),new Point(3.0155d,0.525d,3.5d)};
    final int NUM_PARTICLES = 2000;
  final double WORLD_WIDTH = 6.7D, WORLD_HEIGHT = 6D,WORLD_LENGTH=2.0D;
    //final double WORLD_WIDTH = 1.0D, WORLD_HEIGHT = 1.0D,WORLD_LENGTH=1.0D;
   String pos="6";
   int numBeacons=8;
    File file1= new File("/Users/FahimZafari/Desktop/Thesis/PF/"+NUM_PARTICLES+"_"+numBeacons+"_beacons "+"_"+pos+".txt");
    private String[] re = new String[8];
    public PF() {
        super();
        
    }
    private void setUp() {
    	   
        filter = new ParticleFilter(NUM_PARTICLES, landmarks, WORLD_WIDTH, WORLD_HEIGHT,WORLD_LENGTH);
        filter.setNoise(2.0f, 2.0f, 2.0f);// last one is sensor noise used as sigma in gaussian distribution
        robot = new Particle(landmarks, WORLD_WIDTH, WORLD_HEIGHT,WORLD_LENGTH); //robot also as a particle randomly placed in the world 
  
    }
boolean checkReFull() {
		
		for (String s : re) {
			if (s == null) {
				return false;
			}
		}
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setUp();
		String identity=request.getParameter("landmark");  //minor
		String meters=request.getParameter("meters"); //rssi
	//System.out.println(request.getParameter("meters"));
		if(identity.equals("4"))
			re[0]=meters;
		if(identity.equals("5"))
			re[1]=meters;
		if(identity.equals("6"))
			re[2]=meters;
		if(identity.equals("1"))
			re[3]=meters;
		if(identity.equals("3"))
			re[4]=meters;
		if(identity.equals("2"))
			re[5]=meters;
		if(identity.equals("8"))
			re[6]=meters;
		if(identity.equals("9"))
			re[7]=meters;
		if (checkReFull()) {
			try {
			robot.sense(re);
	//		robot.move(orientation(temp2y-temp1y,temp2x-temp1x),distForward(temp1x,temp2x, temp1y, temp2y));
				filter.move(orientation(temp2y-temp1y,temp2x-temp1x),distForward(temp1x,temp2x, temp1y, temp2y));
		//		robot.sense(re);
				filter.resample(robot.sense(re)); 
				Particle p = filter.getBestParticle();
				 
				 if(!file1.exists())
				 {
					 file1.createNewFile();
				 }
				 FileWriter fw = new FileWriter(file1.getAbsoluteFile(),true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(p+"\n");
					bw.close();
			       System.out.println("Best Particle "+p);
			    //   System.out.println("Average Particle "+filter.getAverageParticle());
			       re = new String[8];
			       temp1x=temp2x;
			       temp1y=temp2y;
			       temp2x=p.x;
			       temp2y=p.y;
			      
			
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			
		}

	}
	
	double orientation(double y,double x)
	{
		double theta=Math.atan2(y,x);
		theta=theta*180/Math.PI;
		//System.out.println("theta is "+theta);
		return theta;
	}
	double distForward(double x1,double x2,double y1,double y2)
	{
	//	System.out.println("distance is "+Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
		double distance= Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		if(distance>1.4)
			return 0.5;
		else
			return distance;
	}
}
