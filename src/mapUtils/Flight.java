package mapUtils;

import models.places.Location;

/**
 * A flight object with a price, duration, origin, destination.
 */
public final class Flight implements Comparable<Flight> {
    
    /** price in USD */
    private double price; // USD
    
    /** duration in hours */
    private double duration;
    
    /** actual origin as Location object */
    private Location origin;
    
    /** actual destination as Location object */
    private Location destination;
    
    /** intended origin as Location object */
    private Location intendedOrigin;
    
    /** intended destination as Location object */
    private Location intendedDestination;
    
    
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
    
    /** price in USD */
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

    /** actual origin as Location object */
    public Location getOrigin() {
        return origin;
    }

    /** actual origin as Location object */
    public Flight setOrigin(Location origin) {
        this.origin = origin;
        return this;
    }

    /** actual destination as Location object */
    public Location getDestination() {
        return destination;
    }

    /** actual destination as Location object */
    public Flight setDestination(Location destination) {
        this.destination = destination;
        return this;
    }
    
    /** intended origin as Location object */
    public Location getIntendedOrigin() {
        return intendedOrigin;
    }
    
    /** intended origin as Location object */
    public Flight setIntendedOrigin(Location intendedOrigin) {
        this.intendedOrigin = intendedOrigin;
        return this;
    }
    
    /** intended destination as Location object */
    public Location getIntendedDestination() {
        return intendedDestination;
    }
    
    /** intended destination as Location object */
    public Flight setIntendedDestination(Location intendedDestination) {
        this.intendedDestination = intendedDestination;
        return this;
    }
    
    @Override
    public int compareTo(Flight o) {
        return (int) (price - o.price);
    }
}
