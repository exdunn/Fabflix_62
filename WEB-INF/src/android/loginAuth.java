package android;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.Context;

import java_beans.Customer;
import java_beans.SessionCart;
import globals.Constants;

@WebServlet("/loginauth")

public class loginAuth extends HttpServlet {
	
	
	public void init(ServletConfig config)
			throws ServletException
	{
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{	 
		PrintWriter out = response.getWriter();
		String isRedirect = request.getParameter("redirect");
		String redirectURL;
		
		response.setContentType("text/plain");
	    response.setCharacterEncoding("utf-8");
		
        try 
        {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection conn = DriverManager.getConnection(Constants.LOGINURL, Constants.LOGINUSER, Constants.LOGINPASS);
            // Declare our statement
            Statement statement = conn.createStatement();
            String email = request.getParameter ("email");
            String password = request.getParameter ("password");
            
            
            
            String query = "SELECT * from customers where email = '" + email 
            		+ "' and password = '" + password + "'";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            
            // Iterate through each row of rs
            if (rs.next()) 
            {
                out.print("success");
            }
            else
            {
            	out.print("fail");
            }
        } 
        catch (SQLException ex) 
        {
            while (ex != null) 
            {
            	out.print("fail");
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            }
        } // end catch SQLException
        catch (java.lang.Exception ex) 
        {
        	out.print("fail");
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