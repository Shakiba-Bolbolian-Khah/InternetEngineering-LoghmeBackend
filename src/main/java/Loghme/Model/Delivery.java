package Loghme.Model;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Delivery {
    private String id;
    private int velocity;
    private Location location;

    public Delivery(String id, int velocity, Location location) {
        this.id = id;
        this.velocity = velocity;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public double findTimeToDeliver(Location userLoc, Location restaurantLoc) {
        int deliveryRestaurantDistanceX = location.getX() - restaurantLoc.getX();
        int deliveryRestaurantDistanceY = location.getY() - restaurantLoc.getY();
        double deliveryRestaurantDistance = sqrt(pow(deliveryRestaurantDistanceX, 2) + pow(deliveryRestaurantDistanceY, 2));
        int deliveryUserDistanceX = location.getX() - userLoc.getX();
        int deliveryUserDistanceY = location.getY() - userLoc.getY();
        double deliveryUserDistance = sqrt(pow(deliveryUserDistanceX, 2) + pow(deliveryUserDistanceY, 2));
        double distance = deliveryRestaurantDistance + deliveryUserDistance;
        return distance/velocity;
    }
}