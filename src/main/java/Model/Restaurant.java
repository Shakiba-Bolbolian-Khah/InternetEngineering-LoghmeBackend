package Model;

import java.util.*;

public class Restaurant{
    private String name;
    private String id;
    private String description;
    private Location location;
    private ArrayList<Food> menu;
    private String logo;

    public Restaurant(String name, String id, String description, Location location, ArrayList<Food> menu, String logo) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.location = location;
        this.menu = menu;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getScore(){
        Double score = 0.0;
        for (Food food : menu) {
            score += food.getPopularity();
        }
        if (score != 0.0) {
            score = score / menu.size();
        }
        return score;
    }
}
