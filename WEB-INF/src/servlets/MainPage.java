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
import javax.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

import globals.Constants;
import java_beans.*;

@WebServlet("/mainpage")

public class MainPage extends HttpServlet {
	
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
			
			// print information about current session
			if (Constants.PRINTSESSION) {
				HttpSession session = request.getSession();
				System.out.println("Session: " + session);
				System.out.println("Customer: " + session.getAttribute("email"));
			}				
			
	        /*Class.forName ("com.mysql.jdbc.Driver").newInstance ();
	        Connection conn = DriverManager.getConnection (Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);*/
	        Statement statement = conn.createStatement ();
	        
	        ArrayList<String> genres = getGenreNames(statement);
	        ArrayList<String> alphabet = alphabet();
	        ArrayList<String> numbers = numbers();

	        request.setAttribute("genres", genres);
	        request.setAttribute("alphabet", alphabet);
	        request.setAttribute("numbers", numbers);
			request.setAttribute("genreSize", genres.size());
			request.setAttribute("alphabetSize", alphabet.size());
			request.setAttribute("numbersSize", numbers.size());
				
			request.getRequestDispatcher("MainPage.jsp").forward(request, response);
			
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
	
	
	public ArrayList<String> getGenreNames(Statement statement) throws SQLException 
	{
		String sqlStatement = "SELECT name FROM genres ORDER BY name ASC";
		System.out.println(sqlStatement);
		
		ResultSet result = statement.executeQuery(sqlStatement);
		ArrayList<String> output = new ArrayList<String>();

		while(result.next())
		{
			output.add(result.getString(1));
		}
		
		return output;
	}
	
	public ArrayList<String> alphabet ()
	{
		ArrayList<String> output = new ArrayList <String> ();
		for (int i = 65; i < 91; i++)
		{
			output.add (Character.toString ((char)i));
		}
		return output;
	}
	
	public ArrayList<String> numbers ()
	{
		ArrayList<String> output = new ArrayList <String> ();
		for (int i = 0; i < 10; i++)
		{
			output.add (Integer.toString (i));
		}
		return output;
	}
}





