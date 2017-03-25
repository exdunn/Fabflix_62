<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %> 
<%@ page import="session.*" %>
<%@ page import="servlets.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Shopping Cart</title>

  <link href="Style/MovieDetail.css" rel="stylesheet" />
</head>

<body>

  <%@ include file="NavBar.jsp" %>

  <div id="main_content">

    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <table border=1 id="results">
        <c:forEach items="${sessionCart.cart}" var="item">
        <tr>
          <td>${item.movie.title}</td>
          <td>
            <form method="POST" action="cartmanager">
			       <input type="text" name="quantity" value="${item.quantity}" style="width: 40px;"/>
			       <input type="hidden" name="movie_id" value="${item.movie.id}"/>
			       <button name="request" value="0" type="submit">Update Quantity</button>
			       <input name="request" value="1" type="image" src="images/cancel/ic_cancel_black_24dp_2x.png"/>
			       </form>
          </td>
        </tr>
        </c:forEach>
        <tr>
          <td><a href="Checkout.jsp"><button class="button_submit">Checkout</button></a>
          </td>
        </tr>
      </table>

    </div>

  </div>
  </body>
  </html>
