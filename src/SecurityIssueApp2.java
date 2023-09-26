import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class SecurityIssueApp2 {

    public static DatabaseConnection db = new DatabaseConnection();

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
            Connection conn = db.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS security_issues ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "description TEXT,"
                    + "severity TEXT NOT NULL,"
                    + "owasp TEXT,"
                    + "path TEXT,"
                    + "startLine INTEGER,"
                    + "endLine INTEGER,"
                    + "codeLine TEXT"
                    + ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listIssues() {
	    Connection conn = db.getConnection();
        try {
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM security_issues");
            System.out.println("Security Issues:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String severity = rs.getString("severity");
                String owasp = rs.getString("owasp");
                String path = rs.getString("path");
                int startLine = rs.getInt("startLine");
                int endLine = rs.getInt("endLine");
                String codeLine = rs.getString("codeLine");
                System.out.println("ID: " + id + ", Title: " + title + ", Description: " + description +
                        ", Severity: " + severity + ", OWASP: " + owasp + ", Path: " + path +
                        ", Start Line: " + startLine + ", End Line: " + endLine + ", Code Line: " + codeLine);
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
        System.out.print("Enter OWASP category: ");
        String owasp = scanner.nextLine();
        System.out.print("Enter file path: ");
        String path = scanner.nextLine();
        System.out.print("Enter start line number: ");
        int startLine = scanner.nextInt();
        System.out.print("Enter end line number: ");
        int endLine = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter line of code: ");
        String codeLine = scanner.nextLine();

        try {
            Connection conn = db.getConnection();
            Statement stmt = conn.createStatement();
            String insertQuery = "INSERT INTO security_issues (title, description, severity, owasp, path, startLine, endLine, codeLine) " +
                    "VALUES ('" + title + "', '" + description + "', '" + severity + "', '" + owasp + "', '" + path + "', " + startLine + ", " + endLine + ", '" + codeLine + "')";
            stmt.executeUpdate(insertQuery);
            System.out.println("Issue added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editIssue(Scanner scanner) {
        System.out.print("Enter issue ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new severity: ");
        String severity = scanner.nextLine();
        System.out.print("Enter new OWASP category: ");
        String owasp = scanner.nextLine();
        System.out.print("Enter new file path: ");
        String path = scanner.nextLine();
        System.out.print("Enter new start line number: ");
        int startLine = scanner.nextInt();
        System.out.print("Enter new end line number: ");
        int endLine = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new line of code: ");
        String codeLine = scanner.nextLine();

        try {
            Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
            String updateQuery = "UPDATE security_issues SET title = '" + title + "', description = '" + description + "', severity = '" + severity + "', " +
                    "owasp = '" + owasp + "', path = '" + path + "', startLine = " + startLine + ", endLine = " + endLine + ", codeLine = '" + codeLine + "' WHERE id = " + id;
            stmt.executeUpdate(updateQuery);
            System.out.println("Issue updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteIssue(Scanner scanner) {
        System.out.print("Enter issue ID to delete: ");
        int id = scanner.nextInt();

        try {
            Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
conn.prepareStatement("DELETE FROM security_issues WHERE id = ?");
stmt.setInt(1, id);
stmt.executeUpdate();
            System.out.println("Issue deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
