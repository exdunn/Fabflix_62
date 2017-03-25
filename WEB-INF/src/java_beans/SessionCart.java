package java_beans;

import java.util.ArrayList;
import java.util.HashMap;

import java_beans.CartItem;
import java_beans.Movie;


public class SessionCart {

	private HashMap<Integer, CartItem> cart;
	
	public SessionCart ()
	{
		cart = new HashMap<Integer,CartItem> ();
	}
	
	public void addItem (Movie m, int q)
	{
		if (cart.containsKey (m.getId ()))
		{
			cart.get (m.getId ()).addQuantity (q);
		}
		else
		{
			cart.put (m.getId (), new CartItem (m, q));
		}
	}
	
	public void removeItem (Movie m, int q)
	{
		if (cart.containsKey (m.getId ()))
		{
			cart.remove (m.getId ());
		}
		else
		{
			// cannot find movie
			System.out.println("UNABLE TO FIND MOVIE WITH ID " + m.getId ());
		}
	}
	
	public void updateItem (Movie m, int q)
	{
		if (cart.containsKey (m.getId ()))
		{
			cart.get (m.getId ()). setQuantity (q);
		}
		else
		{
			// cannot find movie
			System.out.println("UNABLE TO FIND MOVIE WITH ID " + m.getId ());
		}
	}
	
	public void empty ()
	{
		cart.clear ();
	}
	
	public ArrayList<CartItem> getCart ()
	{
		return new ArrayList<CartItem> (cart.values());
	}
}