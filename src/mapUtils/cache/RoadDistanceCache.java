package mapUtils.cache;

import java.sql.*;

public final class RoadDistanceCache {
    private static RoadDistanceCache instance = null;
    public static final String DB_FILE_NAME = "apicache.db";
    
    public static String dbFileName;
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;
    
    public static RoadDistanceCache getInstance() {
        if (instance == null)
           instance = new RoadDistanceCache(DB_FILE_NAME);
        return instance;
    }
    
    private RoadDistanceCache() {
        this(DB_FILE_NAME);
    }
    
    private RoadDistanceCache(String dbFileName) {
        this.dbFileName = dbFileName;
        try {
            conn = DriverManager.getConnection(
                  "jdbc:sqlite:" + this.dbFileName);
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    /*
     *    CREATE TABLE road_distance_cache (
     *        origin_string NOT NULL,
     *        destination_string NOT NULL,
     *        road_distance,
     *        road_time,
     *        response,
     *        response_json,
     *        hits,
     *        date_added,
     *        misc_notes,
     *        PRIMARY KEY (origin_string, destination_string)
     *    );
     */
    
    public static String get(String origin, String destination) {
        String sqlSelect = (new StringBuilder("SELECT response_json "))
              .append("FROM road_distance_cache ")
              .append("WHERE origin_string=\'")
              .append(origin)
              .append("\' ")
              .append("AND destination_string=\'")
              .append(destination)
              .append("\';")
              .toString();
        
        String responseJson = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlSelect);
            if (!rs.next())
                return null;
            responseJson = rs.getString("response_json");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(responseJson);
        return responseJson;
    }

    public static void put(String origin, String destination, String responseJson)
    {
        String sqlString = (new StringBuilder("INSERT INTO road_distance_cache "))
              .append("(origin_string, destination_string, response_json) ")
              .append("VALUES (")
              .append("\'").append(origin).append("\', ")
              .append("\'").append(destination).append("\', ")
              .append("\'").append(responseJson).append("\'")
              .append(");").toString();
    
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
