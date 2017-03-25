<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="servlets.*"%>

<style>
  #search_results{
	border-width:1px; border-color:#919191; border-style:solid;
	font-size: 11px; 
	font-family:"Lucida Grande","Lucida Sans Unicode",Arial,Verdana,sans-serif;
	background-color: #141414;
  }
  #movie_title{
	font-weight:bold; 
	 
	color:#dddddd;
  }
  
  #margins_div{
    padding: 8px;
  }
  
</style>


<script type="text/javascript" src="https://ajax.microsoft.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
<script src="Javascript/moviecard.js"></script>

<div id="search_results">
	<div id="margins_div">
	
	<c:forEach items="${movies}" var="movie">

<!-- 		  <span class="movieCardAnchor" id="movie_title">${title}</span> <br/>
		  <span class="seperator"></span><span class="separator"></span> -->

	  <div class="movieCardAnchor" id="${movie.id}">
		<a id="movie_title" href="moviedetails?movie_id=${movie.id}">${movie.title}</a> <br/>
		<span class="seperator"></span><span class="separator"></span>
	  </div>
	</c:forEach>
	
	</div>
</div>