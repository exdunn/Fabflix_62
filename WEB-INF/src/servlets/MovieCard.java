package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

// fabflix classes
import java_beans.*;
import globals.Constants;

@WebServlet ("/moviecard")

public class MovieCard extends HttpServlet {
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{        
		response.setContentType("application/json");
		
        try 
        {
        	// the following few lines are for connection pooling
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            if (initCtx == null)
                System.out.println("initCtx is NULL");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                System.out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("fabflix/moviedb_read");
            
            if (ds == null)
                System.out.println("ds is null.");

            Connection conn = ds.getConnection();
            
            if (conn == null)
                System.out.println("dbcon is null.");
        	
        	/*Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        	Connection conn = DriverManager.getConnection (Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);*/
            Statement statement = conn.createStatement ();
            
            String movie_id = request.getParameter ("movie_id");     
            
            System.out.println("movie_id: " + movie_id);
            	
            if (movie_id.equals("0"))
            	movie_id = findMovieId(request.getParameter("title"), conn);
            
            String query = "select * from movies where id = '" + movie_id + "'";
            ResultSet rs = statement.executeQuery (query);
            
            // set movie info
            Movie movie = new Movie ();
            rs.next();
            
            movie.setId (rs.getInt (1));
            movie.setTitle (rs.getString (2));
            movie.setYear (rs.getInt (3));
            movie.setDirector (rs.getString (4));
            movie.setBannerURL (rs.getString (5));
            movie.setTrailerURL (rs.getString (6));
			
			// insert actors and genres
			Statement statement2 = conn.createStatement ();
			movie.insertGenres (statement2);
			movie.insertStars (statement2);
			
			
			// send request
			request.setAttribute("movie", movie);
			request.getRequestDispatcher("MovieCard.jsp").forward(request, response);
			
			
			// convert to json
			String  movieJson = new Gson().toJson(movie);	
            response.getWriter().write(movieJson);
            System.out.println(movieJson.toString());
            
            if (conn != null && !conn.isClosed())
            	conn.close();
        } 
        catch (SQLException ex) 
        {
            while (ex != null) 
            {
                System.out.println ("SQL Exception:  " + ex.getMessage ());
                ex = ex.getNextException ();
            }
        } // end catch SQLException
        catch (java.lang.Exception ex) 
        {
            System.out.println ("java.lang.exception ex: " + ex);
            return;
        }
	}
	
	private String findMovieId(String title, Connection conn)
	{
		String ret = "";
		String query = "select id from movies where title = '"
				+ "title'";
		
		try {
			Statement state = conn.createStatement();
			
			ResultSet rs = state.executeQuery(query);
			rs.next();
			ret = String.valueOf(rs.getInt(1));
		}
		catch (SQLException ex) {
            while (ex != null) 
            {
                System.out.println ("SQL Exception:  " + ex.getMessage ());
                ex = ex.getNextException ();
            }
        } // end catch SQLException
		return ret;
	}
}


