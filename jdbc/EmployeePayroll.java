package com.bridgelabz.jdbc;

import java.sql.*;

public class EmployeePayroll {

    private static final String URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Falcon@2003";

    public static Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void retrieveEmployees() {
        try (Connection conn = establishConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employee_payroll")) {

            System.out.println("Employee Payroll Data:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("employee_name")
                        + ", Salary: " + rs.getDouble("salary")
                        + ", Gender: " + rs.getString("gender")
                        + ", Start Date: " + rs.getDate("start_date"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateSalary(String employeeName, double newSalary) {
        String query = "UPDATE employee_payroll SET salary = ? WHERE employee_name = ?";
        try (Connection conn = establishConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, newSalary);
            pstmt.setString(2, employeeName);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Salary updated successfully for " + employeeName);
            } else {
                System.out.println("No record found for " + employeeName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void retrieveEmployeesByDate(String startDate, String endDate) {
        String query = "SELECT * FROM employee_payroll WHERE start_date BETWEEN ? AND ?";
        try (Connection conn = establishConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));

            ResultSet data = pstmt.executeQuery();
            System.out.println("Employees who joined between " + startDate + " and " + endDate + ":");
            while (data.next()) {
                System.out.println("ID: " + data.getInt("id") + ", Name: " + data.getString("employee_name")
                        + ", Salary: " + data.getDouble("salary")
                        + ", Gender: " + data.getString("gender")
                        + ", Start Date: " + data.getDate("start_date"));
            }
            data.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void operations(String gender) {
        String query = "SELECT SUM(salary),AVG(salary),MIN(salary),MAX(salary),COUNT(id) FROM employee_payroll WHERE gender = ? GROUP BY gender";
        try (Connection conn = establishConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, gender);
            ResultSet data = pstmt.executeQuery();

            if (data.next()) {
                System.out.println("Total Salary of " + (gender.equals("M") ? "Male" : "Female") + " Employees: " + data.getDouble(1));
                System.out.println("Average Salary of " + (gender.equals("M") ? "Male" : "Female") + " Employees: " + data.getDouble(2));
                System.out.println("Minimum Salary of " + (gender.equals("M") ? "Male" : "Female") + " Employees: " + data.getDouble(3));
                System.out.println("Maximum Salary of " + (gender.equals("M") ? "Male" : "Female") + " Employees: " + data.getDouble(4));
                System.out.println("Total employees of  " + (gender.equals("M") ? "Male" : "Female") + " Employees: " + data.getInt(5));
            } else {
                System.out.println("No data found for gender: " + gender);
            }
            data.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        retrieveEmployees();
        updateSalary("Sai", 3000000.00);
        retrieveEmployeesByDate("2023-01-16", "2024-02-21");
        operations("M");
        operations("F");
    }
}
