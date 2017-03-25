<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Shopping Cart</title>
  <link href="Style/login.css" rel="stylesheet" />
  <link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
</head>

<body>

  <div class="main content">
    <p>
      <a href="InsertStar.jsp"> Insert New Star </a>
    </p>

    <p>
      <a href="_dashboard?option=2"> Show Metadata </a>
    </p>

    <p>
      <a href="InsertMovie.jsp"> Insert New Movie </a>
    </p>
    
    <p>
      <a href="UpdateMovie.jsp"> Add Star/Genre to Movie </a>
    </p>

  </div>

  <c:if test="${not empty status}">
    <div id="snackbar">${status}</div>
  </c:if>
</body>
</html>
