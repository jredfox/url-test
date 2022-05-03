package jredfox.urltest;

import java.net.MalformedURLException;

public interface IProxy<T> {
	
	public T getRedirect(T old);
}
