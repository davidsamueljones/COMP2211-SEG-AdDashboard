package group33.seg;
import group33.seg.db.DBInterface;
import group33.seg.db.DBRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        DBRequest r1 = new DBRequest(DBRequest.impression);

        r1.setAxes(DBRequest.impression, "date", "count(*)", "day");
        r1.addEqualConstraint(DBRequest.impression, "gender", "Male");
        System.out.println(r1.getSql());

        DBInterface dbi = new DBInterface();
        Future<LinkedHashMap<String, Integer>> theFuture = dbi.call(r1);
        try {
            LinkedHashMap<String, Integer> result = theFuture.get();
            for(String key : result.keySet()) {
                System.out.println(key + " " + result.get(key));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
       
    }
}
