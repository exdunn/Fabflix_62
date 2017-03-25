package manager;

import java.io.IOException;
import java.sql.*;

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
import globals.Constants;

@WebServlet ("/cartmanager")

public class CartManager extends HttpServlet
{	
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
            Connection conn = DriverManager.getConnection (Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);*/
            
            Statement statement = conn.createStatement ();
            
            HttpSession session = request.getSession (false);
            SessionCart cart = (SessionCart) session.getAttribute ("sessionCart");
            
            // print information about current session
        	if (Constants.PRINTSESSION) {
        		session = request.getSession();
        		Customer c = (Customer) session.getAttribute("currentUser");
        		System.out.println("*********************************");
        		System.out.println("Session: " + session);
        		System.out.println("Customer: " + session.getAttribute("email"));
        	}

            // 0 = update, 1 = remove, 2 = empty, 3 = add
            int cartRequest = Integer.parseInt (request.getParameter ("request"));
            String movie_id = request.getParameter("movie_id");
            
            int quantity = request.getParameter("quantity") != null ? 
            		Integer.parseInt(request.getParameter("quantity")) : 1;
            		
            System.out.println("movie_id: " + movie_id);		
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
            
            switch (cartRequest)
            {
            	case 0:
            		cart.updateItem (movie, quantity);
            		break;
            	case 1:
            		cart.removeItem (movie, quantity);
            		break;
            	case 2:
            		cart.empty ();
            		break;
            	case 3:
            		cart.addItem (movie, quantity);
            		break;
            	default:
            		break;
            }
            
            session.setAttribute("sessionCart", cart);
            response.sendRedirect ("ShoppingCart.jsp");
            
            if (conn != null && !conn.isClosed ())
            	conn.close();
            
            // performance measurements
        	long endTime = System.nanoTime();
        	long elapsedTime = endTime - startTime;
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
}


