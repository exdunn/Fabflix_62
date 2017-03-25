package java_beans;

public class CartItem {
	
	private Movie movie;
	private int quantity;
	
	public CartItem (Movie m, int q)
	{
		movie = m;
		quantity = q;
	}
	
	public void setQuantity (int q)
	{
		quantity = q;
	}
	
	public void addQuantity (int q)
	{
		quantity += q;
	}
	
	public void removeQuantity (int q)
	{
		quantity = quantity - q >= 0 ? quantity - q : 0;
	}
	
	public void setMovie (Movie m)
	{
		movie = m;
	}
	
	public Movie getMovie ()
	{
		return movie;
	}
	
	public int getMovieId ()
	{
		return movie.getId();
	}
	
	public int getQuantity ()
	{
		return quantity;
	}
}
