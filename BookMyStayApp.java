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

    public void display() {
        System.out.println("Guest: " + guestName + ", Room Type: " + roomType);
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public void displayQueue() {
        System.out.println("\n===== Booking Request Queue =====");
        for (Reservation r : queue) {
            r.display();
        }
    }

    public Queue<Reservation> getQueue() {
        return queue;
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        requestQueue.addRequest(new Reservation("Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("Bob", "Double Room"));
        requestQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        requestQueue.addRequest(new Reservation("David", "Single Room"));

        requestQueue.displayQueue();
    }
}