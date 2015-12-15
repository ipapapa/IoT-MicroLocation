
<%@page contentType="application/json; charset=UTF-8"%>
<%@page import="org.json.simple.JSONObject"%>

<%	  
    JSONObject obj=new JSONObject();
 //getAttribute returns the value of the named attriuted as an object or null (if nothing)
	obj.put("xcoordinate",new String((String)request.getAttribute("xcoordinate")));
    // put method adds uuid: the UUID value (f4913...) in the object
    obj.put("ycoordinate",new String((String)request.getAttribute("ycoordinate")));
    //obj.put("xcoordinate","3"); //can force it with this line
    // put method adds uuid: the UUID value (f4913...) in the object
    //obj.put("ycoordinate","5"); //can force it with this line too
   
 	//System.out.println("hellobeacons---");
 	//out.print("hellobeacon_success");
 	//System.out.println((String)request.getAttribute("xcoordinate"));
    //System.out.println((String)request.getAttribute("ycoordinate"));
    //System.out.println(obj);
    out.print(obj);
    out.flush();
    //System.out.println("jsp done");
    
%>