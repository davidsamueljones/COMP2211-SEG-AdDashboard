package group33.seg.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ClickLog implements DatabaseTables {

    @Override
    public void createTable(Connection c) throws SQLException {
        Statement st = c.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS click_log (date TIMESTAMP," +
                " user_id BIGINT NOT NULL, " +
                "click_cost REAL)");
        st.close();
    }

    @Override
    public void populateTable(Connection c) throws SQLException {
        Statement st = c.createStatement();
        st.execute("INSERT INTO test_table VALUES (? ? ?)");
        st.close();

    }
}
