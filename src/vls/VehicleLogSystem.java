package vls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleLogSystem {
    private Connection connection;

    public VehicleLogSystem() {
        connection = connectDB();
        createTable();
    }

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:vls.db");
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return con;
    }

    private void createTable() {
        // Creating vehicles table
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS vehicles (plate_no VARCHAR(255) PRIMARY KEY, brand VARCHAR(255), model VARCHAR(255));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating vehicles table: " + e.getMessage());
        }

        // Creating vehicle_logs table
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS vehicle_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, plate_no VARCHAR(255), oil_used VARCHAR(255), date VARCHAR(255), purpose_of_use VARCHAR(255), status VARCHAR(20), FOREIGN KEY(plate_no) REFERENCES vehicles(plate_no));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating vehicle_logs table: " + e.getMessage());
        }
    }

    public void addVehicle(Vehicle vehicle) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO vehicles (plate_no, brand, model) VALUES (?, ?, ?)")) {
            statement.setString(1, vehicle.getPlateNo());
            statement.setString(2, vehicle.getBrand());
            statement.setString(3, vehicle.getModel());
            statement.executeUpdate();
            System.out.println("Vehicle added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    public void addLog(VehicleLog log) {
        try {
            // Check if the vehicle is already in use
            try (PreparedStatement checkStmt = connection.prepareStatement(
                    "SELECT status FROM vehicle_logs WHERE plate_no = ? ORDER BY id DESC LIMIT 1")) {
                checkStmt.setString(1, log.getVehicle().getPlateNo());
                ResultSet resultSet = checkStmt.executeQuery();

                if (resultSet.next()) {
                    String status = resultSet.getString("status");
                    if ("in use".equalsIgnoreCase(status)) {
                        System.out.println("Vehicle is currently in use. Cannot add a new log.");
                        return;
                    }
                }
            }

            // Insert new log
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO vehicle_logs (plate_no, oil_used, date, purpose_of_use, status) VALUES (?, ?, ?, ?, 'in use')")) {
                statement.setString(1, log.getVehicle().getPlateNo());
                statement.setString(2, log.getOilUsed());
                statement.setString(3, log.getDate());
                statement.setString(4, log.getPurposeOfUse());
                statement.executeUpdate();
                System.out.println("Log added successfully. Vehicle marked as 'in use'.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding log: " + e.getMessage());
        }
    }

    public Vehicle getVehicleByPlateNo(String plateNo) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicles WHERE plate_no = ?")) {
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

    public void displayLogs() {
        System.out.println("\n--- Vehicle Logs ---");
        System.out.printf("%-15s %-15s %-10s %-30s %-15s%n", "Plate No", "Oil Used", "Date", "Purpose", "Status");
        System.out.println("-----------------------------------------------------------");

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle_logs")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.printf("%-15s %-15s %-10s %-30s %-15s%n",
                        resultSet.getString("plate_no"),
                        resultSet.getString("oil_used"),
                        resultSet.getString("date"),
                        resultSet.getString("purpose_of_use"),
                        resultSet.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying logs: " + e.getMessage());
        }
    }

    public void displayAvailableVehicles() {
        System.out.println("\n--- Vehicle Availability ---");
        System.out.printf("%-15s %-15s %-10s %-15s%n", "Plate No", "Brand", "Model", "Status");
        System.out.println("--------------------------------------------------------------");

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT v.plate_no, v.brand, v.model, " +
                "(SELECT status FROM vehicle_logs WHERE plate_no = v.plate_no ORDER BY id DESC LIMIT 1) AS status " +
                "FROM vehicles v")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String status = resultSet.getString("status");
                if (status == null || !"in use".equalsIgnoreCase(status)) {
                    status = "available"; // Default to "available" if there's no log entry or status is not "in use"
                }
                System.out.printf("%-15s %-15s %-10s %-15s%n",
                        resultSet.getString("plate_no"),
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        status);
            }
        } catch (SQLException e) {
            System.out.println("Error displaying available vehicles: " + e.getMessage());
        }
    }

    public void updateVehicleLog(String plateNo, String newDate, String newPurpose) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE vehicle_logs SET date = ?, purpose_of_use = ? WHERE plate_no = ?")) {
            statement.setString(1, newDate);
            statement.setString(2, newPurpose);
            statement.setString(3, plateNo);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Vehicle log updated successfully.");
            } else {
                System.out.println("No vehicle log found for the given plate number.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating vehicle log: " + e.getMessage());
        }
    }

    // Method to delete a vehicle log
    public void deleteLog(String plateNo) {
        try {
            // Check if the log exists
            try (PreparedStatement checkLogStmt = connection.prepareStatement(
                    "SELECT * FROM vehicle_logs WHERE plate_no = ?")) {
                checkLogStmt.setString(1, plateNo);
                ResultSet resultSet = checkLogStmt.executeQuery();

                if (!resultSet.next()) {
                    System.out.println("No log found for the given plate number.");
                    return;
                }
            }

            // Delete the log from the vehicle_logs table
            try (PreparedStatement deleteStmt = connection.prepareStatement(
                    "DELETE FROM vehicle_logs WHERE plate_no = ?")) {
                deleteStmt.setString(1, plateNo);
                int rowsDeleted = deleteStmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Vehicle log deleted successfully.");
                } else {
                    System.out.println("No vehicle log found for the given plate number.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting log: " + e.getMessage());
        }
    }
}
