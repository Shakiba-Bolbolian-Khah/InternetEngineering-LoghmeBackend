package Loghme.Domain.Logic;

import Loghme.DataSource.UserRepository;
import Loghme.Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class ShoppingCart {
    private boolean isEmpty;
    private String restaurantId;
    private String restaurantName;
    private int totalPayment;
    private int isFoodParty;
    private LocalDateTime firstPartyFoodEnteredTime;
    private ArrayList<ShoppingCartItem> items;

    public ShoppingCart(boolean isEmpty, String restaurantId, String restaurantName, int totalPayment, int isFoodParty, ArrayList<ShoppingCartItem> items) {
        this.isEmpty = isEmpty;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.totalPayment = totalPayment;
        this.isFoodParty = isFoodParty;
        this.firstPartyFoodEnteredTime = null;
        this.items = items;
    }

    public ShoppingCart(boolean isEmpty, String restaurantId, String restaurantName, int totalPayment, int isFoodParty, LocalDateTime firstPartyFoodEnteredTime, ArrayList<ShoppingCartItem> items) {
        this.isEmpty = isEmpty;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.totalPayment = totalPayment;
        this.isFoodParty = isFoodParty;
        this.firstPartyFoodEnteredTime = firstPartyFoodEnteredTime;
        this.items = items;
    }

    public LocalDateTime getFirstPartyFoodEnteredTime() {
        return firstPartyFoodEnteredTime;
    }

    public void setFirstPartyFoodEnteredTime(LocalDateTime firstPartyFoodEnteredTime) {
        this.firstPartyFoodEnteredTime = firstPartyFoodEnteredTime;
    }

    public int isFoodParty() {
        return isFoodParty;
    }

    public void setIsFoodParty(int foodParty) {
        isFoodParty = foodParty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setRestaurantName(String restaurantId, String restaurantName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.isEmpty = false;
    }

    public ArrayList<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShoppingCartItem> items) {
        this.items = items;
    }

    public int contain(String foodName, boolean isPartyFood){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getFood().getName().equals(foodName) && items.get(i).isPartyFood() == isPartyFood){
                return i;
            }
        }
        return -1;
    }

    public String addToCart(String foodName, String restaurantId, boolean isPartyFood) throws SQLException, Error403 {
        int foodIndex = contain(foodName, isPartyFood);
        if(foodIndex == -1){
            if(!isPartyFood)
                UserRepository.getInstance().insertInCart(foodName, restaurantId, 0);
            else {
                UserRepository.getInstance().insertPartyFoodInCart(foodName, restaurantId, 0);
//            ToDo: for foodparty should be added later
            }
        }
        else{
            if(!isPartyFood)
                UserRepository.getInstance().updateInCart(foodName,0);
            else
                UserRepository.getInstance().updatePartyFoodInCart(foodName, restaurantId, 0);
//            ToDo: for foodparty should be added later
        }
        return "\""+ foodName +"\" has been added to your cart successfully!";
    }

    public String deleteFromCart(String foodName) throws Error404 {
//        ToDo: true should be edited due to argument
        int foodIndex = contain(foodName , true);
        if(foodIndex == -1){
            throw new Error404("Error: There is no food with name "+foodName+" in your cart to delete.");
        }
        else {
            items.get(foodIndex).decreaseNumber();
            totalPayment -= items.get(foodIndex).getFood().getPrice();
            if (items.get(foodIndex).getNumber() == 0){
                items.remove(foodIndex);
                if(items.size() == 0)
                    isEmpty = true;
                return "\""+foodName+"\" has been deleted from your cart successfully!";
            }
            else{
                return "\""+foodName+"\" number has been decreased in your cart successfully!";
            }
        }
    }

    public ArrayList<ShoppingCartItem> doGetCart() throws Error404 {
        if(isEmpty)
            throw new Error404("Error: There is nothing to show in your cart!");
        return items;
    }

    public ShoppingCart finalizeOrder(int userCredit, boolean isFoodPartyFinished) throws Error403, Error400 {
        if(isEmpty){
            throw new Error400("Error: There is nothing in your cart to be finalized!");
        }
        if(isFoodParty != 0 && isFoodPartyFinished){
            clearCart();
            throw new Error403("Error: You ordered from food party! Food party time is over!!");
        }
        if (userCredit >= totalPayment){
            ShoppingCart order = copyCart(this);
            clearCart();
            return order;
        }
        else
            throw new Error400("Error: Not enough credit!");
    }

    private static ShoppingCart copyCart(ShoppingCart cart) {
        Gson gson = new GsonBuilder().create();
        String orderInStringForm = gson.toJson(cart);
        return gson.fromJson(orderInStringForm, ShoppingCart.class);
    }

    public void clearCart(){
        items.clear();
        totalPayment = 0;
        isEmpty = true;
        isFoodParty = 0;
        firstPartyFoodEnteredTime = null;
    }
}
