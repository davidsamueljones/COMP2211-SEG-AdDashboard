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

    @Override
    public boolean equals (Object other) {
        if(other instanceof DBRequest) {
            return this.hashCode() == other.hashCode();
        } else {
            return false;
        }
    }

    public String getSql() {
        StringBuilder sql = new StringBuilder();
        //deal with date_trunc
        if (width != null) {
            sql.append(sortDateTrunc());
            sql.append(",");
            sql.append(this.xAxis);
            sql.append(") as xaxis");
        } else {
            sql.append("select ");
            sql.append(this.xAxis);
            sql.append(" as xaxis");
        }

        sql.append(", ");
        sql.append(this.yAxis);

        sql.append("as yaxis ");
        sql.append(fromStatement());

        //constraints
        Boolean first = true;
        for (String var : constraints.keySet()) {
            if (first) {
                first = false;
                sql.append(" where ");
                sql.append(var);
                sql.append(" ");
                sql.append(constraints.get(var));
            } else {
                sql.append(" and ");
                sql.append(var);
                sql.append(" ");
                sql.append(constraints.get(var));
            }
        }

        sql.append(" group by xaxis order by xaxis");
        return sql.toString();
    }

    private String sortDateTrunc() {
        String[] splitNumber = width.split(" ");
        String timeUnit = splitNumber[splitNumber.length - 1];
        
        StringBuilder sql = new StringBuilder();
        sql.append("select date_trunc(");

        switch (timeUnit) {
            case "second":
            case "seconds": sql.append("'second'");
                            break;
            case "minute":
            case "minutes": sql.append("'minute'");
                            break;
            case "hour":
            case "hours":   sql.append("'hour'");
                            break;
            case "day":
            case "days":    sql.append("'day'");
                            break;
            case "week":
            case "weeks":   sql.append("'week'");
                            break;
            case "month":
            case "months":  sql.append("'month'");
                            break;
            case "year":
            case "years":   sql.append("'year'");
                            break;
        }

        return sql.toString();
    }

    private String fromStatement() {
        boolean first = true;
        StringBuilder from = new StringBuilder();
        String firstTable = "";
        Integer brackets = 0;
        boolean needsNestedJoin = tables.size() == 3;
        for (String table : tables) {
            if (first && needsNestedJoin) {
                first = false;
                firstTable = table;
                from.append("from (");
                from.append(table);
                brackets = 1;
            } else if (first) {
                first = false;
                firstTable = table;
                from.append("from ");
                from.append(table);
            } else if (brackets > 1){
                brackets--;
                from.append(" inner join ");
                from.append(table);
                from.append(" on ");
                from.append(table);
                from.append(".id = ");
                from.append(firstTable);
                from.append(".id)");
            } else {
                from.append(" inner join ");
                from.append(table);
                from.append(" on ");
                from.append(table);
                from.append(".id = ");
                from.append(firstTable);
                from.append(".id");
            }
        }

        return from.toString();
    }

    /**
     * yet to be implemented
     */
    public HashMap<String, Integer> fixResult (HashMap<String, Integer> result) {
        return result;
    }
}