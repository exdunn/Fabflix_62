<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %> 
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Metadata</title>

  <link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
</head>

<body>

  <div class="main content">

    <p>
      <a href="Dashboard.jsp"> Go Back </a>
    </p>

    <c:forEach items="${tables}" var="table">
        Name: ${table.name}<br>
        Columns<br>
        <c:forEach items="${table.columns}" var="column">
          ${column}<br>
        </c:forEach>
        *********************************************** 
        <br><br>
    </c:forEach>
    
  </div>
</body>
</html>
