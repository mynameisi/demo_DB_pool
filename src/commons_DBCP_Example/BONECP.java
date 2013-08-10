package commons_DBCP_Example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public enum BONECP {
	INST;
	private final String DRIVER = "org.hsqldb.jdbcDriver";
	
	private final String URL = "jdbc:hsqldb:file:testdb";
	BoneCP connectionPool = null;
	Connection connection = null;

	BONECP() {

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(URL);
		config.setUsername("sa");
		config.setPassword("");
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void shutdown() throws Exception {
		Msg.debugMsg(DB.class, "Database is shutting down");
		Statement st = null;
		Connection conn = connectionPool.getConnection();
		try {
			st = conn.createStatement();
			st.execute("SHUTDOWN");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(st != null){st.close();}
			if(conn != null){conn.close();}
			if(connectionPool != null){connectionPool.shutdown();}
		}
	}

	public void update(String expression) throws SQLException {
		Statement st = null;
		Connection conn=null;
		try {
			conn = connectionPool.getConnection();

			st = conn.createStatement(); // statements
			int i = st.executeUpdate(expression); // run the query
			if (i == -1) {
				System.out.println("db error : " + expression);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

	}

	public boolean query(String sql, boolean showResult) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = connectionPool.getConnection();
		
		//后三句话能够很好的体现DBCP默认8个连接共同存在的实质
		//System.out.print("| ");
		//Thread.sleep(1000);
		//System.out.println("");
		boolean hasContent = false;
		try {
			st = conn.createStatement();
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
