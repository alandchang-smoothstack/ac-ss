package com.ss.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectUtil {
	private static ConnectUtil instance;

	private static final String driver = "com.mysql.cj.jdbc.Driver";

	public static ConnectUtil getInstance() throws ClassNotFoundException {
		if (instance == null) {
			synchronized (ConnectUtil.class) {
				if (instance == null) {
					instance = new ConnectUtil();
					Class.forName(driver);
				}
			}
		}
		return instance;
	}

	private final String url = "jdbc:mysql://localhost:3306/library?useSSL=false";
	private final String username = "root";
	private final String password = "root";

	public Connection getConnection() throws SQLException {
		Connection connection = null;
		synchronized (this) {
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(false);
		}
		return connection;
	}
}
