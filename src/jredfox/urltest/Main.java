package jredfox.urltest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.omg.CORBA_2_3.portable.InputStream;

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
						old = new URL("http", "www.betacraft.uk", 80, old.getFile(), null);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
//				System.out.println("firing:" + old.getHost());
				return old;
			}
		});
		try 
		{
			URL url = new URL("http://www.minecraft.net/skin/jredfox.png");
			URLConnection con = url.openConnection();
			int size = 0;
			java.io.InputStream s = con.getInputStream();
			while(s.read() != -1)
			{
				size++;
			}
			System.out.println("done:" + url + " lastModified:" + con.getLastModified() + " size:" + size);
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
