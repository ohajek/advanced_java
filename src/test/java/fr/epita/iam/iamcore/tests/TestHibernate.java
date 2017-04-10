package fr.epita.iam.iamcore.tests;

import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.JDBCIdentityDAO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestHibernate {

	@Inject
	SessionFactory sFactory;

	@Inject
	JDBCIdentityDAO dao;
	
	private static final Logger LOGGER = LogManager.getLogger(JDBCIdentityDAO.class);
	
	@Test
	public void testHibernate() throws SQLException {
		Session session = sFactory.openSession();
		Identity identity = new Identity("", "karel", "karel");
		session.saveOrUpdate(identity);
		
		//LOGGER.info(jdbcDao.readAll());
	}
}