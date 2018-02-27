package group33.seg.db;

import java.util.HashMap;

public abstract class DBInterface {
    protected String url;
    protected String dbName;

    private HashMap<Integer, HashMap<String, Integer>> previous;

    public DBInterface (String url, String dbName){
        previous = new HashMap<Integer, HashMap<String, Integer>>();
    }

    public HashMap<String, Integer> getHistogram(DBRequest request) {
        if (previous.get(request.hashCode()) != null) {
            return previous.get(request.hashCode());
        } else {
            return getNewHistogram(request);
        }
    }

    protected abstract HashMap<String, Integer> getNewHistogram(DBRequest request);
}