package fr.epita.iam.iamcore.tests;

import fr.epita.iam.datamodel.Identity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.epita.iam.services.JDBCIdentityDAO;


public class TestJDBCDAO {

	private static final Logger LOGGER = LogManager.getLogger(TestJDBCDAO.class);
	
	@BeforeClass
	public static void globalSetup() throws SQLException {
		LOGGER.info("beginning the setup");
		Connection connection = DriverManager.getConnection("jdbc:derby:memory:IAM_DB;create=true", "admin", "admin");
		PreparedStatement pstmt = connection.prepareStatement("CREATE TABLE IDENTITIES " 
	    + " (IDENTITIES_UID INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT IDENTITIES_PK PRIMARY KEY, " 
	    + " IDENTITIES_DISPLAYNAME VARCHAR(255), "
	    + " IDENTITIES_EMAIL VARCHAR(255), "
	    + " IDENTITIES_PASSWORD VARCHAR(255), "
	    + " IDENTITIES_PRIVILEGE VARCHAR(255) "
	    + " )");

		pstmt.execute();
		connection.commit();
		pstmt.close();
		connection.close();
		LOGGER.info("setup finished : ready to proceed with the testcase");
		
	}
	
	@Before
	public void setUp() {
		LOGGER.info("Init before setup");
	}
	
	@Test
	public void basicTest() throws SQLException {
		JDBCIdentityDAO dao = new JDBCIdentityDAO();
		dao.writeIdentity(new Identity(null, "Karel", "karel"));
	}
	
	@After
	public void tearDown() {
		LOGGER.info("AFTER TEST CLEANUP");
	}
	
	@AfterClass()
	public static void globalTearDown(){
		LOGGER.info("global cleanup");
	}
}
