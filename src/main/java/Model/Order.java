package Model;

import java.util.ArrayList;

enum State{
    Searching, OnWay, Delivered;
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

}
