package com.smartshop.main;

import com.smartshop.dao.ProductDAO;
import com.smartshop.dao.PurchaseDAO;
import com.smartshop.dao.UserDAO;
import com.smartshop.Product;
import com.smartshop.User;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class main {
    private static User loggedInUser = null;

   
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String MOBILE_REGEX = "^[0-9]{10}$";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        PurchaseDAO purchaseDAO = new PurchaseDAO();
        int choice = 0;

        do {
            System.out.println("\n==================================");
            System.out.println("      SMART SHOP APPLICATION      ");
            System.out.println("==================================");
            if (loggedInUser != null) {
                System.out.println("Logged in as: " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
            } else {
                System.out.println("Status: Guest Mode (Please Login/Register)");
            }
           
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Products");
            System.out.println("4. Add Product to Cart / Purchase");
            System.out.println("5. Add New Product (Admin Only)");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
            System.out.print("Enter choice >> ");

            
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input! Please enter a valid number (1-7): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
            case 1:
                User u = new User();
                System.out.print("First Name: "); u.setFirstName(scanner.nextLine().trim());
                System.out.print("Last Name: "); u.setLastName(scanner.nextLine().trim());
                System.out.print("Username: "); u.setUsername(scanner.nextLine().trim()); // .trim() जोडले
                System.out.print("Password: "); u.setPassword(scanner.nextLine().trim()); // .trim() जोडले
                System.out.print("City: "); u.setCity(scanner.nextLine().trim());

                // Email Validation
                String email;
                while (true) {
                    System.out.print("Email (e.g., abc@gmail.com): ");
                    email = scanner.nextLine().trim();
                    if (Pattern.matches(EMAIL_REGEX, email)) {
                        u.setEmail(email);
                        break;
                    }
                    System.out.println("Invalid email format! Must include '@' and domain (e.g., .com).");
                }

                // Mobile Validation (10 Digits)
                String mobile;
                while (true) {
                    System.out.print("Mobile Number (10 digits): ");
                    mobile = scanner.nextLine().trim();
                    if (Pattern.matches(MOBILE_REGEX, mobile)) {
                        u.setMobile(mobile);
                        break;
                    }
                    System.out.println("Invalid mobile number! Must be exactly 10 digits.");
                }

                if (userDAO.registerUser(u)) {
                    System.out.println("Registered Successfully! You can now login.");
                }
                break;

            case 2:
                System.out.print("Username: "); String uname = scanner.nextLine().trim(); // .trim() जोडले
                System.out.print("Password: "); String pass = scanner.nextLine().trim(); // .trim() जोडले
                loggedInUser = userDAO.loginUser(uname, pass);
                if (loggedInUser != null) {
                    System.out.println("Login Successful! Welcome, " + loggedInUser.getFirstName() + " (" + loggedInUser.getRole() + ")");
                } else {
                    System.out.println("Invalid Credentials! Please check your username and password.");
                }
                break;

                case 3:
                    List<Product> list = productDAO.getAllProducts();
                    System.out.println("\nID | Name | Description | Price | Stock");
                    System.out.println("----------------------------------------");
                    for (Product p : list) {
                        System.out.println(p.getProductId() + " | " + p.getName() + " | " + p.getDescription() + " | " + p.getPrice() + " | " + p.getQuantity());
                    }
                    break;

                case 4:
                    if (loggedInUser == null) {
                        System.out.println("Access Denied! Please login first to make a purchase.");
                        break;
                    }
                    if (!loggedInUser.getRole().equals("USER")) {
                        System.out.println("Admins cannot purchase products from this menu.");
                        break;
                    }
                    System.out.print("Enter Product ID: "); int pid = scanner.nextInt();
                    System.out.print("Enter Quantity: "); int qty = scanner.nextInt();
                    scanner.nextLine();
                    if (purchaseDAO.addPurchase(loggedInUser.getUserId(), pid, qty)) {
                        System.out.println("Purchased Successfully! Stock updated.");
                    } else {
                        System.out.println("Purchase failed! Check Product ID or available stock.");
                    }
                    break;

                case 5:
                    if (loggedInUser == null) {
                        System.out.println("Access Denied! Please login as Admin first.");
                        break;
                    }
                    if (!loggedInUser.getRole().equals("ADMIN")) {
                        System.out.println("Access Denied! Only Admin can add new products.");
                        break;
                    }
                    Product np = new Product();
                    System.out.print("Enter Product ID: "); np.setProductId(scanner.nextInt()); scanner.nextLine();
                    System.out.print("Product Name: "); np.setName(scanner.nextLine());
                    System.out.print("Description: "); np.setDescription(scanner.nextLine());
                    System.out.print("Price: "); np.setPrice(scanner.nextDouble());
                    System.out.print("Quantity: "); np.setQuantity(scanner.nextInt()); scanner.nextLine();
                    if (productDAO.addProduct(np)) {
                        System.out.println("Product Added Successfully to Inventory!");
                    }
                    break;

                case 6:
                    if (loggedInUser == null) {
                        System.out.println("You are already logged out!");
                    } else {
                        loggedInUser = null;
                        System.out.println("Logged out successfully!");
                    }
                    break;

                case 7:
                    System.out.println("Exiting Smart Shop. Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice! Please select an option between 1 and 7.");
            }
        } while (choice != 7);

        scanner.close();
    }
}