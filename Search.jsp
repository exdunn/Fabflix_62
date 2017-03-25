<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="servlets.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>FabFlix Main</title>

  <link href="Style/Search.css" rel="stylesheet" />
</head>

<body>

  <%@ include file="NavBar.jsp" %>

  <div id="main_content">

    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <form name="Search" action="movielist" method="POST">
        Title<br>
      	<input type="text" name="title" size ="50"/><br>
      	Year<br>
      	<input type="text" name="year" size ="50"/><br>
      	Director<br>
      	<input type="text" name="director" size ="50"/><br>
      	Actor Name<br>
      	<input type="text" name="first_name" size ="20"/>
      	<input type="text" name="last_name" size ="20"/><br>
        <input type="hidden" name="search" value="0"/>
      	<input type="submit" name="submit" value="Search" class="btn_submit"/>
      </form>

    </div>

  </div>


</body>

</html>