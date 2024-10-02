
package vls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class  {
    public static void main(String[] args) {
        VehicleLogSystem vls = new VehicleLogSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Vehicle");
            System.out.println("2. Add Log");
            System.out.println("3. Display Logs");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter plate no: ");
                    String plateNo = scanner.next();
                    System.out.print("Enter brand: ");
                    String brand = scanner.next();
                    System.out.print("Enter model: ");
                    String model = scanner.next();
                    Vehicle vehicle = new Vehicle(plateNo, brand, model);
                    vls.addVehicle(vehicle);
                    break;
                case 2:
                    System.out.print("Enter plate no: ");
                    plateNo = scanner.next();
                    if (vls.isVehicleAvailable(plateNo)) {
                        System.out.print("Enter oil used: ");
                        String oilUsed = scanner.next();
                        System.out.print("Enter date: ");
                        String date = scanner.next();
                        System.out.print("Enter purpose of use: ");
                        String purposeOfUse = scanner.next();
                        VehicleLog log = new VehicleLog(vls.getVehicleByPlateNo(plateNo), oilUsed, date, purposeOfUse);
                        vls.addLog(log);
                    } else {
                        System.out.println("Vehicle unavailable");
                    }
                    break;
                case 3:
                    vls.displayLogs();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
