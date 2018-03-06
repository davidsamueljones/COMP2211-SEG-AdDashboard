import group33.seg.controller.database.*;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

  @Mock private DatabaseConnection conn;

  @Mock private DatabaseConfig config;

  @Test(expected = IllegalArgumentException.class)
  public void connectionArgsTest() {
    new DatabaseConnection(config.getHost(), null, config.getPassword()).connectDatabase();
  }

  @Test(expected = IllegalArgumentException.class)
  public void configTest() {
    new DatabaseConfig(null);
  }

  @Test(expected = NullPointerException.class)
  public void clickCreateTest() throws SQLException {
    new ClickLogTable().createTable(null);
  }

  @Test(expected = NullPointerException.class)
  public void impressionCreateTest() throws SQLException {
    new ImpressionLogTable().createTable(null);
  }

  @Test(expected = NullPointerException.class)
  public void serverCreateTest() throws SQLException {
    new ServerLogTable().createTable(null);
  }

//  @Test(expected = IllegalArgumentException.class)
//  public void clickImportTest() throws SQLException {
//    new ClickLog().importCSV(conn.connectDatabase(), null);
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void impressionImportTest() throws SQLException {
//    new ImpressionLog().importCSV(conn.connectDatabase(), null);
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void serverImportTest() throws SQLException {
//    new ServerLog().importCSV(conn.connectDatabase(), null);
//  }
}
