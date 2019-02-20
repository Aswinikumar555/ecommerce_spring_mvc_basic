Your application is live

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HelloWorld</title>
</head>
<body>
<form action="/testapp/test/add" method="post">
	  Employee name:<br>
	  <input type="text" name="name" /><br>
	  
	  Employee id:<br>
	  <input type="text" name="userId" /><br>
	  Employee salary:<br>
	  <input type="text" name="salary" /><br><br>
	  <input type="submit" name="submit">
</form>
<form action="/testapp/test/getd" method="post">	  
	  Employee id:<br>
	  <input type="text" name="userId" /><br>
	 
	  <input type="submit" name="submit">
</form>
</body>
</html>

