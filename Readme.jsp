<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
  <TITLE>README</TITLE>
</HEAD>

<BODY>
<p>
TO COMPILE

******************************
javac -cp "lib/servlet-api-3.0.jar;lib/mysql-connector-java-5.0.8-bin.jar;gson-2.3.1.jar;classes" -d classes src/{folder}/*.java 

*in order to compile saxParser include -Xlint:unchecked


FABFLIX

*****************************

ec2-35-162-239-108.us-west-2.compute.amazonaws.com

mysql:
testuser
testpass

fabflix customer:
a@email
a2

fabflix employee:
a@email
a2

*Google captcha is only on customer login, not employee login

To use Fabflix:

1a. go to ec2-35-162-239-108.us-west-2.compute.amazonaws.com:8443/project3_62

1b. to login as employee go to ec2-35-162-239-108.us-west-2.compute.amazonaws.com:8443/project3_62/_dashboard



TO RUN SAXPARSER

*****************************

1. Navigate to classes folder

2. Enter the command: "java -cp ".;../lib/*" saxParser/main"

3. If you are in linux you have to install the javafx package by entering 
"sudo apt-get install openjfx"

In windows:

C:\path\to\webapps\fabflix_62\classes> java -cp ".;../lib/*" saxParser/main

In Linux:

C:\path\to\webapps\fabflix_62\classes> java -cp ".:../lib/*" saxParser/main


SAXPARSER OPTIMIZATIONS

*************************************************

1. Instead of inserting records into the database one at a time we use batch insert.  
This is implemented similarly using a for each loop but under the hood, batch insert is 
much more efficient than multiple insert statements.

2. In order to handle duplicate records efficiently we first query the relevant table for 
the significant attribute-- title for movies and name for actors.  Then when we insert 
the parsed records into the database we check to see if the data structure containing the 
"old" records contains the new record.  If not then the record is added, and if it does the 
record is ignored.
</p>
</BODY>
</HTML>
