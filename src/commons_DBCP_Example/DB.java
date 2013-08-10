package commons_DBCP_Example;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * 这个Enum类对数据库进行先关的操作 之所以为enum而不是utility final class，是因为他需要初始化，共享资源
 * 
 * @author Administrator
 * 
 */
public enum DB {
	INST;
	private final String URL = "jdbc:hsqldb:file:testdb;user=sa";
	private final String DRIVER = "org.hsqldb.jdbcDriver";

	DB() {
		if (conn == null) {
			try {
				Class.forName(DRIVER);
				conn = DriverManager.getConnection(URL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Connection conn = null; //our connnection to the db - presist for life of program

	public void shutdown() {
		Msg.debugMsg(DB.class, "Database is shutting down");
		Statement st;
		try {
			st = conn.createStatement();
			st.execute("SHUTDOWN");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} // if there are no other open connection
			}
		}
	}

	public boolean queryOneConnection(String sql, boolean showResult) throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		boolean hasContent = false;
		try {
			//conn = DriverManager.getConnection(URL);
			st = conn.createStatement();
			//Msg.debugMsg(DB.class, "executing query: " + sql);
			rs = st.executeQuery(sql);
			hasContent = !rs.isBeforeFirst();
			if (showResult) {
				showResultSetContent(rs);
			}
			return hasContent;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				//st.execute("SHUTDOWN");
				st.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return showResult;

	}
	
	//use for SQL command SELECT
	public boolean query(String sql, boolean showResult) throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		boolean hasContent = false;
		try {
			st = conn.createStatement();
			//Msg.debugMsg(DB.class, "executing query: " + sql);
			rs = st.executeQuery(sql);
			hasContent = !rs.isBeforeFirst();
			if (showResult) {
				showResultSetContent(rs);
			}
			return hasContent;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				st.execute("SHUTDOWN");
				st.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return showResult;

	}

	//use for SQL commands CREATE, DROP, INSERT and UPDATE
	public void update(String expression) throws SQLException {
		Statement st = null;
		st = conn.createStatement(); // statements
		int i = st.executeUpdate(expression); // run the query
		if (i == -1) {
			System.out.println("db error : " + expression);
		}
		st.close();
	} // void update()

	public void batchUpdate(File f) throws Exception {
		Statement st = null;
		st = conn.createStatement(); // statements
		Scanner sc = null;
		sc = new Scanner(f);
		StringBuilder sql = new StringBuilder();
		while (sc.hasNextLine()) {
			String sqlLine = sc.nextLine().trim();
			if (sqlLine.startsWith("--")) {
				continue;
			}
			sql.append(sqlLine + ' ');
			if (!sqlLine.contains(";")) {
				continue;
			}
			String finalSQL = sql.toString();
			System.out.println(finalSQL);
			st.executeUpdate(finalSQL);
			sql = new StringBuilder();
		}
		System.out.println("creation done");
		sc.close();
	}

	private void showResultSetContent(ResultSet rs) throws SQLException {
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();

		Msg.userMsgLn("你的SQL的结果是:");
		Msg.userSep(numberOfColumns, '-');
		for (int i = 1; i <= numberOfColumns; i++) {
			String name = rsMetaData.getColumnName(i);
			Msg.userMsgF("%-20s|", name);

		}
		Msg.userMsgLn("");
		Msg.userSep(numberOfColumns, '-');
		while (rs.next()) {
			for (int i = 0; i < numberOfColumns; i++) {
				Msg.userMsgF("%-20s|", rs.getObject(i + 1));
			}
			Msg.userMsgLn("");
		}
		Msg.userSep(numberOfColumns, '-');
	} //void dump( ResultSet rs )

}
