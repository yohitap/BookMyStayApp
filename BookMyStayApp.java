import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

class BookingHistory {
    private Map<String, Reservation> confirmed = new HashMap<>();
    private Set<String> cancelled = new HashSet<>();

    public void add(Reservation r) {
        confirmed.put(r.getReservationId(), r);
    }

    public Reservation get(String id) {
        return confirmed.get(id);
    }

    public boolean isCancelled(String id) {
        return cancelled.contains(id);
    }

    public void markCancelled(String id) {
        cancelled.add(id);
    }
}

class BookingService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Map<String, String> allocation = new HashMap<>();
    private Set<String> usedRoomIds = new HashSet<>();

    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void confirm(Reservation r) {
        if (inventory.getAvailability(r.getRoomType()) > 0) {
            String roomId = generateRoomId(r.getRoomType());
            allocation.put(r.getReservationId(), roomId);
            usedRoomIds.add(roomId);
            inventory.decrement(r.getRoomType());
            history.add(r);
            System.out.println("Confirmed: " + r.getReservationId() + " -> " + roomId);
        } else {
            System.out.println("Booking failed: No availability");
        }
    }

    private String generateRoomId(String type) {
        String prefix = type.substring(0, 2).toUpperCase();
        String id;
        do {
            id = prefix + (int)(Math.random() * 1000);
        } while (usedRoomIds.contains(id));
        return id;
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Map<String, String> allocation;
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history, Map<String, String> allocation) {
        this.inventory = inventory;
        this.history = history;
        this.allocation = allocation;
    }

    public void cancel(String reservationId) {
        Reservation r = history.get(reservationId);

        if (r == null) {
            System.out.println("Cancellation failed: Reservation not found");
            return;
        }

        if (history.isCancelled(reservationId)) {
            System.out.println("Cancellation failed: Already cancelled");
            return;
        }

        String roomId = allocation.get(reservationId);
        rollbackStack.push(roomId);

        inventory.increment(r.getRoomType());
        history.markCancelled(reservationId);

        System.out.println("Cancelled: " + reservationId + " | Released Room ID: " + rollbackStack.peek());
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(inventory, history);

        Reservation r1 = new Reservation("R101", "Alice", "Single Room");
        Reservation r2 = new Reservation("R102", "Bob", "Double Room");

        bookingService.confirm(r1);
        bookingService.confirm(r2);

        Map<String, String> allocationMap = new HashMap<>();
        allocationMap.put("R101", "SI100");
        allocationMap.put("R102", "DO200");

        CancellationService cancelService = new CancellationService(inventory, history, allocationMap);

        cancelService.cancel("R101");
        cancelService.cancel("R101");
        cancelService.cancel("R999");

        inventory.display();
    }
}