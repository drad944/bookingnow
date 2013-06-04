<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>please register account:</h1>
	 <form action="test/registerAccount.action" method="post">
    	<table>
    		<tr>
    			<td>username:</td>
    			<td><input type="text" name="account.name" value="" /></td>
    		</tr>
    		<tr>
    			<td>password:</td>
    			<td><input type="password" name="account.password" value="" /></td>
    		</tr>
    		<tr>
    			<td>role:</td>
    			<td><input type="text" name="account.role" value="" /></td>
    		</tr>
    		<tr>
    			<td><input type="submit" name="submit" value="submit" /></td>
    			<td><input type="reset" name="reset" value="reset" /></td>
    		</tr>
    		<tr>
    			<td></td>
    			<td></td>
    		</tr>
    	</table>
    </form>
</body>
</html>