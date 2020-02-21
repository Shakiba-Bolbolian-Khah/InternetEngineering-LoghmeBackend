package Model;

public class Delivery {
    String id;
    int velocity;
    Location location;

    public Delivery(String id, int velocity, Location location) {
        this.id = id;
        this.velocity = velocity;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
