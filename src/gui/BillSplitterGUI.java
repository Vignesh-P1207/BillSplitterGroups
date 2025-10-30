package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import models.Person;
import models.Expense;
import db.DBHelper;

public class BillSplitterGUI {
    private DBHelper db = new DBHelper();
    private JFrame frame;
    private JTextArea outputArea;

    public BillSplitterGUI() {
        frame = new JFrame("Bill Splitter");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField personNameField = new JTextField();
        JButton addPersonBtn = new JButton("Add Person");

        JTextField expDescField = new JTextField();
        JTextField expAmountField = new JTextField();
        JComboBox<String> payerBox = new JComboBox<>();
        JButton addExpenseBtn = new JButton("Add Expense");

        JButton viewExpensesBtn = new JButton("View Expenses");
        JButton calculateBtn = new JButton("Calculate Balances");

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        panel.add(new JLabel("Person Name:"));
        panel.add(personNameField);
        panel.add(addPersonBtn);

        panel.add(new JLabel("Expense Description:"));
        panel.add(expDescField);
        panel.add(new JLabel("Amount:"));
        panel.add(expAmountField);
        panel.add(new JLabel("Payer:"));
        panel.add(payerBox);
        panel.add(addExpenseBtn);

        panel.add(viewExpensesBtn);
        panel.add(calculateBtn);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        refreshPayerBox(payerBox);

        addPersonBtn.addActionListener(e -> {
            String name = personNameField.getText().trim();
            if(!name.isEmpty()) {
                db.addPerson(new Person(name));
                outputArea.append("Person added: " + name + "\n");
                personNameField.setText("");
                refreshPayerBox(payerBox);
            }
        });

        addExpenseBtn.addActionListener(e -> {
            String desc = expDescField.getText().trim();
            String amtText = expAmountField.getText().trim();
            int payerIndex = payerBox.getSelectedIndex();
            ArrayList<Person> persons = db.getAllPersons();
            if (!desc.isEmpty() && !amtText.isEmpty() && payerIndex >= 0) {
                double amount = Double.parseDouble(amtText);
                Person payer = persons.get(payerIndex);
                db.addExpense(new Expense(desc, amount, payer.getId()));
                outputArea.append("Expense added: " + desc + ", ₹" + amount + ", Paid by: " + payer.getName() + "\n");
                expDescField.setText(""); expAmountField.setText("");
            }
        });

        viewExpensesBtn.addActionListener(e -> {
            outputArea.append("=== All Expenses ===\n");
            for(Expense exp : db.getAllExpenses()) {
                Person payer = db.getAllPersons().stream()
                        .filter(p -> p.getId() == exp.getPayerId()).findFirst().orElse(null);
                if(payer != null)
                    outputArea.append(exp.getDescription() + ": ₹" + exp.getAmount() + ", Paid by: " + payer.getName() + "\n");
            }
        });

        calculateBtn.addActionListener(e -> {
            ArrayList<Person> persons = db.getAllPersons();
            ArrayList<Expense> expenses = db.getAllExpenses();
            if(persons.isEmpty() || expenses.isEmpty()) {
                outputArea.append("No data to calculate.\n");
                return;
            }
            double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
            double share = total / persons.size();
            outputArea.append("Total: ₹" + total + ", Each owes: ₹" + share + "\n");
            for(Person p : persons) {
                double paid = expenses.stream().filter(e1 -> e1.getPayerId()==p.getId())
                        .mapToDouble(Expense::getAmount).sum();
                double balance = paid - share;
                if(balance > 0) outputArea.append(p.getName() + " should receive ₹" + balance + "\n");
                else if(balance < 0) outputArea.append(p.getName() + " owes ₹" + (-balance) + "\n");
                else outputArea.append(p.getName() + " is settled.\n");
            }
        });

        frame.setVisible(true);
    }

    private void refreshPayerBox(JComboBox<String> box) {
        box.removeAllItems();
        for(Person p : db.getAllPersons())
            box.addItem(p.getName());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillSplitterGUI::new);
    }
}
