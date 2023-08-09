package com.search.search;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Database {

//    static{
//        Map<String, String> getenv = System.getenv();
//        boolean foundKey = false;
//        for( String key : getenv.keySet() ){
//            //System.out.println(key + " : " + getenv.get(key));
//            if( key.equals("SEARCH_PW") ){
//                foundKey = true;
//            }
//        }
//        System.out.println("Found key: " + foundKey);
//    }

    //Local mysql db connection string. Use databaseInit.sql to create the database on a local mysql server
    private static String connString = "jdbc:mysql://localhost:3306/searchengine?serverTimezone=UTC";

    //Make sure to create this user in the database
    private static final String user = "search_user";
    private static final String pw = System.getenv("SEARCH_PW");
    private Connection conn;

    public Database(){
        try {
            conn = DriverManager.getConnection(connString, user, pw);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeInDatabase(String url, String title, String content) {
        String sql = "INSERT INTO pages (url, title, content) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, url);
            stmt.setString(2, title);
            stmt.setString(3, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getVisitedUrls(){
        Set<String> visitedUrls = new HashSet<>();
        String sql = "SELECT url FROM pages";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                visitedUrls.add(resultSet.getString("url"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return visitedUrls;
    }

    public void deleteDuplicates(){
        String sql = "CALL delete_duplicate_content()";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    public Set<String> getStartUrls() {
        String sql = "SELECT url from start_urls";
        Set<String> start_urls = new HashSet<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                start_urls.add(resultSet.getString("url"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return start_urls;
    }

    public void saveRobots(Map<String, String> robots) {
        /*
        CREATE TABLE `robots` (
          `id` int NOT NULL AUTO_INCREMENT,
          `url` varchar(256) NOT NULL,
          `content` varchar(2048) NOT NULL,
          `last_crawled` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (`id`),
          UNIQUE KEY `url_UNIQUE` (`url`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
         */
        String sql = "INSERT INTO robots (url, content) VALUES (?, ?) ON DUPLICATE KEY UPDATE content = ?";
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (Map.Entry<String, String> entry : robots.entrySet()) {
                try {
                    stmt.setString(1, entry.getKey());
                    stmt.setString(2, entry.getValue());
                    stmt.setString(3, entry.getValue());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
