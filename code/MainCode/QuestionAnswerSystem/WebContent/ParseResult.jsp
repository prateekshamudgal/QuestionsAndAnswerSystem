<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="edu.buffalo.ui.WikiParser,edu.buffalo.QueryParser.queryResults,edu.buffalo.queryanalyser.QueryParser"
    		errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head><title>Search Results - Question Time</title>
	<link href="css/style.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript" src="js/script.js"></script>
	</head>
	<body>
		<form id="search2" method="post" action="ParseResult.jsp">
			<div id="searchbox">
				<a href="QuestionTimeHome.jsp"><img src="css/qt.png" alt="Question time!"/></a>
				<div>
					<input type="text" id="q" name="q" placeholder="Type your question..." title="Type your question here"/>
					<input type="submit" id="submit-btn" title="Search"/>
				</div>
			</div>
		</form>
		<div class="clear"></div>
		<% 
			String query =  request.getParameter("q"); 
			String url = "", ans="";
			String time="0";  
			if (query == null || query.trim() == "") 
			{ %>
				<!-- <h2>No answer found. Please try another question.</h2> -->				
		 <% } 
			else
			{ 	%>			
				<h2>Answer found for <em><%= query %></em></h2>		
				<div id="resultlist">
				<ul>
				   <%
				   String Path = System.getProperty("user.home")+"/HelloWorld_properties/";
				   
					QueryParser qp = new QueryParser(Path);
				   	queryResults qr = new queryResults(query,qp,Path);
				   	qr.findAttribute();
				   	url = qr.handle;  //////////////////// get the URL
					 ans = qr.answer;  //////////////////// get the Ans
					 ans = ans.replaceAll("\\[", "");
					 ans = ans.replaceAll("\\]", "");
					 System.out.println("query: "+ query );
					 System.out.println("handle: "+ url );
					 System.out.println("answer: "+ ans );
					 
			 System.out.println("Working Directory = " +
              System.getProperty("user.home"));
					%>				
					<li class="result">
						<a href="http://en.wikipedia.org/wiki/<%= url %>">
							<h4><%= url.replace("_"," ") %></h4>
							<p><%= ans.replace("WIKI:","").replace("_", " ") %></p>
						</a>
					</li>
				</ul>
				</div>
				<%  WikiParser w = new WikiParser(); 
					String info = w.extractInfoBox(url);
				    if(info != "")
				    { %>
				 	    <div id="infobox">
						    <%= info %>
					    </div>
				 <% } %>
		<% } %>
	</body>
</html>