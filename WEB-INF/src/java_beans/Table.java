package java_beans;

import java.util.ArrayList;

public class Table
{
	private String name;
	private ArrayList<String> columns;

	public Table ()
	{
		name = "";
		columns = new ArrayList<String> ();
	}
	
	public void setName (String name)
	{
		this.name = name;
	}
	
	public void addColumn (String name, String type)
	{
		columns.add(name + " (" + type + ")");
	}
	
	public String getName ()
	{
		return name;
	}
	
	public ArrayList<String> getColumns ()
	{
		return columns;
	}
}