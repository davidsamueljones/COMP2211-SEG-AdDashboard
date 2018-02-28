package group33.seg.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerLog implements DatabaseTable {
    @Override
    public void createTable(Connection c) throws SQLException {
        Statement st = c.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS server_log (entry_date TIMESTAMP, " +
                "user_id BIGINT NOT NULL," +
                "exit_date TIMESTAMP," +
                "pages_viewed INTEGER, " +
                "conversion BOOLEAN)");
        st.close();
    }

    @Override
    public void importFile(Connection c, String filepath) throws SQLException {
        Statement st = c.createStatement();
        st.execute("COPY server_log FROM '" + filepath + "' WITH DELIMITER ',' CSV HEADER NULL 'n/a'");
        st.close();
    }
}