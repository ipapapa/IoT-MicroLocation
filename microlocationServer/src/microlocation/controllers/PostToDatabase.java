package microlocation.controllers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Encoder;


public class PostToDatabase
	{
		String ID;
	    String revision;
	    String WeMoSwitch1;
	    String WeMoSwitch2;
	    String TV;
	    String Speakers;
        String decodedString1;

	    
	    public PostToDatabase(String CloudantDocID, String DocRevisionNumber, String WeMoSwitch1, String WeMoSwitch2, String TV, String Speakers)
	        {
	            
	         this.ID = CloudantDocID;   
	         this.revision = DocRevisionNumber;  
	         this.WeMoSwitch1 = WeMoSwitch1;
	         this.WeMoSwitch2 = WeMoSwitch2;
	         this.TV = TV;
	         this.Speakers = Speakers;
	         
	        }
	    
	    public String post() throws IOException{
	    	
	    	//String URLID  =ID.substring(ID.indexOf("\"")+1,ID.indexOf(ID+"\""));
	    	//System.out.println(ID);
	    	String currentURL = "https://brz4lif.cloudant.com/microlocation/"+ID;
	    	
	    	try
				{
					URL url = new URL(currentURL);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
					String UP ="brz4lif:F0rgetme";
					Base64.Encoder encode = Base64.getEncoder(); 
					String encodedUNPW = encode.encodeToString(UP.getBytes());
				//	System.out.println(encodedUNPW.toString());
					connection.setDoOutput(true); 
					connection.setInstanceFollowRedirects(false); 
					connection.setRequestMethod("PUT"); 
					connection.setRequestProperty("Authorization", "Basic "+encodedUNPW.toString());
			        connection.setRequestProperty("Content-Type", "application/json"); 
			        connection.setRequestProperty("charset", "UTF-8");
			        connection.connect();
				
			        String json = "{\"_id\":" + "\""+ ID + "\""+ ",\"_rev\":" + "\"" + revision + "\"" + ",\"value\":{\"WeMoSwitch1\":" + "\""+WeMoSwitch1+"\"" + ",\"WeMoSwitch2\":" + "\""+ WeMoSwitch2 +"\""+  ",\"TV\":" +"\""+ TV+ "\""+ ",\"Speakers\":"+"\""+Speakers+"\""+"}," +"\"key\":"+ "\""+ ID + "\""+"}";
			        System.out.println(json);
				
			        System.out.println("check1");
			        
			        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	                out.write(json);       
	                out.close();
	                
	                //System.out.println("check3");
	                
	                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String decodedString = null;
	                System.out.println("check3");

	                while ((decodedString = in.readLine()) != null){
	                	//System.out.println(decodedString); 
	                	System.out.println(decodedString); 
	                	decodedString1 = decodedString;
	                }  
                
          
	                in.close();
				} 
	    	
	    	catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	
	    	 String iBeaconRev  = decodedString1.substring(decodedString1.indexOf("rev")+6, decodedString1.indexOf("}")-1);
	    	 
	    	 System.out.println(iBeaconRev);
	    	
	    		// {"ok":true,"id":"11496","rev":"3-cb68d1997e8cf576b1c013d9f97c7eb2"}
	    	 
	    	return iBeaconRev;
	    	
	    }
	}
