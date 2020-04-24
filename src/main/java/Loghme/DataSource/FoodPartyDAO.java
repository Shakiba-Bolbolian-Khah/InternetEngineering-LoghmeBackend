package Loghme.DataSource;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FoodPartyDAO {
    LocalDateTime enteredDate;
    ArrayList<PartyFoodDAO> partyFoods = new ArrayList<>();

    public LocalDateTime getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(LocalDateTime enteredDate) {
        this.enteredDate = enteredDate;
    }

    public ArrayList<PartyFoodDAO> getPartyFoods() {
        return partyFoods;
    }

    public void addPartyFoodDAO(PartyFoodDAO partyFoodDAO) {
        this.partyFoods.add(partyFoodDAO);
    }

    public void setPartyFoods(ArrayList<PartyFoodDAO> partyFoods) {
        this.partyFoods = partyFoods;
    }
}
