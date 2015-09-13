
<%@page contentType="application/json; charset=UTF-8"%>
<%@page import="org.json.simple.JSONObject"%>

<%	  
    JSONObject obj=new JSONObject();
 //getAttribute returns the value of the named attriuted as an object or null (if nothing)
	obj.put("uuid",new String((String)request.getAttribute("uuid")));
    // put method adds uuid: the UUID value (f4913...) in the object
    obj.put("major",new String((String)request.getAttribute("major")));
    obj.put("minor",new String((String)request.getAttribute("minor")));
    obj.put("name",new String((String)request.getAttribute("name")));
    obj.put("usecase",new String((String)request.getAttribute("usecase")));
    
    out.print(obj);
    out.flush();
%>