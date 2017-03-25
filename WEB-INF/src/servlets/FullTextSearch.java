package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.lang.Math;
import java.lang.Object;
import java.net.URLEncoder;
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

@WebServlet("/fulltextsearch")

public class FullTextSearch extends HttpServlet {
	
	private static Logger LOGGER = Logger.getLogger(FullTextSearch.class.getName());
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{        
        try 
        {	
        	// measure time to execute
        	long TSStartTime = System.nanoTime();
        	long TJStartTime = TSStartTime;
        	
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
        	
            //Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            
            // declare mysql variables
            //Connection conn = DriverManager.getConnection (Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);
            Statement statement = conn.createStatement ();
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            String query = "select * from movies where match (title) against ('";
            String keywords[] = null;
            int limit  = Constants.DEFAULTLIMIT;
            int page = Constants.DEFAULTPAGE;
            
            if (request.getParameter("search") != "" && request.getParameter("search") != null)
            {
            	keywords = request.getParameter("search").split(" ");
            }
            
            // use prepared statements
            if (Constants.PS) {
            	// create prepared statement query
            	query = "select * from movies where match (title) against (?";	
            	for (int i = 0; i < keywords.length-1; i++) {
                	query += " ?";
                }
                query += " in boolean mode)";
            	
                ps = conn.prepareStatement(query);
                
            	// add keywords to prepared statement
            	for (int i = 0; i < keywords.length; i++) {
                	if (i == keywords.length - 1) {
                		ps.setString(i + 1, "+" + keywords[i] + "*");
                	}
                	else {
                		ps.setString(i + 1, "+" + keywords[i]);
                	}
                }
                rs = ps.executeQuery();
            }
            // don't use prepared statements
            else {
            	for (int i = 0; i < keywords.length; i++)
                {
                	query += " +" + keywords[i];
                	if (i == keywords.length - 1)
                		query += "*";
                }
                query += "' in boolean mode)";
                
                rs = statement.executeQuery(query);
            }
                       
            // print the full text query
            //System.out.println("FTS Query: " + query);
            
            ArrayList<Movie> movieList = new ArrayList<Movie> ();
            
            while (rs.next ())
            {
            	Statement statement2 = conn.createStatement();
            	
            	Movie newMovie = new Movie ();
            	newMovie.setId (rs.getInt (1));
            	newMovie.setTitle (rs.getString (2));
            	newMovie.setYear (rs.getInt (3));
            	newMovie.setDirector (rs.getString (4));
            	newMovie.setBannerURL (rs.getString (5));
            	newMovie.setTrailerURL (rs.getString (6));
            	newMovie.insertGenres (statement2);
            	newMovie.insertStars (statement2);
            	movieList.add(newMovie);
            }
            
            // performance measurements
            long TJEndTime = System.nanoTime();
            long TJElapsedTime = TJEndTime - TJStartTime;
            
            ArrayList<Movie> finalMovieList = new ArrayList<Movie> ();
            
            for (int i = 0; i < movieList.size(); i++) {
            	if (page * limit <= i && i < (page + 1) * limit) {
            		finalMovieList.add(movieList.get(i));
            	}
            }
            
            request.setAttribute ("movieList", finalMovieList);
            request.getRequestDispatcher ("MovieList.jsp").forward (request, response);
            
            // close resources
            if (conn != null)
            	conn.close();
            
            if (ps != null)
            	ps.close();
            
            if (statement != null)
            	statement.close();
            if (rs != null)
            	rs.close();
            
            // performance measurement
            long TSEndTime = System.nanoTime();
            long TSElapsedTime = TSEndTime - TSStartTime;
            LOGGER.info("[" + TSElapsedTime + "," + TJElapsedTime +"]");
        }
        catch (SQLException ex) 
        {
            while (ex != null) 
            {
                System.out.println("SQL Exception:  " + ex.getMessage ());
                ex = ex.getNextException();
            }
        } // end catch SQLException
        catch (java.lang.Exception ex) 
        {
            System.out.println("java.lang.exception ex: " + ex);
            return;
        }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
}




