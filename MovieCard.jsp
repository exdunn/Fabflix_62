<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="Style/MovieCard.css" rel="stylesheet" />

<table>
  	<tr>
    	<td>
			<img src="${movie.banner_url}" width="150" height="200" alt="No Movie Image" id="movie_poster"/>
		</td>
		<td>
			<h3 id="movieCardAnchor">Title: ${movie.title}</h3>
			<p>Year Released: ${movie.year}</p>
			<p>Director: ${movie.director}</p>
			<p>Stars: 
				<c:forEach items="${stars}" var="star">
					<a href="stardetails?star_id=${star.id}">${star.first_name} ${star.last_name}</a>, 
				</c:forEach>
			</p>
			<p>
              <a href="cartmanager?movie_id=${movie.id}&request=3">Add to Cart</a>
            </p>
    	</td>
	</tr>
</table>