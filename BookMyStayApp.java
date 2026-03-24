import java.util.*;

class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        System.out.println("\nServices for Reservation ID: " + reservationId);
        for (AddOnService s : services) {
            System.out.println(s.getName() + " - ₹" + s.getCost());
        }
        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        String reservationId1 = "SR101";
        String reservationId2 = "DR202";

        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 300);
        AddOnService spa = new AddOnService("Spa", 1000);

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(reservationId1, wifi);
        manager.addService(reservationId1, breakfast);

        manager.addService(reservationId2, spa);
        manager.addService(reservationId2, breakfast);

        manager.displayServices(reservationId1);
        manager.displayServices(reservationId2);
    }
}