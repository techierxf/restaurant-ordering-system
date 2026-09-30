package com.mycompany.restaurantsystem;
import java.util.ArrayList;

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

    public void displayOrder() {

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
    }
}