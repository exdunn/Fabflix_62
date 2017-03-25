package java_beans;

public class Employee
{
	private String password;
	private String email;
	private String fullName;

	public Employee ()
	{
		email = "";
		password = "";
		fullName = "";
	}
	
	public void setEmail (String em)
	{
		email = em;
	}
	
	public void setPass (String pass)
	{
		password = pass;
	}
	
	public void setName (String name)
	{
		fullName = name;
	}
	
	public String getEmail ()
	{
		return email;
	}
	
	public String getPass ()
	{
		return password;
	}
	
	public String getFullName ()
	{
		return fullName;
	}
}