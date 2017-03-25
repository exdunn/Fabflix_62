<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<%@ page import="servlets.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
	  <script src='https://www.google.com/recaptcha/api.js'></script>
	  <link href="Style/login.css" rel="stylesheet" />
	  <TITLE>Fabflix Employee Login</TITLE>
	</HEAD>

	<BODY>

		<!--<%
			if (request.getSession(false) != null && request.getSession().getAttribute("currentUser") != null)
			{
				response.sendRedirect("mainpage");
			}
		%>-->

		<H1 ALIGN="CENTER">Fabflix Employee Login</H1>

		<FORM ACTION="_dashboard" METHOD="post">
		  Email: <INPUT TYPE="EMAIL" NAME="email"><BR>
		  Password: <INPUT TYPE="PASSWORD" NAME="password"><BR>
		  <CENTER>
		    <INPUT TYPE="SUBMIT" VALUE="Submit Order">
		  </CENTER>
		  <input type="hidden" name="option" value="1"/>
		</FORM>

		<c:if test="${not empty status}">
		  <div id="snackbar">${status}</div>
		</c:if>
	</BODY>
</HTML>
