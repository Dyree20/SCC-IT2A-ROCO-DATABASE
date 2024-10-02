package vls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleLogSystem {
    private Connection connection;

     public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); 
            con = DriverManager.getConnection("jdbc:sqlite:vls.db"); 
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    private void createTable() {
        try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS vehicle_log (id INT AUTO_INCREMENT, plate_no VARCHAR(255), brand VARCHAR(255), model VARCHAR(255), oil_used VARCHAR(255), date VARCHAR(255), purpose_of_use VARCHAR(255), PRIMARY KEY (id))")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void addVehicle(Vehicle vehicle) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO vehicle_log (plate_no, brand, model) VALUES (?, ?, ?)")) {
            statement.setString(1, vehicle.getPlateNo());
            statement.setString(2, vehicle.getBrand());
            statement.setString(3, vehicle.getModel());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    public void addLog(VehicleLog log) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO vehicle_log (plate_no, oil_used, date, purpose_of_use) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, log.getVehicle().getPlateNo());
            statement.setString(2, log.getOilUsed());
            statement.setString(3, log.getDate());
            statement.setString(4, log.getPurposeOfUse());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding log: " + e.getMessage());
        }
    }

    public Vehicle getVehicleByPlateNo(String plateNo) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle_log WHERE plate_no = ?")) {
            statement.setString(1, plateNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Vehicle(resultSet.getString("plate_no"), resultSet.getString("brand"), resultSet.getString("model"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting vehicle: " + e.getMessage());
        }
        return null;
    }
}