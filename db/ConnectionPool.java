package db;

import java.io.ObjectStreamException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
	private static ConnectionPool instance = null;
	private LinkedBlockingQueue<Connection> connections = new LinkedBlockingQueue<>();
	private static final int MAX_CONNECTIONS = 10;
	private static ArrayList<Connection> Connections = new ArrayList<Connection>();

	private ConnectionPool() {
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			String connectionUrl = "jdbc:sqlserver://localhost;database=coupon;integratedSecurity=true;";
			try {
				Connection c = DriverManager.getConnection(connectionUrl);
				connections.offer(c);
			} catch (SQLException e) {
				e.getMessage();
			}
			if (instance != null)
				throw new RuntimeException("Please Use getInstance()");

		}
	}

	public static ConnectionPool getInstance() {
		if (instance == null) {// First check
			synchronized (ConnectionPool.class) {
				if (instance == null) {// Second check
					instance = new ConnectionPool();
				}
			}
		}
		return instance;
	}

	public synchronized Connection getConnection() throws SQLException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e1) {
			System.out.println(e1.getMessage());
		}
		String connectionUrl = "jdbc:sqlserver://localhost;database=coupon;integratedSecurity=true;";
		Connection con = DriverManager.getConnection(connectionUrl);

		while (Connections.size() == MAX_CONNECTIONS) {
			System.out.println("Connection Pool is FULL!!!");
			try {
				System.out.println("Wait until next available connection");
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage() + "Wait() Error in ConPool => on getCon.");
			}
		}
		Connections.add(con);
		return con;
	}

	public synchronized void returnConnection(Connection c) throws Exception {
		for (int i = 0; i < Connections.size(); i++) {
			if (Connections.get(i).equals(c)) {
				try {
					Connections.get(i).close();
					Connections.remove(i);
					notify();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public void closeAllConnections() {
		for (int i = 0; i < Connections.size(); i++) {
			try {
				Connections.get(i).close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("All Connections closed for the session"); // optional massage
	}
	
	/**
	 * This method prevents violating the singleTone concept throw cloning.
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return instance;
	}

	/*
	 * prevents violating the singleTone concept throw OutputStreaming.
	 */
	private Object readResolve() throws ObjectStreamException {
		System.out.println("..Read resolve..");
		return instance;
	}

}
