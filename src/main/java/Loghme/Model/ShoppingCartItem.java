package Loghme.Model;

public class ShoppingCartItem {
    private Food food;
    private int number;
    private int price;
    private boolean isPartyFood;
    public ShoppingCartItem(Food food, int number, int price, boolean isPartyFood) {
        this.food = food;
        this.number = number;
        this.price = price;
        this.isPartyFood = isPartyFood;
    }

    public boolean isPartyFood() {
        return isPartyFood;
    }

    public void setPartyFood(boolean partyFood) {
        isPartyFood = partyFood;
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

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void increaseNumber(){
        number++;
    }

    public void decreaseNumber(){
        number--;
    }
}
