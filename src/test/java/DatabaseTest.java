import group33.seg.controller.database.*;
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
    new ClickLog().createTable(null);
  }

  @Test(expected = NullPointerException.class)
  public void impressionCreateTest() throws SQLException {
    new ImpressionLog().createTable(null);
  }

  @Test(expected = NullPointerException.class)
  public void serverCreateTest() throws SQLException {
    new ServerLog().createTable(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void clickImportTest() throws SQLException {
    new ClickLog().importFile(conn.connectDatabase(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void impressionImportTest() throws SQLException {
    new ImpressionLog().importFile(conn.connectDatabase(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void serverImportTest() throws SQLException {
    new ServerLog().importFile(conn.connectDatabase(), null);
  }
}
