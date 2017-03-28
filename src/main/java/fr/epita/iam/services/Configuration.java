package fr.epita.iam.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {

	
	private static Configuration instance;
	/*private String jdbcConnectionString;
	private String userName;
	private String password;*/
	private static final Logger LOGGER = LogManager.getLogger(JDBCIdentityDAO.class);
	private Properties properties = new Properties();
	
	private Configuration() throws FileNotFoundException, IOException  {
		//initialize properties
		String filename = System.getProperty("fr.epita.iam.confFilePath");
		properties.load(new FileInputStream(new File(filename)));
		LOGGER.debug("Loaded properties: {}", properties);
	}
	
	public static Configuration getInstance() throws FileNotFoundException, IOException {
		if(instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public String getJdbcConnectionString() {
		return properties.getProperty("jdbc.connection.string");
	}

	public String getUserName() {
		return properties.getProperty("jdbc.connection.user");
	}

	public String getPassword() {
		return properties.getProperty("jdbc.connection.password");
	}
}
