package models;
public class Expense {
    private int id;
    private String description;
    private double amount;
    private int payerId;

    public Expense(int id, String description, double amount, int payerId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.payerId = payerId;
    }

    public Expense(String description, double amount, int payerId) {
        this.description = description;
        this.amount = amount;
        this.payerId = payerId;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public int getPayerId() { return payerId; }
}
