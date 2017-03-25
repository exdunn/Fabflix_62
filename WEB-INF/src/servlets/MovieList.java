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

@WebServlet("/movielist")

public class MovieList extends HttpServlet {
	
	private final static Logger LOGGER = Logger.getLogger(login.class.getName());
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{   
		System.out.println("1");
        try 
        {	
        	System.out.println("hello");
        	// performance measurements
        	long TSStartTime = System.nanoTime();
        	long TJStartTime = TSStartTime;
        	
        	// check for invalid search parameters
        	invalidSearch (request, response);
        	
        	// the following few lines are for connection pooling
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            if (initCtx == null)
                System.out.println("initCtx is NULL");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            System.out.println("context: " + envCtx);
            if (envCtx == null)
                System.out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("fabflix/moviedb_read");
            System.out.println("ds: " + ds);
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
            
            System.out.println("2");
            
            String query = "";
            int sortBy = 0;
            int limit = 10;
            int page = 0;
            int search = 0;

            sortBy = request.getParameter ("sortBy") != null ? 
            		Integer.parseInt ( (String)request.getParameter ("sortBy")) : sortBy;
            limit = request.getParameter ("limit") != null ? 
            		Integer.parseInt ( (String)request.getParameter ("limit")) : limit;
            page = request.getParameter ("page") != null ? 
            		Integer.parseInt ( (String)request.getParameter ("page")) : page;
            search = request.getParameter ("search") != null ? 
                    Integer.parseInt ( (String)request.getParameter ("search")) : search;
            		
            System.out.println("3");
                    
            if (request.getParameter ("query") != null)
            { 
            	query = request.getParameter ("query");
            }
            else
            {     
            	System.out.println("5");
            	// create the SQL query
	            query = "select * from movies where ";
	            
	            // use prepared statement
	            if (Constants.PS) {
	            	// search from search form
	            	if (search == 0)
	            	{
		            	if (!request.getParameter("title").equals(""))
		            		query += "title like ? and ";
		            	if (!request.getParameter("year").equals(""))
		            		query += "year = ? and ";
		            	if (!request.getParameter("director").equals(""))
		            		query += "director like ? and ";
		            	
		            	// if user searching for first and/or last name
		            	if (!request.getParameter("first_name").equals("") || !request.getParameter("last_name").equals("")) {
		            		query += " id in "
		            				+ "(select movie_id from stars_in_movies where star_id in"
		            				+ " (select id from stars where ";
		            		
		            		if (!request.getParameter("first_name").equals(""))
		            			query += "first_name like ? and ";
		            		if (!request.getParameter("last_name").equals(""))
		            			query += "last_name like ?";
		            		
		            		query = trimQuery (query) + ")) ";
		            	}
		            	
		            	query = trimQuery(query);
		            	// add order
	            		query = addOrderToQuery(query, sortBy);
	            		ps = conn.prepareStatement(query);
	            		
	            		// put data in the prepared statement
	            		int counter = 1;
	            		if (!request.getParameter("title").equals("")) {
	            			ps.setString(counter, "%" + request.getParameter("title") + "%");
	            			counter++;
	            		}
	            		if (!request.getParameter("year").equals("")) {
	            			ps.setInt(counter, Integer.parseInt(request.getParameter("year")));
	            			counter++;
	            		}
	            		if (!request.getParameter("director").equals("")) {
	            			ps.setString(counter, request.getParameter("director") + "%");
	            			counter++;
	            		}
	            		if (!request.getParameter("first_name").equals("")) {
	            			ps.setString(counter, request.getParameter("first_name"));
	            			counter++;
	            		}
	            		if (!request.getParameter("last_name").equals("")) {
	            			ps.setString(counter, request.getParameter("last_name"));
	            		}
	            	}
	            	// browse by letter
	            	else if (search == 1)
	        		{
	        			query += "1=1 AND movies.title LIKE ?";
	        			ps = conn.prepareStatement(query);
	        			ps.setString(1, request.getParameter("letter") + "%");
	        		}
	            	// browse by genre
	        		else if(search == 2)
	        		{
	        			query += "id in "
	        					+ "(select movie_id from genres_in_movies where genre_id in"
	        					+ "(select id from genres where "
	        					+ "name = ?))";
	        					
	        			ps = conn.prepareStatement(query);
	        			ps.setString(1, request.getParameter("genre"));
	        		}
            		// execute prepared statement
            		rs = ps.executeQuery();
	            }
	            // dont use prepared statement
	            else {
	            	query = createQuery (query, search, request);
	            	query = addOrderToQuery (query, sortBy);
	            	rs = statement.executeQuery(query);
	            }
            }
            
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
            long TJElapsedTime = (TJEndTime - TJStartTime) / 1000000;
            LOGGER.info("TJ: " + TJElapsedTime);
            
            ArrayList<Movie> finalMovieList = new ArrayList<Movie> ();
            
            for (int i = 0; i < movieList.size(); i++)
            {
            	if (page * limit <= i && i < (page + 1) * limit)
            	{
            		finalMovieList.add(movieList.get(i));
            	}
            }
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            
            // System.out.println("\nEncoded Query: " + encodedQuery + "\n");
            
            request.setAttribute ("maxPages", (int) Math.ceil(movieList.size () / limit));
            request.setAttribute ("search", search);
            request.setAttribute ("sortBy", sortBy);
            // '%' needs to be escaped for JSPs
            request.setAttribute ("query", encodedQuery);
            request.setAttribute ("limit", limit);
            request.setAttribute ("page", page);
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
            
            // performance measurements
        	long TSEndTime = System.nanoTime();
            long TSElapsedTime = (TSEndTime - TSStartTime) / 1000000;
            LOGGER.info("TS: " + TSElapsedTime);
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
	
	private String createQuery (String query, int search, HttpServletRequest request)  
	{
		//System.out.println("search option: " + search);
		
		if (search == 0)
		{
			query = !request.getParameter ("title").equals ("") ?  query + "title like '%" + request.getParameter ("title") +"%' and " : query;
			query = !request.getParameter ("year").equals ("") ?  query + "year = '" + request.getParameter ("year") +"' and " : query;
			query = !request.getParameter ("director").equals ("") ?  query + "director like '%" + request.getParameter ("director") + "%' and " : query;
			
			if (!request.getParameter ("first_name").equals ("") || !request.getParameter ("last_name").equals (""))
			{
				query += "id in "
						+ "(select movie_id from stars_in_movies where star_id in"
						+ "(select id from stars where ";
				
				query = !request.getParameter ("first_name").equals ("") ?  query + "first_name like '%" + request.getParameter ("first_name") + "%' and " : query;
				query = !request.getParameter ("last_name").equals ("") ?  query + "last_name like '%" + request.getParameter ("last_name") + "%'" : query;
				query = trimQuery (query) + ")) ";
			}
		}
		else if (search == 1)
		{
			query += "1=1 AND movies.title LIKE '" + request.getParameter ("letter") + "%' ";
		}
		else if(search == 2)
		{

				query += "id in "
						+ "(select movie_id from genres_in_movies where genre_id in"
						+ "(select id from genres where ";
				query =   query + "name = '" + request.getParameter ("genre").trim();
				query = trimQuery (query) + "')) ";
		}
		return trimQuery (query);
	}
	
	private String addOrderToQuery (String query, int sortBy)
	{
		query = query.split("order")[0];
		switch (sortBy)
		{
			case 0:
				query += " order by title asc";
				return query;
			case 1:
				query += " order by title desc";
				return query;
			case 2:
				query += " order by year asc";
				return query;
			case 3:
				query += " order by year desc";
				return query;
			default: 
				query += " order by title asc";
				return query;
		}
	}
	
	// remove trailing "and " from query
	private String trimQuery (String query)
	{
		if (query.endsWith("and"))
		{
			return query.substring(0, query.length () - 4);
		}
		if (query.trim().endsWith("and"))
		{
			return query.substring(0, query.length () - 5);
		}
		return query;
	}

	private void invalidSearch (HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			if (request.getParameter ("search").equals ("0") &&
				request.getParameter ("director").equals ("") &&
				request.getParameter ("year").equals ("") &&
				request.getParameter ("title").equals ("") &&
				request.getParameter ("first_name").equals ("") &&
				request.getParameter ("last_name").equals (""))
			{
				request.getRequestDispatcher ("Search.jsp").forward (request, response);
			}
		}
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




