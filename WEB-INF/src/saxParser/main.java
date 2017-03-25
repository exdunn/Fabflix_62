package saxParser;

import java.sql.*;
import java.util.*;
import javafx.util.Pair;

import globals.Constants;
import java_beans.*;

public class main {
    

    
    public static void main(String[] args) throws SQLException {
    		
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(Constants.LOGINURL, Constants.LOGINUSER,Constants.LOGINPASS);      

	        // Parse xml files for Lists of objects type Movie, Cast, and Star
	        saxParser movieList = new saxParser("../lib/mains243.xml");
	        saxParser castList = new saxParser("../lib/casts124.xml");
	        saxParser starsList = new saxParser("../lib/actors63.xml");
	        
	        movieList.runParser();
	        castList.runParser();
	        starsList.runParser();
	        
	        // Batch insert movieList Movie objects into movies table
	        Statement stmt = connection.createStatement();
	        
	        insertMovies(movieList, stmt);
	        insertStars(starsList, stmt);
	        insertGenres(movieList, stmt);
	        insertStarsInMovies(castList, stmt);
	        insertGenresInMovies(movieList, stmt);
	        
	        stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    // Batch inserts movies that do not already exist in the database
    public static void insertMovies(saxParser movieList, Statement statement) {
    	try {
	        HashSet<Pair<String,String>> dbMoviesSet = new HashSet<Pair<String,String>>();
	        
	        // Select movie titles from moviedb
	        String query = "SELECT id, title, director FROM movies";
	        ResultSet results = statement.executeQuery(query);
	        statement.clearBatch();
	        while (results.next()) {
	            dbMoviesSet.add(new Pair(results.getString("title"), results.getString("director")));
	        }
	        
	        // Insert into moviedb if there are no duplicates
	        Iterator it = movieList.elements.iterator();
	        int count = 0;
	        while (it.hasNext()) {
	            Movie tempMov = (Movie)it.next();
	            // Check for valid input and add to batch insert
	            if ((dbMoviesSet.isEmpty() || !dbMoviesSet.contains(new Pair(tempMov.getTitle(), tempMov.getDirector())))
	                && tempMov.getTitle().matches("[a-zA-Z.]+") 
	                && tempMov.getDirector() != null && tempMov.getDirector().matches("[a-zA-Z.]+")) {
	            	
	            	String batchQuery = "insert into movies (title, year, director) values ";
	            	
	            	batchQuery += " ('" + tempMov.getTitle() + "', " 
	                                          + tempMov.getYear() + ", '" 
	                                          + tempMov.getDirector() + "')";
	            	statement.addBatch(batchQuery);
	                ++count;
	            }
	        }
	        // execute batch insert
	        if (count != 0) {
	        	statement.executeBatch();
	        	statement.clearBatch();
	        }
    	} catch (SQLException ex){ 
	    	while (ex != null) 
	        {
	            System.out.println("SQL Exception:  " + ex.getMessage());
	            ex = ex.getNextException();
	        }
    	}
    	catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Batch inserts star names that do not already exist in the database
    public static void insertStars(saxParser starsList, Statement statement) throws SQLException 
    {
        HashSet<Pair<String, String>> xmlStarSet = new HashSet<Pair<String, String>>();
        HashSet<Pair<String, String>> dbStarSet = new HashSet<Pair<String, String>>();
        
        Iterator it = starsList.elements.iterator();
        while (it.hasNext()) 
        {
            Star tempStar = (Star)it.next();
            xmlStarSet.add(new Pair(tempStar.getFirst_name(), tempStar.getLast_name()));
        }
        // select first/last name from stars
        String query = "SELECT first_name, last_name FROM stars";
        ResultSet starNames = statement.executeQuery(query);
        statement.clearBatch();
        
        while (starNames.next()) 
            dbStarSet.add(new Pair(starNames.getString("first_name"), starNames.getString("last_name")));

        it = xmlStarSet.iterator();
        int count = 0;
        while (it.hasNext()) 
        {
            Pair<String,String> tempName = (Pair)it.next();
            
            if (!dbStarSet.contains(tempName)) 
            {
                if ((tempName.getKey().equals("") || tempName.getKey().matches("[a-zA-Z.]+")) &&
                tempName.getValue().i matches("[a-zA-Z.]+")) {
                	String batchQuery = "INSERT INTO stars (first_name, last_name) VALUES ";
	                batchQuery += " ('" + tempName.getKey() + "', '" 
	                                          + tempName.getValue()+ "')";
                	statement.addBatch(batchQuery);
                	
	                ++count;
                }
            }
        }
        
        if (count != 0) 
        {
            statement.executeBatch();
            statement.clearBatch();
        }
    }
    
    // Batch inserts genre names that do not already exist in the database
    public static void insertGenres(saxParser movieList, Statement statement) throws SQLException
    {
        HashSet<String> xmlGenreSet = new HashSet<String>();
        HashSet<String> dbgenreSet = new HashSet<String>();
        
        Iterator it = movieList.elements.iterator();
        while (it.hasNext()) 
        {
            Movie tempMov = (Movie)it.next();
            ArrayList<String> genres = tempMov.getGenres();
            Iterator itGenres = genres.iterator();
            while (itGenres.hasNext()) 
            {
                String tempGen = (String)itGenres.next();
                xmlGenreSet.add(tempGen);
            }   
        }
        if (xmlGenreSet.contains(null)) 
            xmlGenreSet.remove(null);
        
        String query = "SELECT name FROM genres";
        ResultSet results = statement.executeQuery(query);
        statement.clearBatch();
        while (results.next()) 
        {
            dbgenreSet.add(results.getString("name"));
        }
        
        it = xmlGenreSet.iterator();
        int count = 0;
        while (it.hasNext()) 
        {
            String name = (String)it.next();
            if (!dbgenreSet.contains(name)) 
            {
            	String batchQuery = "INSERT INTO genres (name) VALUES ";
                batchQuery += " ('" + name + "')";
                statement.addBatch(batchQuery);
                
                ++count;
            }
        }
        if (count != 0) 
        {
            statement.executeBatch();
            statement.clearBatch();
        }
    }
    
    // batch insert into genres_in_movies
    public static void insertGenresInMovies(saxParser movieList, Statement statement) throws SQLException 
    {
        HashMap<String, Integer> genresMap = new HashMap<String, Integer>();
        HashMap<Pair<String, String>, Integer> moviesMap = new HashMap<Pair<String, String>, Integer>();
        HashSet<Pair<Integer, Integer>> checkID = new HashSet<Pair<Integer, Integer>>();
        
        String query = "SELECT id, name FROM genres";
        ResultSet results = statement.executeQuery(query);
        statement.clearBatch();
        
        while (results.next()) 
            genresMap.put(results.getString("name"), results.getInt("id"));
        
        query = "SELECT id, title, director FROM movies";
        results = statement.executeQuery(query);
        statement.clearBatch();
        
        while (results.next())
            moviesMap.put(new Pair(results.getString("title"), results.getString("director")), results.getInt("id"));
        
        query = "SELECT genre_id, movie_id from genres_in_movies";
        results = statement.executeQuery(query);
        statement.clearBatch();
        
        while (results.next())
            checkID.add(new Pair(results.getInt("genre_id"), results.getInt("movie_id")));    
        
        Iterator it = movieList.elements.iterator();
        int count = 0;
        while (it.hasNext())
        {	
        	String batchQuery = "INSERT IGNORE INTO genres_in_movies (genre_id, movie_id) VALUES ";
        	
            Movie tempMov = (Movie) it.next();
            String title = tempMov.getTitle();
            String director = tempMov.getDirector();
            ArrayList<String> genreList = tempMov.getGenres();
            Iterator itGenre = genreList.iterator();
            while (itGenre.hasNext()) 
            {
                String tempGenre = (String) itGenre.next();
                if (moviesMap.get(new Pair(title, director)) != null && genresMap.get(tempGenre) != null
                        && (checkID.isEmpty() || !checkID.contains(new Pair(genresMap.get(tempGenre), moviesMap.get(new Pair(title, director)))))) 
                {
                    batchQuery += " ('" + genresMap.get(tempGenre) + "','"
                                              + moviesMap.get(new Pair(title, director)) + "')";
                    ++count;
                }
            }
        }
        
        if (count != 0) 
        {
            statement.executeBatch();
            statement.clearBatch();
        }
        
    }
    
    // batch insert into stars_in_movies
    public static void insertStarsInMovies(saxParser castList, Statement statement) throws SQLException 
    {
        HashSet<Pair<Integer, Integer>> checkID = new HashSet<Pair<Integer, Integer>>();
        HashMap<Pair<String, String>, Integer> starNamesToID = new HashMap<Pair<String, String>, Integer>();
        HashMap<Pair<String, String>, Integer> movieNamesToID = new HashMap<Pair<String, String>, Integer>();
        HashSet<cast> castSet = new HashSet<cast>();
        
        String query = "SELECT id, title, director FROM movies";
        ResultSet results = statement.executeQuery(query);
        statement.clearBatch();
        
        while (results.next())
            movieNamesToID.put(new Pair(results.getString("title"), results.getString("director")), results.getInt("id"));
        
        query = "SELECT id, first_name, last_name FROM stars";
        results = statement.executeQuery(query);
        statement.clearBatch();
        
        while (results.next())
            starNamesToID.put(new Pair(results.getString("first_name"), results.getString("last_name")), results.getInt("id"));
        
        query = "SELECT star_id, movie_id FROM stars_in_movies";
        results = statement.executeQuery(query);
        statement.clearBatch();
        
        while (results.next())
            checkID.add(new Pair(results.getString("star_id"), results.getString("movie_id")));
        
        Iterator it = castList.elements.iterator();
        while (it.hasNext())
            castSet.add((cast) it.next());
        
        it = castSet.iterator();
        int count = 0;
        while (it.hasNext()) 
        {
        	String batchQuery =  "INSERT IGNORE INTO stars_in_movies (star_id, movie_id) VALUES ";
            cast tempCast = (cast) it.next();
            if (movieNamesToID.containsKey(new Pair(tempCast.getMovie(), tempCast.getDirector())) 
                    && tempCast.getFirst_name() != null && starNamesToID.containsKey(new Pair(tempCast.getFirst_name(), tempCast.getLast_name()))
                    && !checkID.contains(new Pair(starNamesToID.get(new Pair(tempCast.getFirst_name(), tempCast.getLast_name())),movieNamesToID.get(new Pair(tempCast.getMovie(), tempCast.getDirector()))))) 
            {
                batchQuery += " ('" + starNamesToID.get(new Pair(tempCast.getFirst_name(), tempCast.getLast_name())) + "','"
                                          + movieNamesToID.get(new Pair(tempCast.getMovie(), tempCast.getDirector())) + "')";
                statement.addBatch(batchQuery);
                
                ++count;
            }
        }
        if (count != 0) 
        {
            statement.executeBatch();
            statement.clearBatch();
        }
    }
}