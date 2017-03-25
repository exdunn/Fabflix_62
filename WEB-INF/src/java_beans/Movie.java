package java_beans;

import java.sql.*;
import java.util.ArrayList;

public class Movie 
{
	private int id;
	private String title;
	private int year;
	private String director;
	private String banner_url;
	private String trailer_url;
	
	private ArrayList<String> genres;
	private ArrayList<Star> stars;
	
	public Movie ()
	{
		id = 0;
		title = "";
		year = 0;
		director = "";
		banner_url = "";
		trailer_url = "";
		
		genres = new ArrayList<String> ();
		stars = new ArrayList<Star> ();
	}
	
	public Movie(int id, String title, int year, String director, 
			String banner_url, String trailer_url)
	{
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.banner_url = banner_url;
		this.trailer_url = trailer_url;
		
		genres = new ArrayList<String> ();
		stars = new ArrayList<Star> ();
	}
	
	public void insertGenres (Statement statement)
	{
		String query = "select name from genres where id in "
				+ "(select genre_id from genres_in_movies "
				+ "where movie_id = '" + id + "')";
		try
		{
			ResultSet rs = statement.executeQuery(query);
			while (rs.next ())
			{
				addGenre (rs.getString ("name"));
			}
		} 
		catch (SQLException ex) 
		{
            while (ex != null) 
            {
                System.out.println("SQL Exception:  " + ex.getMessage ());
                ex = ex.getNextException ();
            }
        } // end catch SQLException
	}
	
	public void insertStars (Statement statement)
	{
		String query = "select * from stars where id in "
				+ "(select star_id from stars_in_movies "
				+ "where movie_id = '" + id + "')";
		try
		{
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				Star newStar = new Star ();
				newStar.setId (rs.getInt("id"));
				newStar.setFirstName (rs.getString("first_name"));
				newStar.setLastName (rs.getString("last_name"));
				newStar.setDob (rs.getDate("dob"));
				newStar.setPhotoURL (rs.getString("photo_url"));
				addStar (newStar);
			}
		} 
		catch (SQLException ex) 
		{
            while (ex != null) 
            {
                System.out.println("SQL Exception:  " + ex.getMessage ());
                ex = ex.getNextException ();
            }
        } // end catch SQLException
	}
	
	// **************SET FUNCTIONS**************
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}
	
	public void setDirector(String director)
	{
		this.director = director;
	}
	
	public void setBannerURL(String banner_url)
	{
		this.banner_url = banner_url;
	}
	
	public void setTrailerURL(String trailer_url)
	{
		this.trailer_url = trailer_url;
	}
	
	public void addGenre (String genre)
	{
		genres.add(genre);
	}
	
	public void addStar (Star star)
	{
		stars.add(star);
	}
	
	// **************GET FUNCTIONS**************
	
	public int getId()
	{
		return id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public int getYear()
	{
		return year;
	}
	
	public String getDirector()
	{
		return director;
	}
	
	public String getBanner_url ()
	{
		return banner_url;
	}
	
	public String getTrailer_url ()
	{
		return trailer_url;
	}
	
	public ArrayList<String> getGenres ()
	{
		return genres;
	}
	
	public ArrayList<Star> getStars ()
	{
		return stars;
	}
}