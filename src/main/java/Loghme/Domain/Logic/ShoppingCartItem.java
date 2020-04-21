package Loghme.Domain.Logic;

public class ShoppingCartItem {
    private Food food;
    private int number;
    private boolean isPartyFood;

    public ShoppingCartItem(Food food, int number, boolean isPartyFood) {
        this.food = food;
        this.number = number;
        this.isPartyFood = isPartyFood;
    }

    public boolean isPartyFood() {
        return isPartyFood;
    }

    public void setPartyFood(boolean partyFood) {
        isPartyFood = partyFood;
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
