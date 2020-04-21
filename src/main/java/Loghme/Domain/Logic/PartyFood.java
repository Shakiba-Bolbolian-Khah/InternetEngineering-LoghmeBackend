package Loghme.Domain.Logic;

public class PartyFood extends Food {
    String restaurantId;
    String restaurantName;
    int count;
    int oldPrice;

    public PartyFood(String name, String description, float popularity, Integer price, String imageUrl, String restaurantId, String restaurantName, int count, int oldPrice) {
        super(name, description, popularity, price, imageUrl);
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.count = count;
        this.oldPrice = oldPrice;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void decreaseCount(){
        this.count--;
    }

    public void increaseCount() {
        this.count++;
    }
}
