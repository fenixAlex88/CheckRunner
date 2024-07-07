package ru.clevertec.check.model;


public class Product {
    private int id;
    private String description;
    private double price;
    private boolean isWholesale;
    private int quantity;

    public Product(int id, String description, double price, boolean isWholesale, int quantity) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.isWholesale = isWholesale;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public void setWholesale(boolean wholesale) {
        isWholesale = wholesale;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
