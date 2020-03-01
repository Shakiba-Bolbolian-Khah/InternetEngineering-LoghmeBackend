package Model;

import java.time.LocalTime;
import java.util.ArrayList;

enum State{
    Searching, Delivering, Done;
}

public class Order extends ShoppingCart {
    private int id;
    private String deliveryId;
    private State state;
    private LocalTime remainingTime;

    public Order(String restaurantId, String restaurantName, int totalPayment, boolean isFoodParty, ArrayList<ShoppingCartItem> items, int id, State state) {
        super(true, restaurantId, restaurantName, totalPayment, isFoodParty, items);
        this.id = id;
        this.deliveryId = null;
        this.state = state;
        this.remainingTime = null;
    }

    public int getId() {
        return id;
    }

    public void setDeliveryForOrder(String deliveryId, LocalTime remainingTime) {
        this.deliveryId = deliveryId;
        this.state = State.Delivering;
        this.remainingTime = remainingTime;
        // timer for delivering
    }

    public int getRemainingHoursAsInteger() {
        return remainingTime.getHour();
    }

    public int getRemainingMinutesAsInteger() {
        return remainingTime.getMinute();
    }

    public int getRemainingSecondsAsInteger() {
        return remainingTime.getSecond();
    }

    public String getStateAsString() {
        switch (state){
            case Searching:
                return "Searching";
            case Delivering:
                return "Delivering";
            case Done:
                return "Done";
            default:
                return "";
        }
    }
}