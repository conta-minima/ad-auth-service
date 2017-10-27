package repository;

import java.util.Properties;

import conf.PropertiesReader;
import dto.User;

public class AdRepostitory {
	
	public User doLogin(String username, String password) {
		
		
		
		return null;
	}
	
	public Properties readProperties() throws Exception {
		Properties p = PropertiesReader.getInstance().getProperties();
		return p;
	}

}
