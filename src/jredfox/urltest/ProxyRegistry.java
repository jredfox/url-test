package jredfox.urltest;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProxyRegistry {
	/**
	 * TODO:
	 *  --proxy.forceHTTPS this flag will set all urls to use https instead of http
	 *  --proxy.removeWWW this flag will remove all www. from domains
		--proxy.map=myProxyMap.txt list of urls change what to what. must contain the protocol and domain names. if the first url contains a path it will be a url to url replacement and will take priority over domain proxy changes
	 */
	public static List<IProxy<URL>> urlProxies = new ArrayList();
	public static List<IProxy<File>> fileProxies = new ArrayList();
	public static List<IProxy<Path>> pathProxies = new ArrayList();
	
	public static URL proxify(URL u)
	{
		return proxify(urlProxies, u);
	}
	
	public static File proxify(File o)
	{
		return proxify(fileProxies, o);
	}
	
	public static Path proxify(Path o)
	{
		return proxify(pathProxies, o);
	}

	protected static <T> T proxify(List<IProxy<T>> plist, T old)
	{
		T p = old;
		for(IProxy<T> proxy : plist)
			p = proxy.getRedirect(p);
		return p;
	}

}
