package Loghme.PresentationController;

import Loghme.DataSource.FoodDAO;
import Loghme.Domain.Logic.Location;

import java.util.ArrayList;

public class RestaurantDTO {
    private String name;
    private String id;
    private Location location;
    private ArrayList<FoodDAO> menu;
    private String logo;

    public RestaurantDTO(String name, String id, Location location, ArrayList<FoodDAO> menu, String logo) {
        this.name = name;
        this.id = id;
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

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<FoodDAO> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<FoodDAO> menu) {
        this.menu = menu;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
