/**
package db;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import group33.seg.db.DBRequest;

public class DBRequestTest {

    @Test
    public void dbRequestEqualityTest () throws Exception {
        DBRequest r1 = new DBRequest(DBRequest.impression);

        r1.setAxes(DBRequest.impression, "date", "count(*)", "5 day");
        r1.addEqualConstraint(DBRequest.impression, "gender", "Male");

        DBRequest r2 = new DBRequest(DBRequest.impression);

        r2.setAxes(DBRequest.impression, "date", "count(*)", "5 day");

        assertTrue(!r1.equals(r2));
        assertTrue(r1.hashCode() != r2.hashCode());
        assertTrue(!r1.getSql().equals(r2.hashCode()));


        r2.addEqualConstraint(DBRequest.impression, "gender", "Male");


        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
        assertTrue(r1.getSql().equals(r2.getSql()));

    }
}
*/
//TODO testing 