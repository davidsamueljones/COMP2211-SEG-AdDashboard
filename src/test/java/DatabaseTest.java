import group33.seg.controller.database.*;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

  @Mock private DatabaseConnection dbConn;

  @Mock private DatabaseConfig config;

  @Mock private Connection conn;

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

  @Test
  public void clearTableTest() throws SQLException {
    ServerLogTable slt = new ServerLogTable();
    Statement stmt = mock(Statement.class);

    when(conn.createStatement()).thenReturn(stmt);

    slt.clearTable(conn);
    verify(conn, times(1)).createStatement();
    verify(stmt, times(1)).execute(contains("TRUNCATE TABLE "));
  }

  @Test
  public void groupingTest() {

    // ensures that any future Intervals will be included in this method
    for (Interval interval : Interval.values()) {
      MetricQuery mq =
          new MetricQuery(mock(CampaignConfig.class), Metric.IMPRESSIONS, interval, null);
      String res = DatabaseQueryFactory.generateSQL(mq);

      assertTrue(res.contains("<interval>"));
    }
  }
}
