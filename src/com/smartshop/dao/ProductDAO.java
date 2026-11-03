package com.smartshop.dao;
	import com.smartshop.Product;
	import com.smartshop.DatabaseConnection;
	import java.sql.*;
	import java.util.ArrayList;
	import java.util.List;

	public class ProductDAO {
	    public List<Product> getAllProducts() {
	        List<Product> products = new ArrayList<>();
	        String query = "SELECT * FROM products ORDER BY name ASC";
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                Product p = new Product();
	                p.setProductId(rs.getInt("product_id"));
	                p.setName(rs.getString("name"));
	                p.setDescription(rs.getString("description"));
	                p.setPrice(rs.getDouble("price"));
	                p.setQuantity(rs.getInt("quantity"));
	                products.add(p);
	            }
	        } catch (SQLException e) {
	            System.out.println("Error fetching products: " + e.getMessage());
	        }
	        return products;
	    }

	    public boolean addProduct(Product p) {
	        String query = "INSERT INTO products (product_id, name, description, price, quantity) VALUES (?, ?, ?, ?, ?)";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setInt(1, p.getProductId());
	            pstmt.setString(2, p.getName());
	            pstmt.setString(3, p.getDescription());
	            pstmt.setDouble(4, p.getPrice());
	            pstmt.setInt(5, p.getQuantity());
	            pstmt.executeUpdate();
	            return true;
	        } catch (SQLException e) {
	            System.out.println("Error adding product: " + e.getMessage());
	            return false;
	        }
	    }
	}

