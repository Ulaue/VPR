package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides necessary methods for all database classes
 *
 * @author Daniel Hecker : 26.12.2017
 */
public class DatabaseAccess
{
	protected static Connection con = null;
	private final static String dbHost = "mysqlpb.pb.bib.de"; // host name
	private final static String dbName = "pbg2h16axxPartylize"; // database name
	private final static String dbUser = "pbg2h16axx"; // database user
	private final static String dbPass = "Pr0jekt"; // database password

	/**
	 * Ignore as long as u don't change it
	 */
	protected DatabaseAccess() {}

	/**
	 * @param nothing use any value, it's not used anyway
	 */
	private DatabaseAccess(int nothing) {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // JDBC driver
			con = DriverManager.getConnection(
					"jdbc:mysql://" + dbHost + "/" + dbName + "?" + "user=" + dbUser + "&password=" + dbPass);
		} catch (ClassNotFoundException e) {
			System.out.println("Treiber nicht gefunden");
		} catch (SQLException e) {
			System.out.println("Verbindung nicht moglich");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}

	/**
	 * Returns an instance of a Database connection
	 * @return
	 */
	protected static Connection getInstance() {
		if (con == null)
			new DatabaseAccess(0);
		return con;
	}

	/**
	 * Breaks connection to Database and sets connection null
	 */
	protected static void endConnection() {
		try {
			getInstance().close();
			con = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to get the available id for a given table.
	 *
	 * @author Tobias(14.11.2017)
	 * @author Team3, Viola (06.12.2017)
	 */
	public static int getHighestId(String tableName, boolean connect)
	{
		if(connect)
			con = getInstance();

		int id = 0;

		if (con != null) {
			Statement query;
			try {
				query = con.createStatement();

				String sql = "SELECT MAX(id) FROM " + tableName + ";";
				ResultSet result = query.executeQuery(sql);
				result.next();
				String idString = result.getString(1);
				id = Integer.parseInt(idString);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(connect)
			endConnection();

		return ++id;
	}
}















