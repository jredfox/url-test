package jredfox.urltest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class ProxyURLStreamHandler extends URLStreamHandler {
	
	public static Method openCon;
	public static Method openConP;
	static
	{
		try
		{
			openCon = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[]{URL.class});
			openCon.setAccessible(true);
			openConP = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[]{URL.class, Proxy.class});
			openConP.setAccessible(true);
		}
		catch(Throwable t)
		{
			System.err.println("proxy URL reflection failed");
			t.printStackTrace();
		}
	}

	public ProxyURLStreamHandler(String p) 
	{
		
	}
	
	@Override
	protected URLConnection openConnection(URL u, Proxy p) throws IOException
	{
		u = ProxyRegistry.proxify(u);
        try 
        {
            String clsName = "sun.net.www.protocol." + u.getProtocol() +".Handler";
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
                return (URLConnection) openConP.invoke(sunImpl, u, p);
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException 
	{
		u = ProxyRegistry.proxify(u);
        try 
        {
            String clsName = "sun.net.www.protocol." + u.getProtocol() +".Handler";
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
                return (URLConnection) openCon.invoke(sunImpl, u);
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
	}

}
