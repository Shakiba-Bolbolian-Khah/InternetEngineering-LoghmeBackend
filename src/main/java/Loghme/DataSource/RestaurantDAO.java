package Loghme.DataSource;

import Loghme.Domain.Logic.Location;

import java.util.ArrayList;

public class RestaurantDAO {
    private String id;
    private String name;
    private Location location;
//    private int x;
//    private int y;
    private String logo;
    private ArrayList<FoodDAO> menu = new ArrayList<>();

    public ArrayList<FoodDAO> getMenu() {
        return menu;
    }

    public void addToMenu(FoodDAO newFood) {
        this.menu.add(newFood);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
