package jredfox.urltest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

	public static void main(String[] args) throws IOException
	{
		URL.setURLStreamHandlerFactory(new URLFactory());
		ProxyRegistry.urlProxies.add(new IProxy<URL>() 
		{
			@Override
			public URL getRedirect(URL old)
			{
				try 
				{
					if (old.getHost().equals("www.minecraft.net"))
						old = new URL("https", "www.betacraft.uk", 80, old.getFile(), null);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
				System.out.println("firing:" + old.getHost());
				return old;
			}
		});
		try 
		{
			URL url = new URL("http://minecraft.net");
			URLConnection con = url.openConnection();
			con.getLastModified();
			System.out.println("done:" + url);
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
