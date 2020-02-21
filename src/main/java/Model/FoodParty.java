package Model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FoodParty {
    ArrayList<PartyFood> partyFoods;
    LocalDateTime enteredDate;

    public FoodParty() {
        this.partyFoods = new ArrayList<>();
        this.enteredDate = null;
    }

    public String setFoodParty(ArrayList<PartyFood> newPartyFoods){
        partyFoods.clear();
        partyFoods = newPartyFoods;
        enteredDate = LocalDateTime.now();
        return "addPartyFood: 200";
    }

    public ArrayList<PartyFood> getPartyFoods() {
        return partyFoods;
    }

    public boolean isPartyFinished(){
        LocalDateTime now = LocalDateTime.now();
        return (enteredDate.until( now, ChronoUnit.SECONDS) > 1800);
    }

    public PartyFood getOrderedFood(String restaurantId, String foodName){
        for (PartyFood partyFood : partyFoods) {
            if ((partyFood.getRestaurantId().equals(restaurantId)) && (partyFood.getName().equals(foodName))) {
                if(partyFood.getCount() > 0){
                    partyFood.decreaseCount();
                    return partyFood;
                }
                else {
                    return null;
                }
            }
        }
        return null;
    }

}
