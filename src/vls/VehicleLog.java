
package vls;


public class VehicleLog {
    private Vehicle vehicle;
    private String oilUsed;
    private String date;
    private String purposeOfUse;

    public VehicleLog(Vehicle vehicle, String oilUsed, String date, String purposeOfUse) {
        this.vehicle = vehicle;
        this.oilUsed = oilUsed;
        this.date = date;
        this.purposeOfUse = purposeOfUse;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getOilUsed() {
        return oilUsed;
    }

    public String getDate() {
        return date;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    
         }