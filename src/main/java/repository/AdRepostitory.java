package repository;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import config.PropertiesReader;
import dto.User;

public class AdRepostitory {
	
	private static final String HOST = "host";
	private static final String USER_ATTR = "user-attribute-name";
	private static final String BASE_DN = "base-dn";
	private static final String BIND_DN = "bind-dn";
	
	private static final String AD_CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	public User doLogin(String username, String password) throws Exception {
		
		
		Properties p = this.readProperties();
		
		String adServer = String.format(p.getProperty(HOST));
		
		DirContext context = this.createContext(adServer, username, password);
		
		SearchResult result = this.searchUser( username, context, p );
		
		System.out.println(result);
		
		return null;
	}
	
	/**
	 * Search for an user on AD.
	 *
	 * @param username
	 *            {@link String} user login name.
	 * @param context
	 *            {@link DirContext} AD context.
	 * @param p
	 * 			{@link Properties} configuration properties 
	 * @return {@link SearchResult} search results
	 * @throws NamingException
	 *             If more than one user with the supplied account name was found
	 * @throws AuthenticationException
	 *             In case of no user found
	 */
	private SearchResult searchUser(String username, DirContext context, Properties p) throws NamingException {
		// Filter only users
		String searchFilter = String.format("(&(objectClass=user)(%s=%s))", p.getProperty(USER_ATTR), username);
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> results = context.search(p.getProperty(BASE_DN), searchFilter, searchControls);

		if (!results.hasMoreElements()) {
			throw new AuthenticationException("User not found.");
		}

		SearchResult searchResult = results.nextElement();
		if (results.hasMoreElements()) {
			throw new NamingException("More then one user with this account name found: '" + username + "' ("+ p.getProperty(USER_ATTR) +").");
		}
		return searchResult;
	}
	
	private DirContext createContext(String adServer, String username, String password) throws NamingException {
		Hashtable<String, String> environment = new Hashtable<String, String>(5);
		environment.put(Context.INITIAL_CONTEXT_FACTORY, AD_CTX_FACTORY);
		environment.put(Context.PROVIDER_URL, adServer);
		environment.put(Context.SECURITY_PRINCIPAL, username);
		environment.put(Context.SECURITY_CREDENTIALS, password);
		return new InitialDirContext(environment);
	}
	
	private Properties readProperties() throws Exception {
		Properties p = PropertiesReader.getInstance().getProperties();
		return p;
	}

}
