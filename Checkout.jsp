<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="servlets.*"%>


<!DOCTYPE HTML PUBLIC>

<html>
<head>
  <title>Checkout</title>
</head>
my nama jef

<body>

  <%@ include file="NavBar.jsp" %>

   <div style="font-size:20px;margin-top:50px; margin-left: auto;margin-right: auto; width: 30em;">    <form name="checkout" action="checkout" method="POST">
      Credit Card Number: <br>
      <input type="text" name="cc_id" size="50" value="" required/><br>
      Full Name: <br>
      <input type="text" name="first_name" size="20" id="first_name" value="" required/>
	  <input type="text" name="last_name" size="20" id="last_name" value="" required/><br>
	  Expiration Date:  <br>
	  <input type="text" name="expiration" size="50" id="expiration" maxlength="10" value="" required/><br> 
	  <input type="submit" name="Search Submit" value="submit" style="margin-left:50%; margin-top:20;"/>
    </form>

  </div>


</body>

</html>