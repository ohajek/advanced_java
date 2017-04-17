/**
 * 
 */
package fr.epita.iam.services;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.iamcore.tests.TestJDBCDAO;

/**
 * @author ohajek
 *
 */
public class JDBCIdentityDAOzaloha {

	
	@Inject
	@Named("dataSourceBean")
	private DataSource ds;
	
	private Connection connection;
	private static final Logger LOGGER = LogManager.getLogger(JDBCIdentityDAOzaloha.class);
	

	
	private JDBCIdentityDAOzaloha() throws SQLException {
	}
	
	/**
	 * Writes a new identity element into the database from given Identity object informations
	 * @param identity			Newly filled Identity object to write into the database
	 * @throws SQLException
	 */
	public void writeIdentity(Identity identity) throws SQLException {
		
		connection = ds.getConnection();
		String insertStatement = "insert into IDENTITIES (IDENTITIES_DISPLAYNAME, IDENTITIES_EMAIL, "
				+ "IDENTITIES_PASSWORD, IDENTITIES_PRIVILEGE) "
				+ "values(?, ?, ?, ?)";
		PreparedStatement pstmtInsert = connection.prepareStatement(insertStatement);
		pstmtInsert.setString(1, identity.getDisplayName());
		pstmtInsert.setString(2, identity.getEmail());
		pstmtInsert.setString(3, identity.getPassword());
		pstmtInsert.setString(4, identity.getPrivilege());
		
		pstmtInsert.execute();
	}
	
	/**
	 * Updates identity in the database based on the information from passed identity object
	 * @param identity		Newly created Identity object containing new information and UID of the identity to update
	 * @throws SQLException
	 */
	public void modifyIdentity(Identity identity) throws SQLException {
		String modifyStatement = "UPDATE IDENTITIES "
				+ "SET IDENTITIES_DISPLAYNAME=?,"
				+ "IDENTITIES_EMAIL=?,"
				+ "IDENTITIES_PASSWORD=?,"
				+ "IDENTITIES_PRIVILEGE=?"
				+ "WHERE IDENTITIES_UID=?";
		
		PreparedStatement pstmtModify = connection.prepareStatement(modifyStatement);
		pstmtModify.setString(1, identity.getDisplayName());
		pstmtModify.setString(2, identity.getEmail());
		pstmtModify.setString(3, identity.getPassword());
		pstmtModify.setString(4, identity.getPrivilege());
		pstmtModify.setString(5, identity.getUid());
		
		pstmtModify.execute();

	}
	
	/**
	 * Reads all identities from the database and returns them
	 * @return	List containing all identities in the database
	 * @throws SQLException
	 */
	public List<Identity> readAll() throws SQLException {
		List<Identity> identities = new ArrayList<Identity>();

		PreparedStatement pstmtSelect = connection.prepareStatement("select * from IDENTITIES");
		ResultSet rs = pstmtSelect.executeQuery();
		while (rs.next()) {
			String displayName = rs.getString("IDENTITIES_DISPLAYNAME");
			String uid = String.valueOf(rs.getString("IDENTITIES_UID"));
			String email = rs.getString("IDENTITIES_EMAIL");
			Identity identity = new Identity(uid, displayName, email);
			identities.add(identity);
		}
		return identities;
	}
	
	/**
	 * Deletes identity from the database based on the given UID
	 * @param identityID	UID of the identity in the databasse to be deleted
	 * @throws SQLException
	 */
	public void deleteIdentity(String identityID) throws SQLException {
		String deleteStatement = "DELETE FROM IDENTITIES WHERE IDENTITIES_UID = ?";
		
		PreparedStatement pstmtDelete = connection.prepareStatement(deleteStatement);
		pstmtDelete.setString(1, identityID);
		
		pstmtDelete.execute();
	}

	/**
	 * Checks for identity that contains given name and password and then checks, if it has admin privileges
	 * @param login			Name of the identity to check for authentication
	 * @param password		Password of the identity to check for authentication
	 * @return				True if such identity with given parameters is found in the database, otherwise returns false
	 * @throws SQLException
	 */
	public boolean authenticate(String login, String password) throws SQLException {
		String authenticateStatement = "select * from IDENTITIES where "
				+ "IDENTITIES_DISPLAYNAME=? and IDENTITIES_PASSWORD=? and IDENTITIES_PRIVILEGE='admin'";
		
		PreparedStatement pstmtAuthenticate = connection.prepareStatement(authenticateStatement);
		pstmtAuthenticate.setString(1, login);
		pstmtAuthenticate.setString(2, password);

		ResultSet rs = pstmtAuthenticate.executeQuery();
		if(rs.next()) {
			return true;
		}
		return false;
	}
}
