package fr.epita.iam.iamcore.tests;

import fr.epita.iam.datamodel.Identity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.epita.iam.services.JDBCIdentityDAO;
import junit.framework.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestJDBCDAO {

	
	@Inject
	JDBCIdentityDAO dao;
	
	@Inject
	DataSource ds;
	
	private static final Logger LOGGER = LogManager.getLogger(TestJDBCDAO.class);
	
	public static void globalSetup(DataSource source) throws SQLException {
		LOGGER.info("beginning the setup");
		Connection connection =  source.getConnection();
		/*
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
		*/
		LOGGER.info("setup finished : ready to proceed with the testcase");
		
	}
	
	@Before
	public void setUp() throws SQLException {
		LOGGER.info("Init before setup");
		globalSetup(ds);
	}
	
	@Test
	public void basicTest() throws SQLException {
		String displayName = "Karlos";
		dao.writeIdentity(new Identity(null, displayName, "karluv mail"));
		
		
		String validationSql = "select * from IDENTITIES where IDENTITIES_DISPLAYNAME=?";
		Connection connection = ds.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(validationSql);
		pstmt.setString(1, displayName);
		
		ResultSet rs = pstmt.executeQuery();
		List<String> displayNames = new ArrayList<String>();
		while(rs.next()){
			String foundDisplayName= rs.getString("IDENTITIES_DISPLAYNAME");
			displayNames.add(foundDisplayName);
		}
		
		Assert.assertEquals(1, displayNames.size());
		Assert.assertEquals(displayName, displayNames.get(0));
		
		pstmt.close();
		rs.close();
		connection.close();
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
