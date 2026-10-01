package com.mycompany.restaurantsystem;
import java.util.ArrayList;
import java.util.Scanner;

public class Order {

    private ArrayList<Food> foods;
    private ArrayList<Integer> quantities;

    public Order() {
        foods = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    public void addOrder(Food food, int quantity) {
        foods.add(food);
        quantities.add(quantity);
    }

    public double getTotal() {

        double total = 0;

        for (int i = 0; i < foods.size(); i++) {
            total += foods.get(i).getPrice() * quantities.get(i);
        }

        return total;
    }

 public void displayOrder(Scanner input) {

    System.out.println("\n==================== ORDER SUMMARY ====================");
    System.out.printf("%-5s %-20s %-10s %-8s %-10s%n",
            "ID", "FOOD NAME", "PRICE", "QTY", "TOTAL");
    System.out.println("--------------------------------------------------------");

    for (int i = 0; i < foods.size(); i++) {

        Food food = foods.get(i);
        int quantity = quantities.get(i);

        double subtotal = food.getPrice() * quantity;

        System.out.printf("%-5d %-20s %-10.2f %-8d %-10.2f%n",
                food.getFoodID(),
                food.getFoodName(),
                food.getPrice(),
                quantity,
                subtotal);
    }

    System.out.println("--------------------------------------------------------");
    System.out.printf("%-43s %.2f%n", "GRAND TOTAL:", getTotal());
    System.out.println("========================================================");

    System.out.print("\nProceed to checkout? (Y/N): ");
    String checkout = input.next();

    if (checkout.equalsIgnoreCase("Y")) {

        double payment;

        do {
            System.out.print("Enter Payment: ");

            if (input.hasNextDouble()) {

                payment = input.nextDouble();

                if (payment < getTotal()) {
                    System.out.println("Insufficient payment!");
                }

            } else {

                System.out.println("Invalid payment! Please enter a number.");
                input.next();
                payment = 0;
            }

        } while (payment < getTotal());

        double change = payment - getTotal();

        System.out.println("\n==================== PAYMENT RECEIPT ====================");
        System.out.printf("Total Amount : %.2f%n", getTotal());
        System.out.printf("Payment      : %.2f%n", payment);
        System.out.printf("Change       : %.2f%n", change);
        System.out.println("==================================================");
        System.out.println("Payment successful!");
        System.out.println("Thank you for ordering!");

    } else {
        System.out.println("Checkout cancelled.");
    }
}
}