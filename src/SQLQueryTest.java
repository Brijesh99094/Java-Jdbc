import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

public class SQLQueryTest {

    private static Connection conn;

    @BeforeAll
    public static void setUp() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:security_issues_test.db");
        // Create a table for testing
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE security_issues (id INTEGER PRIMARY KEY, title TEXT, description TEXT, severity TEXT, owasp TEXT, path TEXT, startLine INT, endLine INT, codeLine TEXT)");
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void testGeneratedQueryEqualsOriginalQuery() throws SQLException {
        String title = "Test Title";
        String description = "Test Description";
        String severity = "High";
        String owasp = "A1";
        String path = "/test/path";
        int startLine = 1;
        int endLine = 10;
        String codeLine = "Test code line";

        // Original vulnerable query
        String originalQuery = "INSERT INTO security_issues (title, description, severity, owasp, path, startLine, endLine, codeLine) " +
                "VALUES ('" + title + "', '" + description + "', '" + severity + "', '" + owasp + "', '" + path + "', " + startLine + ", " + endLine + ", '" + codeLine + "')";

        // Secure code using prepared statements
        String secureQuery = "INSERT INTO security_issues (title, description, severity, owasp, path, startLine, endLine, codeLine) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Execute the original vulnerable query
            Statement stmt = conn.createStatement();
            int originalRowCount = stmt.executeUpdate(originalQuery);

            // Execute the secure query
            PreparedStatement pstmt = conn.prepareStatement(secureQuery);
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, severity);
            pstmt.setString(4, owasp);
            pstmt.setString(5, path);
            pstmt.setInt(6, startLine);
            pstmt.setInt(7, endLine);
            pstmt.setString(8, codeLine);
            int secureRowCount = pstmt.executeUpdate();

            // Compare the results (number of affected rows)
            assertEquals(originalRowCount, secureRowCount, "Original and secure queries should affect the same number of rows");
        } catch (SQLException e) {
            fail("SQL error occurred: " + e.getMessage());
        }
    }
}

