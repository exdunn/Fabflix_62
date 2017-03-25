package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

// fabflix classes
import java_beans.*;
import globals.Constants;

@WebServlet("/_dashboard")

public class EmployeeLogin extends HttpServlet {
	
	private HttpSession session;
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
            DataSource ds = (DataSource) envCtx.lookup("fabflix/moviedb_write");
            
            if (ds == null)
                System.out.println("ds is null.");

            Connection conn = ds.getConnection();
            
            if (conn == null)
                System.out.println("dbcon is null.");
			
			/*// Class.forName("org.gjt.mm.mysql.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	
			Connection conn = DriverManager.getConnection(Constants.LOGINURL,
					Constants.LOGINUSER, Constants.LOGINPASS);*/
	
			int option = request.getParameter("option") != null ? Integer
					.parseInt(request.getParameter("option")) : 0;
			switch (option) 
			{
			// if no option go to login page
			case 0:
				request.getRequestDispatcher("EmployeeLogin.jsp").forward(request,
						response);
				break;
			case 1:
				login(request, response, conn);
				break;
			case 2:
				getMetadata(request, response, conn);
				break;
			case 3:
				insertStar(request, conn);
				break;
			case 4:
				insertMovie(request, conn);
				break;
			default:
				break;
			}
			
			request.getRequestDispatcher ("Dashboard.jsp").forward (request, response);
			
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
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            }
        }
		catch (java.lang.Exception ex) 
        {
            System.out.println("MovieDB: Error " + ex.getMessage());
            return;
        }	
    }
	
	public void doPost (HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		doGet (request, response);
	}
	
	// login to gain administrative access to moviedb
	private void login (HttpServletRequest request, HttpServletResponse response, Connection conn)
	{
		try
	    {
			String email = request.getParameter ("email");
	        String password = request.getParameter ("password");
	        String captcha = request.getParameter ("g-recaptcha-response");
	        
	        ResultSet rs = null;
	        PreparedStatement ps = null;
	        Statement statement = null;
	        String query = "SELECT * from employees where email = '" + email 
	        		+ "' and password = '" + password + "'";

	        // use prepared statement
	        if (Constants.PS) {
	        	query = "select * from employees where email = ? and password = ?";
	        	ps = conn.prepareStatement(query);
	        	ps.setString(1, email);
	        	ps.setString(2, password);
	        	
	        	rs = ps.executeQuery();
	        }
	        // use regular statement
	        else {
	        	rs = statement.executeQuery(query);
	        }
	        
	        if (rs == null)
	        	System.out.println("Result set is empty");
	        
	        // Iterate through each row of rs
	        if (rs.next()) {
	            Employee emp = new Employee();
	            emp.setEmail (rs.getString("email"));
	            emp.setPass (rs.getString("password"));
	            emp.setName (rs.getString("fullName"));
	        		
	            // set session variables
	            session = request.getSession (true);
	            session.setAttribute ("employee", emp);
	            session.setAttribute ("connection", conn);
	            request.setAttribute("status", "Successfully logged in as EMPLOYEE");
	        }
	        else {
	        	request.setAttribute("status", "Username and Password did not match a valid Customer");
	        	request.getRequestDispatcher ("EmployeeLogin.jsp").forward (request, response);
	        }
	        // close resources
	        if (conn != null)
	        	conn.close();
	        
	        if (statement != null)
	        	statement.close();
	        
	        if (rs != null)
            	rs.close();
	        
	        if (ps != null)
	        	ps.close();
        }
        catch (SQLException ex) 
        {
            while (ex != null) 
            {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            }
        } 
        catch (java.lang.Exception ex) 
        {
            System.out.println("MovieDB: Error " + ex.getMessage());
            return;
        }
	}
	
	// insert star into moviedb
	private void insertStar (HttpServletRequest request, Connection conn)
	{
		// get parameters from jsp
		String first_name = request.getParameter("first_name") != null ?
				request.getParameter("first_name") : "";
		String last_name = request.getParameter("last_name") != null ?
				request.getParameter("last_name") : "";
		String dob_string = request.getParameter("dob") != null ?
				request.getParameter("dob") : null;
		String photo_url = request.getParameter("photo_url") != null ? 
				request.getParameter("photo_url") : "";
				
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dob = null;
		
		try {
			dob = new Date(sdf.parse(dob_string).getTime());
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
				
		try {
			// create the query to add new star
			String query = "insert into stars (first_name, last_name, dob, photo_url)" + " values (?, ?, ?, ?)";
			PreparedStatement newActor = conn.prepareStatement(query);
			newActor.setString(1, first_name);
			newActor.setString(2, last_name);
			newActor.setDate(3, dob);
			newActor.setString(4, photo_url);
			newActor.executeUpdate();
			
			// set status message to be displayed
			request.setAttribute("status", "Successfully added new star.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// insert movie into moviedb
	private void insertMovie (HttpServletRequest request, Connection conn)
	{		
		// get paramters from jsp
		String title = request.getParameter("title") != null ? request.getParameter("title") : "";
		String director = request.getParameter("director") != null ? request.getParameter("director") : "";
		String banner_url = request.getParameter("banner_url") != null ? request.getParameter("banner_url") : "";
		String trailer_url = request.getParameter("trailer_url") != null ? request.getParameter("trailer_url") : "";
		String genre = request.getParameter("genre") != null ? request.getParameter("genre") : "";
		String first_name = request.getParameter("first_name") != null ? request.getParameter("first_name") : "";
		String last_name = request.getParameter("last_name") != null ? request.getParameter("last_name") : "";
		
		int year = -1;
		if (request.getParameter("year") != null && request.getParameter("year").matches("^[0-9]+$"))
			year = Integer.parseInt(request.getParameter("year"));

		try {
			// create the query to call stored procedure add_movie
			String query = "call add_movie(?, ?, ?, ?, ?, ?, ?, ?)";
			CallableStatement cs = conn.prepareCall(query);
			
			cs.setString(1, title);
			cs.setInt(2, year);
			cs.setString(3, director);
			cs.setString(4, banner_url);
			cs.setString(5, trailer_url);
			cs.setString(6, genre);
			cs.setString(7, first_name);
			cs.setString(8, last_name);
			cs.execute();
			
			// get result set which contains status information
			ResultSet rs = cs.getResultSet ();
			if (rs.next())
			{
				int returnValue = rs.getInt(1);
				switch(returnValue)
				{
				case 1:
					request.setAttribute("status", "Successfully added movie");
					break;
				case 2:
					request.setAttribute("status", "Record successfully updated");
					break;
				case -1:
					request.setAttribute("status", "Duplicate movie was not added");
					break;
				case -2:
					request.setAttribute("status", "Movie not added... Title, Year, and Director must have values");
					break;
				case -3:
					request.setAttribute("status", "Movie not added... Year must be a number");
					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// gets table metadata from moviedb
	private void getMetadata (HttpServletRequest request, HttpServletResponse response, Connection c)
	{
		String table[] = { "TABLE" };
		ResultSet rs = null;
		ArrayList<Table> tables = new ArrayList<Table>();
		
		try {
			DatabaseMetaData metadata = c.getMetaData();
			rs = metadata.getTables(null, null, null, table);
			
			while(rs.next())
			{
				Table newTable = new Table();
				newTable.setName(rs.getString("TABLE_NAME"));
				
				ResultSet rs2 = metadata.getColumns(null, null, newTable.getName(), null);
				
				while (rs2.next())
				{
					newTable.addColumn(rs2.getString("COLUMN_NAME"), rs2.getString("TYPE_NAME"));
				}
				tables.add(newTable);
			}
			request.setAttribute("tables", tables);
			request.getRequestDispatcher ("Metadata.jsp").forward (request, response);
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
		catch (java.lang.Exception ex) 
        {
            System.out.println("MovieDB: Error " + ex.getMessage());
            return;
        }
	}
}