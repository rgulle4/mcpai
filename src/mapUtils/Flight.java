package mapUtils;

import models.places.Location;

/**
 * A flight object with a price, duration, origin, destination.
 */
public final class Flight {
    
    /** price in USD */
    private double price; // USD
    
    /** duration in hours */
    private double duration;
    
    /** origin as Location object */
    private Location origin;
    
    /** destination as Location object */
    private Location destination;
    
    /**
     * price in USD
     * */
    public double getPrice() {
        return price;
    }
    
    /** price in USD */
    public Flight setPrice(double price) {
        this.price = price;
        return this;
    }

    public Flight setPrice(String price) {
        this.price = Double.parseDouble(price.substring(3));
        return this;
    }

    /** duration in hours */
    public double getDuration() {
        return duration;
    }

    /** duration in hours */
    public Flight setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    /** origin as Location object */
    public Location getOrigin() {
        return origin;
    }

    /** origin as Location object */
    public Flight setOrigin(Location origin) {
        this.origin = origin;
        return this;
    }

    /** destination as Location object */
    public Location getDestination() {
        return destination;
    }

    /** destination as Location object */
    public Flight setDestination(Location destination) {
        this.destination = destination;
        return this;
    }
}
