<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %> 
<%@ page import="servlets.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>FabFlix Movie List</title>

  <link href="Style/MovieList.css" rel="stylesheet" />
  <link href="Style/MovieCard.css" rel="stylesheet" />

  <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
  <script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <script src="Javascript/moviecard.js"></script>
</head>

<bod>

  <%@ include file="NavBar.jsp" %>
  
  <div id="main_content">

  <p>
    Sort By:
    <a href="movielist?query=${query}&page=0&sortBy=0&limit=${limit}"> Title Ascending</a>
    <a href="movielist?query=${query}&page=0&sortBy=1&limit=${limit}"> Title Descending</a>
    <a href="movielist?query=${query}&page=0&sortBy=2&limit=${limit}"> Year Ascending</a>
    <a href="movielist?query=${query}&page=0&sortBy=3&limit=${limit}"> Year Descending</a>
  </p>

  <div>
        Results per page
        <a href="movielist?query=${query}&limit=10&page=0&sortBy=${sortBy}">10</a>
        <a href="movielist?query=${query}&limit=25&page=0&sortBy=${sortBy}">25</a>
        <a href="movielist?query=${query}&limit=50&page=0&sortBy=${sortBy}">50</a>
        <a href="movielist?query=${query}&limit=100&page=0&sortBy=${sortBy}">100</a>
    </div>


    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <table border=1 id="results">
        <c:forEach items="${movieList}" var="movie">
        <tr>
          <td>
            <img src="${movie.banner_url}" width="150" height="200" alt="Image not Available"/>
          </td>
          <td>
            <h3 class="movieCardAnchor" id="${movie.id}">
              <a href="moviedetails?movie_id=${movie.id}">${movie.title}</a>
            </h3>
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
                  ${genre}, 
                </c:forEach>
            </p>
            <p>
              <form action="cartmanager" method="post">
                <button type="submit">Add to Cart</button>
                <input type="hidden" name="movie_id" value="${movie.id}"/>
                <input type="hidden" name="request" value="3"/>
              </form>
            </p>
          </td>
        </tr>
        </c:forEach>
      </table>

      <p> Page:
        <c:if test="${page ne 0}">
          <a href="movielist?query=${query}&page=${page - 1}&limit=${limit}&sortBy=${sortBy}">
            Prev
          </a>
        </c:if>
        <c:forEach begin="0" end="${maxPages}" var="val">
          <a href="movielist?query=${query}&page=${val}&limit=${limit}&sortBy=${sortBy}">${val}</a> 
        </c:forEach>
        <c:if test="${page lt maxPages}">
          <a href="movielist?query=${query}&page=${page + 1}&limit=${limit}&sortBy=${sortBy}">
            Next
          </a>
        </c:if>
      </p>

    </div>

  </div>
  </body>
  </html>