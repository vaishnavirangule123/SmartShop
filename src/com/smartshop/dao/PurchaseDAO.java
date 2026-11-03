package com.smartshop.dao;

import com.smartshop.DatabaseConnection;
import java.sql.*;

public class PurchaseDAO {
    public boolean addPurchase(int userId, int productId, int quantity) {
        String priceQuery = "SELECT price, quantity FROM products WHERE product_id = ?";
        String insertPurchase = "INSERT INTO purchases (user_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";
        String updateStock = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction start

           
            PreparedStatement priceStmt = conn.prepareStatement(priceQuery);
            priceStmt.setInt(1, productId);
            ResultSet rs = priceStmt.executeQuery();

            if (rs.next()) {
                double price = rs.getDouble("price");
                int availableQty = rs.getInt("quantity");

                if (availableQty >= quantity) {
                    double totalPrice = price * quantity; 

                   
                    PreparedStatement pStmt = conn.prepareStatement(insertPurchase);
                    pStmt.setInt(1, userId);
                    pStmt.setInt(2, productId);
                    pStmt.setInt(3, quantity);
                    pStmt.setDouble(4, totalPrice);
                    pStmt.executeUpdate();

                   
                    PreparedStatement uStmt = conn.prepareStatement(updateStock);
                    uStmt.setInt(1, quantity);
                    uStmt.setInt(2, productId);
                    uStmt.executeUpdate();

                    conn.commit();
                    return true;
                } else {
                    System.out.println("Insufficient stock! Available quantity: " + availableQty);
                }
            } else {
                System.out.println("Invalid Product ID!");
            }
            conn.rollback();
        } catch (SQLException e) {
            System.out.println("Purchase Error: " + e.getMessage());
        }
        return false;
    }
}