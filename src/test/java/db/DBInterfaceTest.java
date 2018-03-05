/**
package db;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import group33.seg.db.DBRequest;
import group33.seg.db.DBInterface;

import java.util.LinkedHashMap;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DBInterfaceTest {

    @Test
    public void basicSQLTest(){
        LinkedHashMap<String, Integer> expected = new LinkedHashMap<String, Integer>();
        expected.put("Female", 4414304);
        expected.put("Male", 4413944);

        DBRequest r1 = new DBRequest(DBRequest.impression);

        r1.setAxes(DBRequest.impression, "gender", "count(*)", null);

        DBInterface dbi = new DBInterface();
        Future<LinkedHashMap<String, Integer>> theFuture = dbi.call(r1);
        try {
            LinkedHashMap<String, Integer> result = theFuture.get();
            assertTrue(result.hashCode() == expected.hashCode());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void groupingTest(){
        LinkedHashMap<String, Integer> expected = new LinkedHashMap<String, Integer>();
        expected.put("2015-01-01 00:00:00", 249626);
        expected.put("2015-01-06 00:00:00", 299477);
        expected.put("2015-01-11 00:00:00", 303342);
        expected.put("2015-01-16 00:00:00", 243774);
        expected.put("2015-01-21 00:00:00", 308588);
        expected.put("2015-01-26 00:00:00", 312816);
        expected.put("2015-01-31 00:00:00", 329824);
        expected.put("2015-02-05 00:00:00", 417923);
        expected.put("2015-02-10 00:00:00", 450354);
        expected.put("2015-02-15 00:00:00", 537500);
        expected.put("2015-02-20 00:00:00", 571053);
        expected.put("2015-02-25 00:00:00", 389667);

        DBRequest r1 = new DBRequest(DBRequest.impression);

        r1.setAxes(DBRequest.impression, "date", "count(*)", "5 day");
        r1.addEqualConstraint(DBRequest.impression, "gender", "Male");

        DBInterface dbi = new DBInterface();
        Future<LinkedHashMap<String, Integer>> theFuture = dbi.call(r1);
        try {
            LinkedHashMap<String, Integer> result = theFuture.get();
            assertTrue(result.hashCode() == expected.hashCode());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
 */