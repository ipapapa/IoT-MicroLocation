<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Microlocation Login</title>
</head>
<body>
    <form action="LoginController" method="post">
        Enter username : <input type="text" name="username"> <BR>
        Enter password : <input type="password" name="password"> <BR>
        <input type="submit" />
    </form>
    <br/>
    <a href="http://10.0.0.4:8080/incareer/signup.jsp">Sign Up</a>

<!-- 		<form action="OccupationController" method="post">
			<input type="submit" name="Get Occupations">
		</form> -->
</body>
</html>