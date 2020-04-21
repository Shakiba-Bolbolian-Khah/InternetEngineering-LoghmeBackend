package Loghme.Domain.Logic;

import Loghme.Exceptions.ErrorHandler;

import java.time.LocalTime;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Restaurant{
    private String name;
    private String id;
    private Location location;
    private ArrayList<Food> menu;
    private String logo;

    public Restaurant(String name, String id, Location location, ArrayList<Food> menu, String logo) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.menu = menu;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<Food> getMenu() {
        return menu;
    }

    public void clearMenu() {
        this.menu.clear();
    }

    public String getLogoUrl() {
        return logo;
    }

    public Food getFood(String foodName) throws ErrorHandler {
        for (Food food : menu) {
            if (food.getName().equals(foodName)) {
                return food;
            }
        }
        throw new ErrorHandler("Error: No \"" + foodName +"\" in \"" + this.name + "\" restaurant exists!");
    }

    public String addFood(Food newFood) throws ErrorHandler{
        for (Food food : menu) {
            if (food.getName().equals(newFood.getName())) {
                throw new ErrorHandler("Error: \"" + newFood.getName() + "\" had been added in \"" + name + "\" menu before!");
            }
        }
        menu.ensureCapacity(menu.size()+1);
        menu.add(newFood);
        return "\""+newFood.getName()+"\" food has been added in \""+name+"\" menu successfully!";
    }

    public Food getOrderedFood(String foodName){
        for (Food food : menu) {
            if (food.getName().equals(foodName)) {
                return food;
            }
        }
        return null;
    }

    public Double calculateScore(){
        double score = 0.0;
        for (Food food : menu) {
            score += food.getPopularity();
        }
        if (score != 0.0) {
            score = score / menu.size();
        }
        return score;
    }

    public LocalTime estimateDeliveryTime(Location userLocation) {
        double estimatedTime = calculateEstimatedTimeToDeliver(userLocation);
        int hours = (int) estimatedTime / 3600;
        int minutes = (int) (estimatedTime % 3600) / 60;
        int seconds = (int) estimatedTime % 60;
        return LocalTime.of(hours, minutes, seconds);
    }

    public double calculateEstimatedTimeToDeliver(Location userLoc) {
        int estimatedVelocity = 5;
        int estimatedDeliveryFindingTime = 60;
        int restaurantToUserDistanceX = location.getX() - userLoc.getX();
        int restaurantToUserDistanceY = location.getY() - userLoc.getY();
        double restaurantToUserDistance = sqrt(pow(restaurantToUserDistanceX, 2) + pow(restaurantToUserDistanceY, 2));
        double distance = 1.5 * restaurantToUserDistance;
        return distance/estimatedVelocity + estimatedDeliveryFindingTime;
    }
}