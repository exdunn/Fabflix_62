<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Insert Star</title>

  <link href="Style/Search.css" rel="stylesheet" />
</head>

<body>

  <div id="main_content">

    <div style="font-size:20px;margin-left: auto;margin-right: auto; width: 30em;">
      
      <form name="ActorInfo" action="_dashboard" method="POST">
      	Actor Name<br>
      	<input type="text" name="first_name" size ="20"/>
      	<input type="text" name="last_name" size ="20"/><br>
        Date of Birth (MM/dd/yyyy)<br>
        <input type="text" name="dob" size ="10"/><br>
        Photo URL<br>
        <input type="text" name="photo_url" size ="50"/><br>
        <input type="hidden" name="option" value="3"/>
      	<input type="submit" name="submit" value="Insert" class="btn_submit"/>
      </form>

    </div>

  </div>

</body>

</html>