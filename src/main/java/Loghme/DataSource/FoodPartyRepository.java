package Loghme.DataSource;

public class FoodPartyRepository {
    private static FoodPartyRepository instance;

    public static FoodPartyRepository getInstance() {
        if (instance == null)
            instance = new FoodPartyRepository();
        return instance;
    }


}