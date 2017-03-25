package java_beans;

public class Customer
{
	private int _id;
	private String _first_name;
	private String _last_name;
	private String _cc_id;
	private String _address;
	private String _email;
	private String _password;
	
	public Customer ()
	{
		_id = 0;
		_first_name = "";
		_last_name = "";
		_cc_id = "";
		_address = "";
		_email = "";
		_password = "";
	}
	
	// **************SET FUNCTIONS**************
	
	public void setID (int id)
	{
		_id = id;
	}
	
	public void setFirstName (String fname)
	{
		_first_name = fname;
	}
	
	public void setLastName (String lname)
	{
		_last_name = lname;
	}
	
	public void setCCID (String ccid)
	{
		_cc_id = ccid;
	}
	
	public void setAddress (String addr)
	{
		_address = addr;
	}
	
	public void setEmail (String email)
	{
		_email = email;
	}
	
	public void setPassword (String pass)
	{
		_password = pass;
	}
	
	// **************Get FUNCTIONS**************
	
	public int getID ()
	{
		return _id;
	}
	
	public String getFirstName ()
	{
		return _first_name;
	}
	
	public String getLastName ()
	{
		return _last_name;
	}
	
	public String getCCID ()
	{
		return _cc_id;
	}
	
	public String getAddress ()
	{
		return _address;
	}
	
	public String getEmail ()
	{
		return _email;
	}
	
	public String getPassword ()
	{
		return _password;
	}
}