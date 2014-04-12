<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search - Question Time Home</title>
<link href="css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<form id="search1" method="post" action="ParseResult.jsp">		
		<div id="searchbox">
			<img src="css/qt.png" alt="Question time!"/>
			<div>
				<input type="text" id="q" name="q" placeholder="Type your question..." title="Type your question here"/>
				<input type="submit" id="submit-btn" title="Search"/>
			</div>
		</div>
	</form>
	<div class="clear"></div>
</body>
</html>