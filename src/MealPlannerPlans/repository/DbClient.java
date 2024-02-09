package MealPlannerPlans.repository;

import java.sql.*;

public class DbClient {
    private final String DB_URL = "jdbc:postgresql:meals_db";
    private final String USER = "postgres";
    private final String PASS = "1111";

    private Connection connection;
    private Statement statement;

    DbClient() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        connection.setAutoCommit(true);

        statement = connection.createStatement();
    }

    public void executeUpdate(String query) throws SQLException {
        statement.executeUpdate(query);
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }

}
