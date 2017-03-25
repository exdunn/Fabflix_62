package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java_beans.Customer;
import java_beans.SessionCart;

@WebServlet("/logout")

public class Logout extends HttpServlet {
	
	
	public Logout() 
    {
        super();
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{	 
        try 
        {
        	HttpSession session = request.getSession(false);
    		Customer customer = (Customer) session.getAttribute("currentUser");
    		
    		if (customer != null)
    		{
    			SessionCart cart = (SessionCart) session.getAttribute("sessionCart");
    			
    			session.setAttribute("currentUser", null);
    			session.setAttribute("connection", null);
    			session.setAttribute("sessionCart", null);
    		}
    		response.sendRedirect("login.jsp");
        }
        catch (java.lang.Exception ex) 
        {
            System.out.println ("java.lang.exception ex: " + ex);
            return;
        }
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
}