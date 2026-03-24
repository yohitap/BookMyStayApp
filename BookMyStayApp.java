import java.util.*;

abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1500.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2500.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Price: ₹" + getPrice());
    }
}

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: unavailable
    }

    
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    
    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}


class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, List<Room> rooms) {

        System.out.println("\n===== Available Rooms =====");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());

            
            if (available > 0) {
                System.out.println("\n------------------------");
                room.displayDetails();
                System.out.println("Available: " + available);
            }
        }
    }
}


public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Room Search System =====");

        
        RoomInventory inventory = new RoomInventory();

        
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        
        SearchService searchService = new SearchService();

        
        searchService.searchAvailableRooms(inventory, rooms);

        System.out.println("\nSearch completed. (No inventory modified)");
    }
}