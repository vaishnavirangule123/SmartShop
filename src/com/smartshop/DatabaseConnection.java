package com.smartshop;
	import java.sql.Connection;
	import java.sql.DriverManager;

	public class DatabaseConnection {
	    private static final String URL = "jdbc:mysql://localhost:3306/smart_shop_db";
	    private static final String USER = "root";
	    private static final String PASSWORD = "vaishnavi"; 

	    public static Connection getConnection() {
	        Connection conn = null;
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            conn = DriverManager.getConnection(URL, USER, PASSWORD);
	        } catch (Exception e) {
	            System.out.println("Database Connection Error: " + e.getMessage());
	        }
	        return conn;
	    }
	}

