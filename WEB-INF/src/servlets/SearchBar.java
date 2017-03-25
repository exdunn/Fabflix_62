package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import com.google.gson.Gson;
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

@WebServlet("/searchbar")

public class SearchBar extends HttpServlet {
	
	private final static Logger LOGGER = Logger.getLogger(login.class.getName());
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{     		
		response.setContentType("application/json");
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
            
            String query = "select id, title from movies where match (title) against ('";
            String keywords[] = null;
            
            if (request.getParameter("term") != "" && request.getParameter("term") != null)
            {
            	keywords = request.getParameter("term").split(" ");
            	//System.out.println("Data from ajax call " + keywords);
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
          
            // add result set to array list
            ArrayList<Movie> list = new ArrayList<Movie>();
            while (rs.next ())
            {
            	Movie movie = new Movie();
            	movie.setId(rs.getInt(1));
            	movie.setTitle(rs.getString(2));
            	list.add(movie);
            }
            
            /*ArrayList<String> list = new ArrayList<String>();
            while(rs.next())
            {
            	String title = rs.getString(2);
            	list.add(title);
            }*/
            
            // forward response
            request.setAttribute("movies", list);
            request.getRequestDispatcher("Dropdown.jsp").forward(request, response);
            
            // convert to json
            String titles = new Gson().toJson(list);	
            response.getWriter().write(titles);
            System.out.println(titles);
            
            // close resources
            if (conn != null)
            	conn.close();
            
            if (ps != null)
            	ps.close();
            
            if (statement != null)
            	statement.close();
            
            // performance measurements
        	long endTime = System.nanoTime();
        	long elapsedTime = (endTime - startTime) / 1000000;
        	if (Constants.LOG) {
        		LOGGER.info("Elapsed Time: " + elapsedTime + "ms");
        	}
            
        } catch (SQLException ex) {
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




