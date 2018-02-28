package group33.seg;
import group33.seg.db.DBInterface;
import group33.seg.db.DBRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.util.concurrent.Future;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        DBRequest r1 = new DBRequest(DBRequest.impression);

        r1.addEqualConstraint("test", "test", "5");
        r1.addLikeConstraint("test", "meme", "dream");

        r1.setAxes("test", "test", "count(test)", null);

        DBInterface dbi = new DBInterface();
        Future<HashMap<String, Integer>> theFuture = dbi.call(r1);
        try {
            HashMap<String, Integer> result = theFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
}
