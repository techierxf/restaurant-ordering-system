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
    private void updateFood() {

       System.out.print("Enter Food ID: ");

        if (!input.hasNextInt()) {
            System.out.println("Invalid Food ID! Please enter a number.");
            input.nextLine();
        return;
      }
            int id = input.nextInt();
            input.nextLine();

        System.out.print("Enter new food name: ");
        String name = input.nextLine();

        System.out.print("Enter new price: ");

        if (!input.hasNextDouble()) {
             System.out.println("Invalid price! Please enter a number.");
            input.nextLine();
        return;
    }

        double price = input.nextDouble();

            System.out.print("Enter new quantity: ");

        if (!input.hasNextInt()) {
            System.out.println("Invalid quantity! Please enter a number.");
             input.nextLine();
        return;
    }

int quantity = input.nextInt();

        String sql = "UPDATE foods SET food_name=?, price=?, quantity=? WHERE food_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setInt(4, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Food updated successfully!");
            } else {
                System.out.println("Food ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // DELETE FOOD
    private void deleteFood() {

        System.out.print("Enter Food ID to delete: ");
        int id = input.nextInt();

        String sql = "DELETE FROM foods WHERE food_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Food deleted successfully!");
            } else {
                System.out.println("Food ID not found.");
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