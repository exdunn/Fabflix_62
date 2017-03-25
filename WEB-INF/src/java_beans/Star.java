package java_beans;

import java.util.ArrayList;
import java.sql.Date;

public class Star 
{
	private int _id;
	private String _first_name;
	private String _last_name;
	private Date _dob;
	private String _photo_url;
	
	private ArrayList<Movie> _movies;
	
	public Star ()
	{
		_id = 0;
		_first_name = "";
		_last_name = "";
		_dob = null;
		_photo_url = "";
		_movies = new ArrayList<Movie> ();
	}
	
	// **************SET FUNCTIONS**************
	
	public void setId (int id)
	{
		_id = id;
	}
	
	public void setName (String name)
	{
		parse(name);
	}
	
	public void setFirstName (String fname)
	{
		_first_name = fname;
	}

	public void setLastName (String lname)
	{
		_last_name = lname;
	}
	
	public void setDob (Date dob)
	{
		_dob = dob;
	}
	
	public void setPhotoURL (String photo_url)
	{
		_photo_url = photo_url;
	}
	
	public void addMovie (Movie movie)
	{
		_movies.add(movie);
	}
	
	// **************GET FUNCTIONS**************
	
	public int getId ()
	{
		return _id;
	}
	
	public String getFirst_name ()
	{
		return _first_name;
	}
	
	public String getLast_name ()
	{
		return _last_name;
	}
	
	public Date getDob ()
	{
		return _dob;
	}
	
	public String getPhoto_url ()
	{
		return _photo_url;
	}
	
	public ArrayList<Movie> getMovies ()
	{
		return _movies;
	}
	
	// separate string into first and last name
	private void parse(String name) {
        String[] split = name.split("\\s+");
    
        if (split.length == 1) {
            _first_name = "";
            _last_name = split[0];
        
        } else if (split.length == 2) {
            _first_name = split[0];
            _last_name = split[1];
        
        } else {
            _first_name = split[0] + " ";
        
            int i;
            for (i = 1; i < split.length - 1; ++i) {
                _first_name += split[i] + " ";
            }           
            _last_name = split[i];
        }     
    }
}
















