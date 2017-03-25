<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %> 
<%@ page import="servlets.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Movie Information</title>

  <link href="Style/MovieDetail.css" rel="stylesheet" />
</head>

<body>

  <%@ include file="NavBar.jsp" %>

  <div id="main_content">

    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <table border=1 id="results">
        <tr>
          <td>
            <img src="${movie.banner_url}" width="150" height="200" alt="Image not Available"/>
          </td>
          <td>
            <h3>${movie.title}</h3>
            <p>Year: ${movie.year}</p>
            <p>Director: ${movie.director}</p>
            <p>
              Stars:
                <c:forEach items="${movie.stars}" var="star">
                  <a href="stardetails?star_id=${star.id}">${star.first_name} ${star.last_name}</a>,
                </c:forEach>
            </p>
            <p>
              Genres:
                <c:forEach items="${movie.genres}" var="genre">
                  <a href="movielist?search=2&genre=${genre}"> ${genre}</a>, 
                </c:forEach>
            </p>
            <p>
              Trailer:
              ${movie.trailer_url}</p>
            <p>
              <a href="cartmanager?movie_id=${movie.id}&request=3">Add to Cart</a>
            </p>
          </td>
        </tr>
      </table>

    </div>

  </div>
  </body>
  </html>