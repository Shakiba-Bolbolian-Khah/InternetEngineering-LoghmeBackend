package Loghme.PresentationController;

import Loghme.Domain.Logic.Location;

public class HomeRestaurantDTO {
    private String name;
    private String id;
    private String logo;

    public HomeRestaurantDTO(String name, String id, String logo) {
        this.name = name;
        this.id = id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
