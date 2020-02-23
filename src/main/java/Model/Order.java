package Model;

import java.util.ArrayList;

enum State{
    Searching, Delivering, Done;
}

public class Order extends ShoppingCart {
    private int id;
    private String deliveryId;
    State state;

    public Order(String restaurantId, String restaurantName, int totalPayment, boolean isFoodParty, ArrayList<ShoppingCartItem> items, int id, State state) {
        super(true, restaurantId, restaurantName, totalPayment, isFoodParty, items);
        this.id = id;

        this.deliveryId = null;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getStateString(){
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
