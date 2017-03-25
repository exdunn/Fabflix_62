<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Add Genre and Star</title>
  
  <link href="Style/Search.css" rel="stylesheet" />
</head>

<body>

  <div id="main_content">

    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <form name="Movie" action="_dashboard" method="POST">
      	Title<br>
        <input type="text" name="title" size = "100"/><br>
        Genre<br>
        <input type="text" name="genre" size ="50"/><br>
        Star Name<br>
        <input type="text" name="first_name" size ="20"/>
        <input type="text" name="last_name" size ="20"/><br>

        
        <!--<input type="hidden" name="year" value = ""/>
        <input type="hidden" name="director" value = ""/>
        <input type="hidden" name="banner_url" value = ""/>
        <input type="hidden" name="trailer_url" value = ""/>-->
        <input type="hidden" name="option" value="4"/>
      	<input type="submit" name="submit" value="Update" class="btn_submit"/>
      </form>

    </div>

  </div>

</body>

</html>