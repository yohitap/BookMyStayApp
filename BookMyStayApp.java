import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int available = getAvailability(roomType);

        if (available < 0) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (available == 0) {
            throw new InvalidBookingException("No availability for: " + roomType);
        }

        inventory.put(roomType, available - 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }
}

class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {
        try {
            validate(r);
            inventory.decrement(r.getRoomType());
            System.out.println("Booking confirmed for " + r.getGuestName() + " (" + r.getRoomType() + ")");
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed for " + r.getGuestName() + ": " + e.getMessage());
        }
    }

    private void validate(Reservation r) throws InvalidBookingException {
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected");
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        service.processBooking(new Reservation("Alice", "Single Room"));
        service.processBooking(new Reservation("Bob", "Suite Room"));
        service.processBooking(new Reservation("", "Double Room"));
        service.processBooking(new Reservation("Charlie", "Single Room"));
        service.processBooking(new Reservation("David", "Single Room"));
    }
}