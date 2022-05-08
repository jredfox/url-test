package jredfox.urltest;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class URLFactory implements URLStreamHandlerFactory {

	@Override
	public URLStreamHandler createURLStreamHandler(String p) 
	{
		return new ProxyURLStreamHandler(p);
	}

}
