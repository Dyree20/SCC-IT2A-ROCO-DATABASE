package vls;

import java.util.Scanner;

public class VLS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VehicleLogSystem vls = new VehicleLogSystem();
        
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n--- Vehicle Log System Menu ---");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Add Vehicle Log");
            System.out.println("3. Display Vehicle Logs");
            System.out.println("4. Display Available Vehicles");
            System.out.println("5. Update Vehicle Log");
            System.out.println("6. Delete Vehicle Log");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Add a vehicle
                    System.out.print("Enter plate number: ");
                    String plateNo = scanner.nextLine();
                    System.out.print("Enter vehicle brand: ");
                    String brand = scanner.nextLine();
                    System.out.print("Enter vehicle model: ");
                    String model = scanner.nextLine();
                    
                    Vehicle vehicle = new Vehicle(plateNo, brand, model);
                    vls.addVehicle(vehicle);
                    break;
                    
                case 2:
                    
                    vls.displayAvailableVehicles();
                    
                    System.out.print("Enter plate number: ");
                    plateNo = scanner.nextLine();
                    System.out.print("Enter oil used: ");
                    String oilUsed = scanner.nextLine();
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter purpose of use: ");
                    String purpose = scanner.nextLine();
                    
                    Vehicle logVehicle = vls.getVehicleByPlateNo(plateNo);
                    if (logVehicle != null) {
                        VehicleLog log = new VehicleLog(logVehicle, oilUsed, date, purpose);
                        vls.addLog(log);
                    } else {
                        System.out.println("Vehicle with plate number " + plateNo + " not found.");
                    }
                    break;
                    
                case 3:
                    // Display logs
                    vls.displayLogs();
                    break;
                    
                case 4:
                    // Display available vehicles
                    vls.displayAvailableVehicles();
                    break;
                    
                case 5:
                    // Update a vehicle log
                    System.out.print("Enter plate number: ");
                    plateNo = scanner.nextLine();
                    System.out.print("Enter new date (YYYY-MM-DD): ");
                    String newDate = scanner.nextLine();
                    System.out.print("Enter new purpose of use: ");
                    String newPurpose = scanner.nextLine();
                    
                    vls.updateVehicleLog(plateNo, newDate, newPurpose);
                    break;
                    
                case 6:
                    // Delete a vehicle log
                     vls.displayLogs();
                    System.out.print("Enter the plate number of the log to delete: ");
                    String plateToDelete = scanner.nextLine();
                    vls.deleteLog(plateToDelete);
                    break;
                    
                case 0:
                    exit = true;
                    System.out.println("Exiting the system.");
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
}
