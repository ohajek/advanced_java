/**
 * 
 */
package fr.epita.iam.iamcore.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.JDBCIdentityDAO;;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestSpring {
	
	@Inject
	JDBCIdentityDAO dao;	
	
	private static final Logger LOGGER = LogManager.getLogger(TestSpring.class);
	

	
	
	@BeforeClass
	public static void globalSetup() throws SQLException{
		LOGGER.info("beginning the setup");
		Connection connection = getConnection();
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
	
	/**
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:derby:memory:IAM_DB;create=true", "admin", "admin");
		return connection;
	}
	
	
	
	@Test
	public void testSpringContext() throws SQLException{
		dao.writeIdentity(new Identity(null, "Karel", "karel"));
	}

}