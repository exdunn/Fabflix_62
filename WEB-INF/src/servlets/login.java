package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

import java_beans.Customer;
import java_beans.SessionCart;
import globals.Constants;

@WebServlet("/login")

public class login extends HttpServlet {
	
	private final static Logger LOGGER = Logger.getLogger(login.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
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
        	
            //Class.forName("org.gjt.mm.mysql.Driver");
            /*Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection conn = DriverManager.getConnection(Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);*/
            
            // Declare database variables
            Statement statement = conn.createStatement();
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            String email = request.getParameter ("email");
            String password = request.getParameter ("password");
            
            
            String query = "SELECT * from customers where email = '" + email 
            		+ "' and password = '" + password + "'";
                        
            // use prepared statements
            if (Constants.PS)
            {
            	query = "select * from customers where email = ? and password = ?";
            	ps = conn.prepareStatement(query);
            	ps.setString(1, email);
            	ps.setString(2, password);
            	rs = ps.executeQuery();
            }
            // don't use prepared statements
            else 
            {
            	rs = statement.executeQuery(query);
            }

            // Iterate through each row of rs
            if (rs.next()) 
            {
            	System.out.println("**Successful Login**");
            	
                String passwordCheck = rs.getString("password");
                
                // create customer object
                Customer customer = new Customer ();    
                customer.setID (rs.getInt("id"));
                customer.setFirstName (rs.getString("first_name"));
            	customer.setLastName (rs.getString("last_name"));
            	customer.setCCID (rs.getString("cc_id"));
            	customer.setEmail (rs.getString("email"));
            	customer.setAddress (rs.getString("address"));
            	customer.setPassword (rs.getString("password"));
            		
            	// create the session
                HttpSession session = request.getSession (true);
                session.setAttribute ("currentUser", customer);
                session.setAttribute ("connection", conn);
                session.setAttribute ("sessionCart", new SessionCart());
                session.setAttribute ("email", email);
                
                // send to main page
                response.sendRedirect("mainpage");             
            }
            else
            {
            	System.out.println("**Could Not Login**");
            	
            	// go back to login page
            	request.setAttribute("status", "Username and Password did not match a valid Customer");
            	request.getRequestDispatcher ("login.jsp").forward (request, response);
            }
            // close connection
            if (conn != null){
        		conn.close();
        	}
        	// close prepared statement
        	if (ps != null) {
        		ps.close();
        	}
        	if (statement != null) {
        		statement.close();
        	}
        	if (rs != null)
            	rs.close();
        	
        	// performance measurements
        	long endTime = System.nanoTime();
        	long elapsedTime = (endTime - startTime) / 1000000;
        	if (Constants.LOG) {
        		LOGGER.info("Elapsed Time: " + elapsedTime + "ms");
        	}
        } 
        catch (SQLException ex) {
            while (ex != null) {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            }
        } // end catch SQLException
        catch (java.lang.Exception ex) {
            System.out.println("MovieDB: Error " + ex.getMessage());
            return;
        }
    }
	
	public void doPost (HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		doGet (request, response);
	}
}