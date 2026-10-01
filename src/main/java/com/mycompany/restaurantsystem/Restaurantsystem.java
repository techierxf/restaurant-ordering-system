package com.mycompany.restaurantsystem;

import java.sql.*;
import java.util.Scanner;

public class Restaurantsystem {

    private Scanner input = new Scanner(System.in);

    public void start() {

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("   RESTAURANT ORDERING SYSTEM");
            System.out.println("================================");
            System.out.println("1. View Food");
            System.out.println("2. Update Food");
            System.out.println("3. Delete Food");
            System.out.println("4. Order Food");
            System.out.println("5. Exit");
            System.out.print("Please choose from 1-5: ");

        if (input.hasNextInt()) {
            choice = input.nextInt();
            input.nextLine();
        } else {
            System.out.println("Invalid input! Please enter a number.");
            input.nextLine();
            choice = 0;
            continue;
}


            switch (choice) {

                case 1:
                    viewFood();
                    break;

                case 2:
                    updateFood();
                    break;

                case 3:
                    deleteFood();
                    break;

                case 4:
                    orderFood();
                    break;

                case 5:
                    System.out.println("Thank you for ordering!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);
    }
    // VIEW FOOD


        private void viewFood() {

    String sql = "SELECT * FROM foods ORDER BY "
            + "CASE category "
            + "WHEN 'Main Course' THEN 1 "
            + "WHEN 'Appetizer' THEN 2 "
            + "WHEN 'Drinks' THEN 3 "
            + "WHEN 'Dessert' THEN 4 "
            + "ELSE 5 END, food_id";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        String currentCategory = "";

        while (rs.next()) {

            String category = rs.getString("category");

            // Display category heading when category changes
            if (!category.equals(currentCategory)) {

                currentCategory = category;

                System.out.println("\n================ "
                        + category.toUpperCase()
                        + " ================");

                System.out.printf("%-5s %-20s %-10s %-10s%n",
                        "ID", "FOOD NAME", "PRICE", "STOCK");

                System.out.println("--------------------------------------------");
            }

            System.out.printf("%-5d %-20s %-10.2f %-10d%n",
                    rs.getInt("food_id"),
                    rs.getString("food_name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"));
        }

    } catch (SQLException e) {
        System.out.println("Database Error: " + e.getMessage());
    }
}

    // UPDATE FOOD
    public void updateFood() {

    System.out.println("\n========== UPDATE FOOD ==========");

    System.out.print("Enter Food ID to update: ");

    if (!input.hasNextInt()) {
        System.out.println("Invalid Food ID! Please enter a number.");
        input.next();
        return;
    }

    int foodID = input.nextInt();
    input.nextLine();

    String sql = "SELECT * FROM foods WHERE food_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, foodID);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            String foodName = rs.getString("food_name");

            System.out.println("\nFood Found:");
            System.out.println("Food ID : " + foodID);
            System.out.println("Food Name : " + foodName);
            System.out.println("Current Price : " + rs.getDouble("price"));
            System.out.println("Current Stock : " + rs.getInt("quantity"));

            // Enter new price
            double newPrice;

            while (true) {
                System.out.print("\nEnter New Price: ");

                if (input.hasNextDouble()) {
                    newPrice = input.nextDouble();

                    if (newPrice >= 0) {
                        break;
                    }

                    System.out.println("Price cannot be negative!");

                } else {
                    System.out.println("Invalid price! Please enter a number.");
                    input.next();
                }
            }

            // Enter new quantity
            int newQuantity;

            while (true) {
                System.out.print("Enter New Quantity: ");

                if (input.hasNextInt()) {
                    newQuantity = input.nextInt();

                    if (newQuantity >= 0) {
                        break;
                    }

                    System.out.println("Quantity cannot be negative!");

                } else {
                    System.out.println("Invalid quantity! Please enter a whole number.");
                    input.next();
                }
            }

            // Update ONLY price and quantity
            String updateSQL =
                    "UPDATE foods SET price = ?, quantity = ? WHERE food_id = ?";

            try (PreparedStatement updateStmt =
                         conn.prepareStatement(updateSQL)) {

                updateStmt.setDouble(1, newPrice);
                updateStmt.setInt(2, newQuantity);
                updateStmt.setInt(3, foodID);

                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("\nFood updated successfully!");
                    System.out.println("Food Name : " + foodName);
                    System.out.printf("New Price : %.2f%n", newPrice);
                    System.out.println("New Stock : " + newQuantity);
                }
            }

        } else {
            System.out.println("\nFood ID not found!");
        }

    } catch (SQLException e) {
        System.out.println("Database Error: " + e.getMessage());
    }
}

    // DELETE FOOD
   public void deleteFood() {

    System.out.println("\n========== DELETE FOOD ==========");

    System.out.print("Enter Food ID to delete: ");

    if (!input.hasNextInt()) {
        System.out.println("Invalid Food ID! Please enter a number.");
        input.next();
        return;
    }

    int foodID = input.nextInt();
    input.nextLine();

    String sql = "SELECT * FROM foods WHERE food_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, foodID);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            // Display food before deleting
            System.out.println("\nFood Found:");
            System.out.println("ID       : " + rs.getInt("food_id"));
            System.out.println("Name     : " + rs.getString("food_name"));
            System.out.println("Price    : " + rs.getDouble("price"));
            System.out.println("Stock    : " + rs.getInt("quantity"));
            System.out.println("Category : " + rs.getString("category"));

            // Confirmation
            System.out.print("\nAre you sure you want to delete this food? (Y/N): ");
            String confirm = input.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {

                String deleteSQL = "DELETE FROM foods WHERE food_id = ?";

                try (PreparedStatement deleteStmt =
                             conn.prepareStatement(deleteSQL)) {

                    deleteStmt.setInt(1, foodID);

                    int rows = deleteStmt.executeUpdate();

                    if (rows > 0) {
                        System.out.println("\nFood deleted successfully!");
                    }
                }

            } else if (confirm.equalsIgnoreCase("N")) {

                System.out.println("\nDelete cancelled.");

            } else {

                System.out.println("\nInvalid choice! Delete cancelled.");
            }

        } else {

            System.out.println("\nFood ID not found!");
        }

    } catch (SQLException e) {
        System.out.println("Database Error: " + e.getMessage());
    }
}

    // ORDER FOOD
  private void orderFood() {

    Order order = new Order();

    String again = "Y";

    do {

        viewFood();

       System.out.print("\nEnter Food ID: ");

    if (!input.hasNextInt()) {
        System.out.println("Invalid Food ID! Please enter a number.");
        input.nextLine();
        continue;
}

    int id = input.nextInt();

        System.out.print("Enter quantity: ");

    if (!input.hasNextInt()) {
        System.out.println("Invalid quantity! Please enter a number.");
        input.nextLine();
    continue;
}

int quantity = input.nextInt();

        Food food = getFood(id);

        if (food == null) {
            System.out.println("Food ID not found!");
            continue;
        }

        if (quantity <= 0) {
            System.out.println("Invalid quantity!");
            continue;
        }

        if (quantity > food.getQuantity()) {
            System.out.println("Not enough stock!");
            continue;
        }

        order.addOrder(food, quantity);

        updateStock(id, food.getQuantity() - quantity);

        System.out.print("Add another food? (Y/N): ");
        again = input.next();

    } while (again.equalsIgnoreCase("Y"));

    order.displayOrder(input);
}

    // GET FOOD
    private Food getFood(int id) {

    String sql = "SELECT * FROM foods WHERE food_id=?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            return new Food(
                rs.getInt("food_id"),
                rs.getString("food_name"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getString("category")
            );
        }

    } catch (SQLException e) {
        System.out.println("Database Error: " + e.getMessage());
    }

    return null;
}
    

        // UPDATE STOCK AFTER ORDER
    private void updateStock(int id, int newQuantity) {

        String sql = "UPDATE foods SET quantity=? WHERE food_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newQuantity);
            ps.setInt(2, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Restaurantsystem system = new Restaurantsystem();
        system.start();
    }
}