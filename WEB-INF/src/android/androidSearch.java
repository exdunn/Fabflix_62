package android;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.Math;
import java.lang.Object;
import java.net.URLEncoder;

import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.*;

import javax.naming.InitialContext;
import javax.naming.Context;

import java_beans.Movie;
import globals.Constants;

@WebServlet("/androidsearch")

public class androidSearch extends HttpServlet {
	
	public void init (ServletConfig config)
			throws ServletException
	{
		try {
			Context initCtx = new InitialContext();
		    Context envCtx = (Context) initCtx.lookup("java:comp/env");
		} catch (Exception e) {
		      e.printStackTrace();
		}
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{        
		PrintWriter out = response.getWriter();
	    response.setContentType("text/plain");
	    response.setCharacterEncoding("utf-8");
		
	    String queryDebug = "";
	    
        try 
        {	
        	// connect to database
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            Connection conn = DriverManager.getConnection (Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);
            
            // create statement
            Statement statement = conn.createStatement ();
            
            // get parameters
            String title = request.getParameter("title");
            String keywords[] = title.split(" ");
            		
            // create the full text search query
            String query = "select * from movies where match (title) against ('";
            for (int i = 0; i < keywords.length; i++)
            {
            	query += " +" + keywords[i];
            	if (i == keywords.length - 1)
            		query += "*";
            }
            query += "' in boolean mode)";
            queryDebug = query;
            
            ResultSet rs = statement.executeQuery (query);
            
            // create json object of movies
            JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
            JsonArrayBuilder titleListBuilder = Json.createArrayBuilder();
            
            while (rs.next ())
            {
            	JsonObjectBuilder movieBuilder = Json.createObjectBuilder()
            			.add("id", rs.getString("id"))
                        .add("title", rs.getString("title"))
                        .add("year", rs.getString("year"))
                        .add("banner", rs.getString("banner_url"));

            	titleListBuilder.add(movieBuilder);
            }
            responseBuilder.add("movies", titleListBuilder);
            JsonObject jsonResponse = responseBuilder.build();
            
            // create text response
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.writeObject(jsonResponse);
            writer.close();
            
            // set status OK
            response.setStatus(200);
            out.write(stringWriter.getBuffer().toString());
            
            if (conn != null && !conn.isClosed())
            	conn.close();
            if (rs != null)
            	rs.close();
        } 
        catch (SQLException ex) 
        {
            while (ex != null) 
            {
            	out.write("SQL Exception:  " + ex.getMessage ());
            	out.write("\nQuery: " + queryDebug);
                ex = ex.getNextException();
            }
        } // end catch SQLException
        catch (java.lang.Exception ex) 
        {
        	response.setStatus(400);
            out.write("java.lang.exception ex: " + ex);
        }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		doGet(request, response);
	}
}




