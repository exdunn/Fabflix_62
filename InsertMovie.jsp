<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Insert Movie</title>
  
  <link href="Style/Search.css" rel="stylesheet" />
</head>

<body>

  <div id="main_content">

    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <form name="Movie" action="_dashboard" method="POST">
      	Title<br>
      	<input type="text" name="title" size ="100"/><br>
        Year<br>
      	<input type="text" name="year" size ="4"/><br>
        Director<br>
        <input type="text" name="director" size ="50"/><br>
        Banner URL<br>
        <input type="text" name="banner_url" size ="200"/><br>
        Trailer URL<br>
        <input type="text" name="trailer_url" size ="200"/><br>
        Genre<br>
        <input type="text" name="genre" size ="50"/><br>
        Star Name<br>
        <input type="text" name="first_name" size ="20"/>
        <input type="text" name="last_name" size ="20"/><br>
        <input type="hidden" name="option" value="4"/>
      	<input type="submit" name="submit" value="Insert" class="btn_submit"/>
      </form>

    </div>

  </div>

</body>

</html>