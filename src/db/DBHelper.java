package db;
import java.sql.*;
import java.util.ArrayList;
import models.Person;
import models.Expense;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/bill_splitter?useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "Sql@123"; // replace with your MySQL password

    private Connection conn;

    public DBHelper() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add person
    public void addPerson(Person p) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO persons(name) VALUES(?)");
            ps.setString(1, p.getName());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Get all persons
    public ArrayList<Person> getAllPersons() {
        ArrayList<Person> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM persons");
            while (rs.next()) {
                list.add(new Person(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Add expense
    public void addExpense(Expense e) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO expenses(description, amount, payer_id) VALUES(?, ?, ?)"
            );
            ps.setString(1, e.getDescription());
            ps.setDouble(2, e.getAmount());
            ps.setInt(3, e.getPayerId());
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    // Get all expenses
    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM expenses");
            while (rs.next()) {
                list.add(new Expense(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getInt("payer_id")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
