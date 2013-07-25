<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
div#container {
	width: 500px
}

div#header {
	background-color: #99bbbb;
}

div#menu {
	background-color: #ffff99;
	height: 200px;
	width: 20%;
	float: left;
}

div#content {
	background-color: #EEEEEE;
	height: 200px;
	width: 80%;
	float: left;
}

div#footer {
	background-color: #99bbbb;
	clear: both;
	text-align: center;
}

h1 {
	margin-bottom: 0;
}

h2 {
	margin-bottom: 0;
	font-size: 18px;
}

ul {
	margin: 0;
}

li {
	list-style: none;
}
</style>
</head>

<body>

	<div id="container">

		<div id="header">
			<h1>Main Title of Web Page</h1>
		</div>

		<div id="content">
			<h2>Menu</h2>
			<ul>
				<li>HTML</li>
				<li>CSS</li>
				<li>JavaScript</li>
			</ul>
		</div>

		<div id="menu">Content goes here</div>

		<div id="footer">Copyright W3School.com.cn</div>

	</div>

</body>
</html>
