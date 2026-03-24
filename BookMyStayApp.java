import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

class BookingService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Set<String> allRoomIds = new HashSet<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void process(BookingRequestQueue queue) {
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                String roomId = generateRoomId(type);

                allRoomIds.add(roomId);

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomId);

                inventory.decrement(type);

                System.out.println("Confirmed: " + r.getGuestName() + " -> " + type + " | Room ID: " + roomId);
            } else {
                System.out.println("Failed: " + r.getGuestName() + " -> " + type + " (No availability)");
            }
        }
    }

    private String generateRoomId(String type) {
        String prefix = type.substring(0, 2).toUpperCase();
        String id;
        do {
            id = prefix + (int)(Math.random() * 1000);
        } while (allRoomIds.contains(id));
        return id;
    }

    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> e : allocatedRooms.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Suite Room"));
        queue.addRequest(new Reservation("David", "Single Room"));
        queue.addRequest(new Reservation("Eve", "Suite Room"));

        RoomInventory inventory = new RoomInventory();

        BookingService service = new BookingService(inventory);

        service.process(queue);

        service.displayAllocations();

        inventory.display();
    }
}