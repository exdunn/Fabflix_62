<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %> 
<%@ page import="servlets.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Nav Bar</title>

  <link rel="stylesheet" href="https://www.w3schools.com/lib/w3.css">
  <link href="Style/NavBar.css" rel="stylesheet"/>
  <link rel="stylesheet" href="style.css">

  <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
  <script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <script src="Javascript/autocomplete.js"></script>

</head>

<body>

  <div class="w3-container" id="dropdownAnchor">

    <ul class="w3-navbar w3-black">
  	  <li><a href="Search.jsp" id="search_img_link">Search<img src="images/ic_search_black_24dp_2x.png"/></a></li>
  	  <li><a href="mainpage" id="search_img_link">Browse<img src="images/browse/ic_text_format_black_24dp_2x.png"/></a></li></a></li> 
  	  <li><a href="ShoppingCart.jsp" id="search_img_link">Checkout<img src="images/cart/ic_shopping_cart_black_24dp_2x.png"/></a></li></a></li>
  	  <li><a href="logout" id="search_img_link">Logout<img src="images/logout/ic_directions_run_black_24dp_2x.png"/></a></li>

      <li>
        <div class="headerbg" style="margin-top: 20px">
          <form name="Search" id="searchForm" action="fulltextsearch" method="post">  
                <input type="text" id="searchBar" name="search" class="search" placeholder="Title..." onkeyup="lookup(this.value);"/>
                <button>Search</button>
          </form>
        </div>
      </li>
  	</ul>
    
  </div>
</body>
</html>
