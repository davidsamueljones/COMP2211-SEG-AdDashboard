package group33.seg.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ImpressionLog implements DatabaseTables {

    @Override
    public void createTable(Connection c) throws SQLException {
        Statement st = c.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS impressions_log (date TIMESTAMP," +
                "user_id BIGINT NOT NULL, " +
                "female BOOLEAN, " +
                "age age, " +
                "income income, " +
                "context context, " +
                "impression_cost REAL)");
        st.close();
    }

    @Override
    public void populateTable(Connection c) throws SQLException {
        Statement st = c.createStatement();
        st.execute("INSERT INTO impressions_log VALUES (? ? ? ? ? ? ?)");
        st.close();

    }
}
