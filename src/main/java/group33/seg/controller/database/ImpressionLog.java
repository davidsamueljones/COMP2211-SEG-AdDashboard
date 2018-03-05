package group33.seg.controller.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

public class ImpressionLog implements DatabaseTable {

  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS impression_log (date TIMESTAMP,"
            + "user_id BIGINT NOT NULL, "
            + "female BOOLEAN, "
            + "age age, "
            + "income income, "
            + "context context, "
            + "impression_cost REAL)");
    st.close();
  }

  @Override
  public void importFile(Connection c, String filepath) throws SQLException {
    if (filepath == null) {
      throw new IllegalArgumentException("Filepath cannot be null!");
    }

    try {
      Stream<String> linesIn = Files.lines(Paths.get(filepath));
      Stream<String> linesOut =
          linesIn
              .map(s -> s.replace(",Male,", ",false,"))
              .map(s -> s.replace(",Female,", ",true,"));

      Files.write(
          Paths.get(filepath + ".tmp"),
          (Iterable<String>) linesOut::iterator,
          StandardOpenOption.CREATE);

      linesIn.close();
      linesOut.close();

      Statement st = c.createStatement();
      st.execute("COPY impression_log FROM '" + filepath + ".tmp' WITH DELIMITER ',' CSV HEADER");
      st.close();

      Files.delete(Paths.get(filepath + ".tmp"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
