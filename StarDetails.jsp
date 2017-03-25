<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.ArrayList"%>
<%@ page isELIgnored="false" %> 
<%@ page import="servlets.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Star Information</title>

  <link href="Style/StarDetails.css" rel="stylesheet" />
</head>

<body>

  <%@ include file="NavBar.jsp" %>

  <div id="main_content">

    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <table border=1 id="results">
        <tr>
          <td>
            <img src="${star.photo_url}" width="150" height="200" alt="Image not Available"/>
          </td>
          <td>
            <h3>${star.first_name} ${star.last_name}</h3>
            <p>Date of Birth: ${star.dob}</p>
            <p>
              Filmography:
                <c:forEach items="${star.movies}" var="movie">
                  <a href="moviedetails?movie_id=${movie.id}">
                    ${movie.title}
                  </a>,
                </c:forEach>
            </p>
          </td>
        </tr>
      </table>

    </div>

  </div>