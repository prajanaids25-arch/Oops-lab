import java.util.ArrayList;
import java.util.List;

/*
 * Register No : 80
 * Mini Project: Vehicle Service Management System
 * Operations  : Vehicle registration; service selection
 * Output      : Display vehicle, services performed and service bill
 * Concepts    : Class & Object, Inheritance, Polymorphism, Encapsulation
 */

// ---------------------------------------------------------------
// Main class
// ---------------------------------------------------------------
public class VehicleServiceManagementSystem {
    public static void main(String[] args) {
        System.out.println("**************************************************");
        System.out.println("     VEHICLE SERVICE MANAGEMENT SYSTEM");
        System.out.println("**************************************************");
        System.out.println();

        // 1. Register vehicles
        Vehicle bike  = new TwoWheeler("TN 66 AB 1234", "Arun Kumar", "Honda Shine");
        Vehicle car   = new Car("TN 37 CD 5678", "Priya Sharma", "Hyundai i20");
        Vehicle truck = new HeavyVehicle("TN 38 EF 9012", "Ravi Transports", "Tata 407");

        // 2. Select services for each vehicle
        ServiceRecord r1 = new ServiceRecord(bike);
        r1.addService(new GeneralService());
        r1.addService(new OilChange());
        r1.addService(new WashAndPolish());

        ServiceRecord r2 = new ServiceRecord(car);
        r2.addService(new GeneralService());
        r2.addService(new BrakeRepair());
        r2.addService(new TyreReplacement());

        ServiceRecord r3 = new ServiceRecord(truck);
        r3.addService(new OilChange());
        r3.addService(new BrakeRepair());

        // 3. Display vehicle, services performed and bill
        r1.printBill();
        r2.printBill();
        r3.printBill();

        System.out.println("Thank you for choosing our service center!");
    }
}

// ---------------------------------------------------------------
// Base class: Vehicle (Encapsulation - private fields + getters)
// ---------------------------------------------------------------
abstract class Vehicle {
    private String registrationNo;
    private String ownerName;
    private String model;

    public Vehicle(String registrationNo, String ownerName, String model) {
        this.registrationNo = registrationNo;
        this.ownerName = ownerName;
        this.model = model;
    }

    public String getRegistrationNo() { return registrationNo; }
    public String getOwnerName()      { return ownerName; }
    public String getModel()          { return model; }

    // Overridden by every child class (Polymorphism)
    public abstract String getVehicleType();
    public abstract double getLabourRate();      // base labour charge per service
    public abstract double getPartsMultiplier(); // cost factor for parts

    public void displayDetails() {
        System.out.println("Vehicle Type    : " + getVehicleType());
        System.out.println("Registration No : " + registrationNo);
        System.out.println("Model           : " + model);
        System.out.println("Owner           : " + ownerName);
    }
}

// ---------------------------------------------------------------
// Child classes of Vehicle (Inheritance)
// ---------------------------------------------------------------
class TwoWheeler extends Vehicle {
    public TwoWheeler(String regNo, String owner, String model) {
        super(regNo, owner, model);
    }
    @Override public String getVehicleType()     { return "Two Wheeler"; }
    @Override public double getLabourRate()      { return 150.0; }
    @Override public double getPartsMultiplier() { return 1.0; }
}

class Car extends Vehicle {
    public Car(String regNo, String owner, String model) {
        super(regNo, owner, model);
    }
    @Override public String getVehicleType()     { return "Car"; }
    @Override public double getLabourRate()      { return 400.0; }
    @Override public double getPartsMultiplier() { return 2.0; }
}

class HeavyVehicle extends Vehicle {
    public HeavyVehicle(String regNo, String owner, String model) {
        super(regNo, owner, model);
    }
    @Override public String getVehicleType()     { return "Heavy Vehicle"; }
    @Override public double getLabourRate()      { return 800.0; }
    @Override public double getPartsMultiplier() { return 3.5; }
}

// ---------------------------------------------------------------
// Base class: Service (Encapsulation)
// ---------------------------------------------------------------
abstract class Service {
    private String serviceName;
    private double basePartsCost;

    public Service(String serviceName, double basePartsCost) {
        this.serviceName = serviceName;
        this.basePartsCost = basePartsCost;
    }

    public String getServiceName()  { return serviceName; }
    public double getBasePartsCost() { return basePartsCost; }

    // Each service calculates its charge differently (Polymorphism)
    public abstract double calculateCharge(Vehicle vehicle);
}

// ---------------------------------------------------------------
// Child classes of Service (Inheritance + Method Overriding)
// ---------------------------------------------------------------
class GeneralService extends Service {
    public GeneralService() { super("General Service", 500.0); }

    @Override
    public double calculateCharge(Vehicle v) {
        return v.getLabourRate() + getBasePartsCost() * v.getPartsMultiplier();
    }
}

class OilChange extends Service {
    public OilChange() { super("Engine Oil Change", 350.0); }

    @Override
    public double calculateCharge(Vehicle v) {
        return (v.getLabourRate() * 0.5) + getBasePartsCost() * v.getPartsMultiplier();
    }
}

class BrakeRepair extends Service {
    public BrakeRepair() { super("Brake Repair", 700.0); }

    @Override
    public double calculateCharge(Vehicle v) {
        return (v.getLabourRate() * 1.5) + getBasePartsCost() * v.getPartsMultiplier();
    }
}

class TyreReplacement extends Service {
    public TyreReplacement() { super("Tyre Replacement", 1200.0); }

    @Override
    public double calculateCharge(Vehicle v) {
        return (v.getLabourRate() * 1.2) + getBasePartsCost() * v.getPartsMultiplier();
    }
}

class WashAndPolish extends Service {
    public WashAndPolish() { super("Wash & Polish", 100.0); }

    @Override
    public double calculateCharge(Vehicle v) {
        // Labour only for washing; small material cost
        return (v.getLabourRate() * 0.4) + getBasePartsCost();
    }
}

// ---------------------------------------------------------------
// ServiceRecord: one vehicle + its selected services + the bill
// ---------------------------------------------------------------
class ServiceRecord {
    private static final double GST_RATE = 0.18;

    private Vehicle vehicle;
    private List<Service> services = new ArrayList<>();

    public ServiceRecord(Vehicle vehicle) {          // Vehicle registration
        this.vehicle = vehicle;
    }

    public void addService(Service s) {              // Service selection
        services.add(s);
    }

    public void printBill() {
        System.out.println("==================================================");
        System.out.println("          VEHICLE SERVICE BILL");
        System.out.println("==================================================");
        vehicle.displayDetails();
        System.out.println("--------------------------------------------------");
        System.out.printf("%-4s %-25s %15s%n", "No.", "Service Performed", "Charge (Rs.)");
        System.out.println("--------------------------------------------------");

        double subtotal = 0;
        int count = 1;
        for (Service s : services) {
            double charge = s.calculateCharge(vehicle);   // Polymorphic call
            subtotal += charge;
            System.out.printf("%-4d %-25s %15.2f%n", count++, s.getServiceName(), charge);
        }

        double gst = subtotal * GST_RATE;
        double total = subtotal + gst;

        System.out.println("--------------------------------------------------");
        System.out.printf("%-30s %15.2f%n", "Sub Total", subtotal);
        System.out.printf("%-30s %15.2f%n", "GST (18%)", gst);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-30s %15.2f%n", "TOTAL SERVICE BILL", total);
        System.out.println("==================================================");
        System.out.println();
    }
}
