package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

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

@WebServlet ("/stardetails")

public class StarDetails extends HttpServlet {
	
	private final static Logger LOGGER = Logger.getLogger(login.class.getName());
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{   
        try 
        {
        	// performance measurements
        	long startTime = System.nanoTime();
        	
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
        	
        	// declare mysql variables
            Connection conn = DriverManager.getConnection (Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);*/
            Statement statement = conn.createStatement ();
            PreparedStatement ps = null;
            ResultSet rs = null;	
            
            String star_id = request.getParameter ("star_id");            
            String query = "Select * from stars where id = '" + star_id + "'";
            
            // use prepared statement
            if (Constants.PS) {
            	query = "select * from stars where id = ?";
            	ps = conn.prepareStatement(query);
            	ps.setInt(1, Integer.parseInt(request.getParameter("star_id")));
            	rs = ps.executeQuery();
            }
            // use regular statement
            else {
            	rs = statement.executeQuery (query);
            }
            
            // set star info
            Star star = new Star ();
            rs.next();
            star.setId (rs.getInt("id"));
			star.setFirstName (rs.getString ("first_name"));
			star.setLastName (rs.getString ("last_name"));
			star.setDob (rs.getDate ("dob"));
			star.setPhotoURL (rs.getString ("photo_url"));
			
			// set star filmography
			Statement statement2 = conn.createStatement ();
			insertMovies (statement2, star);
			
			for (Movie movie : star.getMovies ())
			{
				//System.out.println (movie.getTitle());
			}
			
			// send request
			request.setAttribute ("star", star);
	        request.getRequestDispatcher ("StarDetails.jsp").forward (request, response);
	        
	        // close resources
            if (conn != null)
            	conn.close();
            
            if (ps != null)
            	ps.close();
            
            if (statement != null)
            	statement.close();
            if (rs != null)
            	rs.close();
            
            // performance measurements
        	long endTime = System.nanoTime();
        	long elapsedTime = (endTime - startTime) / 1000000;
        	if (Constants.LOG) {
        		LOGGER.info("Elapsed Time: " + elapsedTime + "ms");
        	}
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
	private void insertMovies (Statement statement, Star star)
	{
		String query = "select * from movies where id in "
				+ "(select movie_id from stars_in_movies where "
				+ "star_id = '" + star.getId () + "')";
		try
		{
			ResultSet rs = statement.executeQuery (query);
			
			while (rs.next ())
			{
				Movie newMovie = new Movie ();
				newMovie.setId (rs.getInt (1));
            	newMovie.setTitle (rs.getString (2));
            	newMovie.setYear (rs.getInt (3));
            	newMovie.setDirector (rs.getString (4));
            	newMovie.setBannerURL (rs.getString (5));
            	newMovie.setTrailerURL (rs.getString (6));
            	star.addMovie (newMovie);
			}
		}
		catch (SQLException ex) 
		{
            while (ex != null) 
            {
                System.out.println("SQL Exception:  " + ex.getMessage ());
                ex = ex.getNextException ();
            }
        } // end catch SQLException
				
	}
}




