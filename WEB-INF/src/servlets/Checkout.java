package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.*;
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

// fabflix classes
import java_beans.*;
import manager.CartManager;
import globals.Constants;

@WebServlet("/checkout")

public class Checkout extends HttpServlet 
{	
	private final static Logger LOGGER = Logger.getLogger(login.class.getName());
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{        
        try 
        {
        	// performance measurements
        	long startTime = System.nanoTime();
        	
        	// print information about current session
        	if (Constants.PRINTSESSION) {
        		HttpSession session = request.getSession();
        		Customer c = (Customer) session.getAttribute("currentUser");
        		System.out.println("*********************************");
        		System.out.println("Session: " + session);
        		System.out.println("Customer: " + session.getAttribute("email"));
        	}				

        	// the following few lines are for connection pooling
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            if (initCtx == null)
                System.out.println("initCtx is NULL");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                System.out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("fabflix/moviedb_write");
            
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
            
            String cc_id = request.getParameter ("cc_id") != null ?
            		request.getParameter ("cc_id") : "";
            String first_name = request.getParameter ("first_name") != null ?
            		request.getParameter ("first_name") : "";
            String last_name = request.getParameter ("last_name") != null ?
                    request.getParameter ("last_name") : "";	
            String expiration = request.getParameter ("expiration") != null ?
                    request.getParameter ("expiration") : "";
                    
            String query = "Select * from creditcards where "
            		+ "id = '" + cc_id + "' and "
            		+ "first_name = '" + first_name + "' and "
            		+ "last_name = '" + last_name + "' and "
            		+ "expiration = '" + expiration + "'";
            
            // use prepared statement
            if (Constants.PS) {
            	query = "select * from creditcards where "
            			+ "id = ? and "
            			+ "first_name = ? and "
            			+ "last_name = ? and "
            			+ "expiration = ?";
            			
            	ps = conn.prepareStatement(query);
            	ps.setInt(1, Integer.parseInt(cc_id));
            	ps.setString(2, first_name);
            	ps.setString(3, last_name);
            	ps.setDate(4, parseDate(expiration));
            	
            	rs = ps.executeQuery();
            }
            // use regular statement
            else {
            	rs = statement.executeQuery(query);
            }
            
            HttpSession session = request.getSession (true);
            ArrayList<CartItem> cart = ((SessionCart)session.getAttribute ("sessionCart")).getCart();
            
            if (rs.next())
            {
            	for (CartItem c : cart)
            	{
	            	String query2 = "insert into sales"
	            			+ " (customer_id, movie_id, sale_date)"
	            			+ " values (?, ?, ?)";
	            	PreparedStatement newSale = conn.prepareStatement(query2);
	            	
	            	newSale.setInt(1, ((Customer)session.getAttribute("currentUser")).getID());
	            	
	            	Statement statement2 = conn.createStatement();
	            	newSale.setInt(2, findMovieID(statement2, c.getMovie().getTitle()));
	            	
	            	Date date = new Date(Calendar.getInstance().getTimeInMillis());
	            	newSale.setDate(3, date);
	            	
	            	/*
	            	System.out.println("Date: " + date);
	            	System.out.println("User ID: " + ((Customer)session.getAttribute("currentUser")).getID());
	            	System.out.println("Movie ID: " + findMovieID(statement2, c.getMovie().getTitle()));
	            	*/
	            	
	            	newSale.executeUpdate();
	            	
	            	if (newSale != null)
	            		newSale.close();
            	}
            	
            	response.sendRedirect("ConfirmationPage.jsp");
            }
            // unsuccessful purchase
            else {
            	response.sendRedirect("Checkout.jsp");
            }
            
            // close resources
            if (conn != null)
            	conn.close();
            if (statement != null)
            	statement.close();
            if (rs != null)
            	rs.close();
            if (ps != null)
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
	
	public void doPost (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{   
		doGet(request, response);
	}
	
	// find ID of purchased movie
	private int findMovieID (Statement statement, String title)
	{
		try
		{
			String query = "Select * from movies where title = '" + title + "'";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			return rs.getInt(1);
		}
		catch (SQLException ex) 
        {
            while (ex != null) 
            {
                System.out.println ("SQL Exception:  " + ex.getMessage ());
                ex = ex.getNextException ();
            }
        } // end catch SQLException
		return -1;
	}
	
	// converts string to sql date
	private java.sql.Date parseDate(String dateString) 
			throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date;
		date = sdf.parse(dateString);
		System.out.println("Date: " + date);

		return new java.sql.Date(date.getTime());

	}
}


