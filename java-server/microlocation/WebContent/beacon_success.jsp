
<%@page contentType="application/json; charset=UTF-8"%>
<%@page import="org.json.simple.JSONObject"%>

<%	  
    JSONObject obj=new JSONObject();
    
	obj.put("uuid",new String((String)request.getAttribute("uuid")));
    obj.put("major",new String((String)request.getAttribute("major")));
    obj.put("minor",new String((String)request.getAttribute("minor")));
    obj.put("name",new String((String)request.getAttribute("name")));
    obj.put("usecase",new String((String)request.getAttribute("usecase")));
    
    out.print(obj);
    out.flush();
%>