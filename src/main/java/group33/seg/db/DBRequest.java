package group33.seg.db;

import java.util.HashMap;
import java.util.HashSet;

public class DBRequest {
    private HashSet<String> tables;
    private HashMap<String, String> constraints;
    private String xAxis;
    private String yAxis;
    private String width;

    /**
     * table names
     */
    public static final String impression = "impression_log";
    public static final String server = "server_log";
    public static final String click = "click_log";

    public DBRequest (String table) {
        tables = new HashSet<String>();
        constraints = new HashMap<String, String>();
        xAxis = null;
        yAxis = null;
        width = null;

        join(table);
    }

    /**
     * xTable.xAxis is what we group by
     * you must specify the table when you use yAxis
     * otherwise expect weird results (ambiguous column names etc.)
     * give width as null unless it is a date, when you can use a layout like
     * '15 minute'
     */
    public DBRequest setAxes (String xTable, String xAxis, String yAxis, String width) {
        this.xAxis = xTable + "." + xAxis;
        this.yAxis = yAxis;

        if (xAxis == "date") {
            this.width = width;
        }

        return this;
    }

    /**
     * only the 3 tables that exist can be added
     */
    public DBRequest join (String table) {
        switch (table) {
            case impression:
            case server:
            case click:     tables.add(table);
            default:        break;
        }
        return this;
    }

    private String formatString (String input) {
        try {
            Double.parseDouble(input);
            return input;
        } catch (NumberFormatException e) {
            return "'" + input + "'";
        } 
    }

    public DBRequest addEqualConstraint (String table, String var, String value) {
        return addConstraint(table, var, "= " + formatString(value));
    }

    public DBRequest addNotEqualConstraint (String table, String var, String value) {
        return addConstraint(table, var, "<> " + formatString(value));
    }

    public DBRequest addLessConstraint (String table, String var, String value) {
        return addConstraint(table, var, "< " + formatString(value));
    }

    public DBRequest addMoreConstraint (String table, String var, String value) {
        return addConstraint(table, var, "> " + formatString(value));
    }

    public DBRequest addLikeConstraint (String table, String var, String value) {
        return addConstraint(table, var, "LIKE " + formatString(value));
    }

    private DBRequest addConstraint (String table, String var, String value) {
        constraints.put(table + "." + var, value);
        return this;
    }

    @Override
    public int hashCode () {
        int intermediate =  tables.hashCode() * constraints.hashCode();
        
        if(xAxis != null){
            intermediate *= xAxis.hashCode();
        }

        if(yAxis != null){
            intermediate *= yAxis.hashCode();
        }
        
        if(width != null) {
            intermediate *= width.hashCode();
        }

        return intermediate;
    }

    public String getSql() {
        String sql;
        //deal with date_trunc
        if (width != null) {
            sql = sortDateTrunc();
            sql += "," + this.xAxis + ") as xaxis";
        } else {
            sql = "select " + this.xAxis + " as xaxis";
        }

        sql += ", " + this.yAxis;

        sql += " " + fromStatement();

        //constraints
        Boolean first = true;
        for (String var : constraints.keySet()) {
            if (first) {
                first = false;
                sql += " where " + var + " " + constraints.get(var);
            } else {
                sql += " and " + var + " " + constraints.get(var);
            }
        }

        sql += " group by xaxis";
        return sql;
    }

    private String sortDateTrunc() {
        String[] splitNumber = width.split(" ");
        String timeUnit = splitNumber[splitNumber.length - 1];
        
        String sql = "select date_trunc(";
        switch (timeUnit) {
            case "second":
            case "seconds": sql += "'second'";
                            break;
            case "minute":
            case "minutes": sql += "'minute'";
                            break;
            case "hour":
            case "hours":   sql += "'hour'";
                            break;
            case "day":
            case "days":    sql += "'day'";
                            break;
            case "week":
            case "weeks":   sql += "'week'";
                            break;
            case "month":
            case "months":  sql += "'month'";
                            break;
            case "year":
            case "years":   sql += "'year'";
                            break;
        }

        return sql;
    }

    private String fromStatement() {
        Boolean first = true;
        String from = "";
        String firstTable = "";
        Integer brackets = 0;
        for (String table : tables) {
            if (first && tables.size() == 3) {
                first = false;
                firstTable = table;
                from = "from (" + table;
                brackets = 1;
            } else if (first) {
                first = false;
                firstTable = table;
                from = "from " + table;
            } else if (brackets > 1){
                brackets--;
                from += " inner join " + table + " on " + table + ".id = " + firstTable + ".id)";
            } else {
                from += " inner join " + table + " on " + table + ".id = " + firstTable + ".id";
            }
        }

        return from;
    }

    public HashMap<String, Integer> fixResult (HashMap<String, Integer> result) {
        return result;
    }
}