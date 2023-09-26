import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class SecurityIssueAppTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Before
    public void createDatabase() {
        // Create an SQLite database for testing
        String dbURL = "jdbc:sqlite:test_security_issues.db";
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS security_issues ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "description TEXT,"
                    + "severity TEXT NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void deleteDatabase() {
        // Delete the test database after testing
        try {
            DriverManager.getConnection("jdbc:sqlite:test_security_issues.db;shutdown=true");
        } catch (SQLException ignored) {
            // Ignore any exception during shutdown
        }
    }

    @Test
    public void testAddIssue() {
        String input = "2\nTest Title\nTest Description\nHigh\n5\n1\nTest Code Line\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SecurityIssueApp.main(new String[]{});

        assertEquals("Issue added successfully.\n", outContent.toString());
    }

    @Test
    public void testEditIssue() {
        // Insert a test issue into the database
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test_security_issues.db");
             Statement stmt = conn.createStatement()) {
            String insertQuery = "INSERT INTO security_issues (title, description, severity) VALUES " +
                    "('Test Title', 'Test Description', 'Medium')";
            stmt.executeUpdate(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String input = "3\n1\nNew Title\nNew Description\nHigh\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SecurityIssueApp.main(new String[]{});

        assertEquals("Issue updated successfully.\n", outContent.toString());
    }

    @Test
    public void testDeleteIssue() {
        // Insert a test issue into the database
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test_security_issues.db");
             Statement stmt = conn.createStatement()) {
            String insertQuery = "INSERT INTO security_issues (title, description, severity) VALUES " +
                    "('Test Title', 'Test Description', 'Medium')";
            stmt.executeUpdate(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String input = "4\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SecurityIssueApp.main(new String[]{});

        assertEquals("Issue deleted successfully.\n", outContent.toString());
    }

    @Test
    public void testListIssues() {
        // Insert two test issues into the database
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test_security_issues.db");
             Statement stmt = conn.createStatement()) {
            String insertQuery1 = "INSERT INTO security_issues (title, description, severity) VALUES " +
                    "('Test Title 1', 'Test Description 1', 'High')";
            String insertQuery2 = "INSERT INTO security_issues (title, description, severity) VALUES " +
                    "('Test Title 2', 'Test Description 2', 'Medium')";
            stmt.executeUpdate(insertQuery1);
            stmt.executeUpdate(insertQuery2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SecurityIssueApp.main(new String[]{});

        String expectedOutput = "Security Issues:\n" +
                "ID: 1, Title: Test Title 1, Description: Test Description 1, Severity: High\n" +
                "ID: 2, Title: Test Title 2, Description: Test Description 2, Severity: Medium\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
