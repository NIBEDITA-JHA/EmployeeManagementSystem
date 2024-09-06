import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmpManageSystem extends JFrame {
    private JTextField nameField, emailField, deptField, salaryField;
    private JButton addButton, viewButton, updateButton, deleteButton;
    private JTextArea resultArea;

    public EmpManageSystem() {
        setTitle("Employee Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        JLabel deptLabel = new JLabel("Department:");
        deptField = new JTextField(20);
        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField(20);

        addButton = new JButton("Add Employee");
        viewButton = new JButton("View Employees");
        updateButton = new JButton("Update Employee");
        deleteButton = new JButton("Delete Employee");

        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);

        add(nameLabel); add(nameField);
        add(emailLabel); add(emailField);
        add(deptLabel); add(deptField);
        add(salaryLabel); add(salaryField);
        add(addButton); add(viewButton);
        add(updateButton); add(deleteButton);
        add(new JScrollPane(resultArea));

        // Button actions
        addButton.addActionListener(e -> addEmployee());
        viewButton.addActionListener(e -> viewEmployees());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());

        setVisible(true);
    }

    private void addEmployee() {
        String name = nameField.getText();
        String email = emailField.getText();
        String dept = deptField.getText();
        double salary = Double.parseDouble(salaryField.getText());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO employees (name, email, department, salary) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, dept);
            pstmt.setDouble(4, salary);
            pstmt.executeUpdate();
            resultArea.setText("Employee added successfully!");
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void viewEmployees() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("id"))
                        .append(", Name: ").append(rs.getString("name"))
                        .append(", Email: ").append(rs.getString("email"))
                        .append(", Department: ").append(rs.getString("department"))
                        .append(", Salary: ").append(rs.getDouble("salary"))
                        .append("\n");
            }
            resultArea.setText(result.toString());
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        String name = nameField.getText();
        String email = emailField.getText();
        String dept = deptField.getText();
        double salary = Double.parseDouble(salaryField.getText());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE employees SET email=?, department=?, salary=? WHERE name=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, dept);
            pstmt.setDouble(3, salary);
            pstmt.setString(4, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                resultArea.setText("Employee updated successfully!");
            } else {
                resultArea.setText("Employee not found!");
            }
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        String name = nameField.getText();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM employees WHERE name=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                resultArea.setText("Employee deleted successfully!");
            } else {
                resultArea.setText("Employee not found!");
            }
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new EmpManageSystem();
    }
}
