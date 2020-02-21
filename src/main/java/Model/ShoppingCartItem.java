package Model;

public class ShoppingCartItem {
    private Food food;
    private int number;

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

    public ShoppingCartItem(Food food, int number) {
        this.food = food;
        this.number = number;
    }

    public void IncreaseNumber(){
        number++;
    }
}
