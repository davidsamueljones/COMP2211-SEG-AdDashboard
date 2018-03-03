package group33.seg.controller.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseTable {
  void createTable(Connection c) throws SQLException;

  void importFile(Connection c, String filepath) throws SQLException;

}


