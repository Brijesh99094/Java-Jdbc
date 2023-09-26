import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class SecurityIssueApp {

    private static final String DB_URL = "jdbc:sqlite:security_issues.db";

    public static void main(String[] args) {
        initDatabase();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Security Issue Tracking System");
            System.out.println("1. List Issues");
            System.out.println("2. Add Issue");
            System.out.println("3. Edit Issue");
            System.out.println("4. Delete Issue");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    listIssues();
                    break;
                case 2:
                    addIssue(scanner);
                    break;
                case 3:
                    editIssue(scanner);
                    break;
                case 4:
                    deleteIssue(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void initDatabase() {
        try {
        Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement() ;
                String sql = "CREATE TABLE IF NOT EXISTS security_issues ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "description TEXT,"
                    + "severity TEXT NOT NULL)" ;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listIssues() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM security_issues")) {
            System.out.println("Security Issues:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String severity = rs.getString("severity");
                System.out.println("ID: " + id + ", Title: " + title + ", Description: " + description + ", Severity: " + severity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addIssue(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter severity: ");
        String severity = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String insertQuery = String.format(
                    "INSERT INTO security_issues (title, description, severity) VALUES ('%s', '%s', '%s')",
                    title, description, severity);
            stmt.executeUpdate(insertQuery);
            System.out.println("Issue added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editIssue(Scanner scanner) {
        System.out.print("Enter issue ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new severity: ");
        String severity = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String updateQuery = String.format(
                    "UPDATE security_issues SET title = '%s', description = '%s', severity = '%s' WHERE id = %d",
                    title, description, severity, id);
            stmt.executeUpdate(updateQuery);
            System.out.println("Issue updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteIssue(Scanner scanner) {
        System.out.print("Enter issue ID to delete: ");
        int id = scanner.nextInt();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String deleteQuery = String.format("DELETE FROM security_issues WHERE id = %d", id);
            stmt.executeUpdate(deleteQuery);
            System.out.println("Issue deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
