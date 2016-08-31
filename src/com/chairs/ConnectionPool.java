package com.chairs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class ConnectionPool {

	private static DataSource datasource;
	public static String dbURL;// = "jdbc:mysql://localhost:3306/" + "chairs";
	public static String driverClass;// = "com.mysql.jdbc.Driver";
	public static String userName;// = "root";
	public static String password;// = "admin";
	public static boolean jmx = true;
	public static boolean testIdle = false;
	public static boolean testBorrow = true;
	public static boolean testReturn = false;
	public static int validationInterval = 30000;
	public static int timeBetweenEviction = 30000;
	public static int maxActive = 100;
	public static int initialSize = 10;
	public static int maxWait = 10000;
	public static int removeAbandonedTimeout = 60;
	public static int minEvictableIdle = 30000;
	public static int minIdle = 10;
	public static boolean logAbandoned = true;
	public static boolean removeAbandoned = true;
	public static String jdbcInterceptors = "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
			+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer";

	private ConnectionPool() throws IOException {
		Properties prop = new Properties();
		String propFileName = "config.properties";
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		userName = prop.getProperty("userName");
		password = prop.getProperty("password");
		dbURL = prop.getProperty("dbURL");
		driverClass = prop.getProperty("driverClass");
	}

	public static synchronized DataSource getInstance() throws IOException {
		new ConnectionPool();
		
		if (datasource == null) {
			PoolProperties p = new PoolProperties();
			p.setUrl(dbURL);
			p.setDriverClassName(driverClass);
			p.setUsername(userName);
			p.setPassword(password);
			p.setJmxEnabled(jmx);
			p.setTestWhileIdle(testIdle);
			p.setTestOnBorrow(testBorrow);
			p.setTestOnReturn(testReturn);
			p.setValidationInterval(validationInterval);
			p.setTimeBetweenEvictionRunsMillis(timeBetweenEviction);
			p.setMaxActive(maxActive);
			p.setInitialSize(initialSize);
			p.setMaxWait(maxWait);
			p.setRemoveAbandonedTimeout(removeAbandonedTimeout);
			p.setMinEvictableIdleTimeMillis(minEvictableIdle);
			p.setMinIdle(minIdle);
			p.setLogAbandoned(logAbandoned);
			p.setRemoveAbandoned(removeAbandoned);
			p.setJdbcInterceptors(jdbcInterceptors);
			datasource = new DataSource();
			datasource.setPoolProperties(p);
		}
		return datasource;
	}

	public static synchronized void closePool() {
		if (datasource != null) {
			datasource.close();
		}
	}
}