package Loghme.DataSource;

public class CartItemDAO {
    private String foodName;
    private int price;
    private int number;
    private boolean isPartyFood;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPartyFood() {
        return isPartyFood;
    }

    public void setPartyFood(boolean partyFood) {
        isPartyFood = partyFood;
    }
}
