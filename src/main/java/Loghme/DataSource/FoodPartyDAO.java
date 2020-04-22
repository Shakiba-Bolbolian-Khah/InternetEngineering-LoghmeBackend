package Loghme.DataSource;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FoodPartyDAO {
    LocalDateTime enteredTime;
    ArrayList<PartyFoodDAO> partyFoodDAOS;

    public LocalDateTime getEnteredTime() {
        return enteredTime;
    }

    public void setEnteredTime(LocalDateTime enteredTime) {
        this.enteredTime = enteredTime;
    }

    public ArrayList<PartyFoodDAO> getPartyFoodDAOS() {
        return partyFoodDAOS;
    }

    public void addPartyFoodDAO(PartyFoodDAO partyFoodDAO) {
        this.partyFoodDAOS.add(partyFoodDAO);
    }
}
