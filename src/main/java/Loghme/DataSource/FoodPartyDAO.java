package Loghme.DataSource;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FoodPartyDAO {
    LocalDateTime enteredDate;
    ArrayList<PartyFoodDAO> partyFoods = new ArrayList<>();

    public LocalDateTime getEnteredTime() {
        return enteredDate;
    }

    public void setEnteredTime(LocalDateTime enteredTime) {
        this.enteredDate = enteredTime;
    }

    public ArrayList<PartyFoodDAO> getPartyFoodDAOS() {
        return partyFoods;
    }

    public void addPartyFoodDAO(PartyFoodDAO partyFoodDAO) {
        this.partyFoods.add(partyFoodDAO);
    }

    public void setPartyFoodDAOS(ArrayList<PartyFoodDAO> partyFoodDAOS) {
        this.partyFoods = partyFoodDAOS;
    }
}
