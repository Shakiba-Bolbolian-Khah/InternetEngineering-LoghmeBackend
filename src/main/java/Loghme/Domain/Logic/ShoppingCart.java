package Loghme.Domain.Logic;

import Loghme.DataSource.UserRepository;
import Loghme.Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
            }
        }
        else{
            if(!isPartyFood)
                UserRepository.getInstance().updateInCart(foodName,0);
            else
                if(isPartyFinished())
                    UserRepository.getInstance().updatePartyFoodInCart(foodName, restaurantId, 0);
                else{
                    UserRepository.getInstance().clearCart(0);
                    throw new Error403("Error: Food party time is over!");
                }
        }
        return "\""+ foodName +"\" has been added to your cart successfully!";
    }

    public String deleteFromCart(String restaurantId, String foodName, boolean isPartyFood) throws Error404, SQLException, Error403 {
        int foodIndex = contain(foodName , isPartyFood);
        if(foodIndex == -1){
            throw new Error404("Error: There is no food with name "+foodName+" in your cart to delete.");
        }
        else {
            if (!isPartyFood) {
                if (UserRepository.getInstance().deleteFromCart(foodName, 0, items.get(foodIndex).getNumber() == 1, items.get(foodIndex).getFood().getPrice())) {
                    UserRepository.getInstance().clearCart(0);
                }
            }
            else {
                if (isPartyFinished()) {
                    if (UserRepository.getInstance().deletePartyFoodFromCart(restaurantId, foodName, 0, items.get(foodIndex).getNumber() == 1, items.get(foodIndex).getFood().getPrice())) {
                        UserRepository.getInstance().clearCart(0);
                    }
                }
                else {
                    UserRepository.getInstance().clearCart(0);
                    throw new Error403("Error: Food party time is over!");
                }
            }
            return "\"" + foodName + "\" has been deleted from your cart successfully!";
        }

    }

    public boolean isPartyFinished(){
        LocalDateTime now = LocalDateTime.now();
        if(firstPartyFoodEnteredTime != null && isFoodParty != 0)
            return (firstPartyFoodEnteredTime.until(now, ChronoUnit.MINUTES) < 30);
        return false;
    }

    public int finalizeOrder(int userCredit, int orderId) throws Error403, Error400, SQLException {
        if(isEmpty){
            throw new Error400("Error: There is nothing in your cart to be finalized!");
        }
        if(isFoodParty != 0 && !isPartyFinished()){
            UserRepository.getInstance().clearCart(0);
            throw new Error403("Error: You ordered from food party! Food party time is over!");
        }
        if (userCredit >= totalPayment){
            UserRepository.getInstance().finalizeOrder(0, orderId);
            UserRepository.getInstance().clearCart(0);
            return orderId;
        }
        else
            throw new Error400("Error: Not enough credit!");
    }
}
