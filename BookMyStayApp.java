import java.io.*;
import java.util.*;

class Reservation implements Serializable {
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

class RoomInventory implements Serializable {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r.getReservationId() + " | " + r.getGuestName() + " | " + r.getRoomType());
        }
    }
}

class SystemState implements Serializable {
    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

class PersistenceService {
    private static final String FILE_NAME = "system_state.ser";

    public void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(state);
            System.out.println("\nState saved successfully.");
        } catch (IOException e) {
            System.out.println("\nError saving state: " + e.getMessage());
        }
    }

    public SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) in.readObject();
            System.out.println("\nState loaded successfully.");
            return state;
        } catch (Exception e) {
            System.out.println("\nNo previous state found. Starting fresh.");
            return null;
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        SystemState state = persistence.load();

        RoomInventory inventory;
        BookingHistory history;

        if (state != null) {
            inventory = state.inventory;
            history = state.history;
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        history.add(new Reservation("R101", "Alice", "Single Room"));
        history.add(new Reservation("R102", "Bob", "Double Room"));

        inventory.getInventory().put("Single Room", 1);

        history.display();
        inventory.display();

        persistence.save(new SystemState(inventory, history));
    }
}