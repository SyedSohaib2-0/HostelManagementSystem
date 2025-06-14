package com.hostel1;

import com.hostel.database.DatabaseConnection;
import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Test: Connection is successful.");
        } else {
            System.out.println("Test: Connection failed.");
        }
        DatabaseConnection.closeConnection();
    }
}
