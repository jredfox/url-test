package jredfox.urltest;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

public class ProxyURLStreamHandler extends URLStreamHandler {
	
	public static Map<String, URLStreamHandler> handlers = new HashMap<>();
//	public static Method openCon;
//	public static Method openConP;
//	static
//	{
//		try
//		{
//			openCon = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[]{URL.class});
//			openCon.setAccessible(true);
//			openConP = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[]{URL.class, Proxy.class});
//			openConP.setAccessible(true);
//		}
//		catch(Throwable t)
//		{
//			System.err.println("proxy URL reflection failed");
//			t.printStackTrace();
//		}
//	}

	public ProxyURLStreamHandler(String p) 
	{
		
	}
	
	@Override
	protected URLConnection openConnection(URL u, Proxy p) throws IOException
	{
		return this.openConnection(u);
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException 
	{
		u = ProxyRegistry.proxify(u);
        try 
        {
        	String protocol = u.getProtocol();
        	URLStreamHandler handler = this.getURLStreamHandler(protocol);
        	return new URL(protocol, u.getHost(), u.getPort(), u.getFile(), handler).openConnection();//This won't cause recursion cause a new fixed handler is set
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
	}
	
	public URLStreamHandler getURLStreamHandler(String protocol) throws Exception 
	{
		URLStreamHandler h = handlers.get(protocol);
		if(h == null)
		{
	        String clsName = "sun.net.www.protocol." + protocol +".Handler";
	        Class<?> cls = null;
	        try
	        {
	            cls = Class.forName(clsName);
	        } 
	        catch (ClassNotFoundException e)
	        {
	            ClassLoader cl = ClassLoader.getSystemClassLoader();
	            if (cl != null)
	            {
	                cls = cl.loadClass(clsName);
	            }
	        }
	        if (cls != null) 
	        {
	            URLStreamHandler sunImpl = ((URLStreamHandler)cls.newInstance());
	            handlers.put(protocol, sunImpl);
	            return sunImpl;
	        }
		}
		return h;
	}

}
