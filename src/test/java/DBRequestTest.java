import org.junit.Test;

import static org.junit.Assert.assertTrue;
import group33.seg.db.DBRequest;

public class DBRequestTest {

    @Test
    public void dbRequestTest () throws Exception {
        DBRequest r1 = new DBRequest(DBRequest.impression);
        DBRequest r2 = new DBRequest(DBRequest.impression);

        assertTrue(r1.hashCode() == r2.hashCode());

        r1.addEqualConstraint("test", "test", "5");
        r2.addLikeConstraint("test", "meme", "dream");
        r2.addEqualConstraint("test", "test", "5");
        r1.addLikeConstraint("test", "meme", "dream");

        r1.setAxes("test", "test", "count(test)", null);
        assertTrue(r1.hashCode() != r2.hashCode());

        r2.setAxes("test", "test", "count(test)", null);
        assertTrue(r1.hashCode() == r2.hashCode());
    }
}
