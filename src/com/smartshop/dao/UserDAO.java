package com.smartshop.dao;
	import com.smartshop.User;
	import com.smartshop.DatabaseConnection;
	import java.sql.*;

	public class UserDAO {
	    public boolean registerUser(User user) {
	        String query = "INSERT INTO users (first_name, last_name, username, password, city, email, mobile, role) VALUES (?, ?, ?, ?, ?, ?, ?, 'USER')";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, user.getFirstName());
	            pstmt.setString(2, user.getLastName());
	            pstmt.setString(3, user.getUsername());
	            pstmt.setString(4, user.getPassword());
	            pstmt.setString(5, user.getCity());
	            pstmt.setString(6, user.getEmail());
	            pstmt.setString(7, user.getMobile());
	            pstmt.executeUpdate();
	            return true;
	        } catch (SQLException e) {
	            System.out.println("Registration Error: " + e.getMessage());
	            return false;
	        }
	    }

	    public User loginUser(String username, String password) {
	        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, password);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                User user = new User();
	                user.setUserId(rs.getInt("user_id"));
	                user.setFirstName(rs.getString("first_name"));
	                user.setUsername(rs.getString("username"));
	                user.setRole(rs.getString("role"));
	                return user;
	            }
	        } catch (SQLException e) {
	            System.out.println("Login Error: " + e.getMessage());
	        }
	        return null;
	    }
	}

