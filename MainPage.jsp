<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="servlets.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>FabFlix Main</title>
</head>

<body>

  <%@ include file="NavBar.jsp" %>

  <div id="main_content">
    <fieldset>
  	  <legend><b>Browse by Genre</b></legend>
    	<c:forEach items="${genres}" var="genre">
      	  <a href="movielist?search=2&genre=${genre}"> ${genre}</a> &nbsp
    	</c:forEach>
    </fieldset>
  
	<fieldset>
  	  <legend><b>Browse by Title</b></legend>
  	  <p>
    	<c:forEach items="${alphabet}" var="letter">
      	  <a href="movielist?search=1&letter=${letter}"> <button>${letter}</button></a>
    	</c:forEach>
    	</p>
    	<c:forEach items="${numbers}" var="number">
      	  <a href="movielist?search=1&letter=${number}"> <button>${number}</button></a>
    	</c:forEach>
    </fieldset>

  </div>


</body>

</html>