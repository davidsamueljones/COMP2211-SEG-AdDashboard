package group33.seg.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerLog implements DatabaseTables {
    @Override
    public void createTable(Connection c) throws SQLException {
        Statement st = c.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS test_server (entry_date TIMESTAMP, " +
                "user_id BIGINT NOT NULL," +
                "exit_date TIMESTAMP," +
                "pages_viewed INTEGER, " +
                "conversion BOOLEAN)");
        st.close();
    }

    @Override
    public void populateTable(Connection c) throws SQLException {
        Statement st = c.createStatement();
        st.execute("INSERT INTO test_server VALUES (? ? ? ? ?)");
        st.close();

    }
}
