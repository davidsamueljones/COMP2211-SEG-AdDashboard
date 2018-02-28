package group33.seg.database;


import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseTables {
    public void createTable(Connection c) throws SQLException;

    public void populateTable(Connection c) throws SQLException;

}


