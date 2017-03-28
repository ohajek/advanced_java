package fr.epita.iam.iamcore.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import fr.epita.iam.services.Configuration;



public class TestProperties {

	private static final Logger LOGGER = LogManager.getLogger(TestProperties.class);
	
	@Test
	public void testIO() throws FileNotFoundException, IOException {
		Properties props = new Properties();
		String filename = System.getProperty("fr.epita.iam.confFilePath");
		
		props.load(new FileInputStream(new File(filename)));
		
		LOGGER.info(props.getProperty("jdbc.connection.password"));
		
		//Configuration conf = Configuration.getInstance();
		//LOGGER.info(conf.getPassword());
	}
}