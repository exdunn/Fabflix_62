<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<%@ page import="servlets.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
	  <script src='https://www.google.com/recaptcha/api.js'></script>
	  <link href="Style/login.css" rel="stylesheet" />
	  <TITLE>Fabflix Login</TITLE>
	</HEAD>

	<BODY>

		<%
			if (request.getSession(false) != null && request.getSession().getAttribute("currentUser") != null)
			{
				response.sendRedirect("mainpage");
			}
		%>

		<H1 ALIGN="CENTER">Fabflix Login</H1>
		<div align="center">
			<FORM ACTION="login" METHOD="post" >
			  Email: <INPUT TYPE="EMAIL" NAME="email"><br><br>
			  Password: <INPUT TYPE="PASSWORD" NAME="password"><br><br>
			  <CENTER>
			    <INPUT TYPE="SUBMIT" VALUE="Submit Order">
			  </CENTER>
			  <!-- <div class="g-recaptcha" data-sitekey="6LcI6hQUAAAAAItfLuelpastxycvbNWV2yO41h6G"></div> -->
			</FORM>
		<div>
		<c:if test="${not empty status}">
		  <div id="snackbar">${status}</div>
		</c:if>
	</BODY>
</HTML>
