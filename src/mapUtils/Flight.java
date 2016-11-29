package mapUtils;

import models.places.Location;

public final class Flight {
    private double price; // USD
    private double duration; // hours
    private Location origin;
    private Location destination;

    public double getPrice() {
        return price;
    }

    public Flight setPrice(double price) {
        this.price = price;
        return this;
    }

    public Flight setPrice(String price) {
        this.price = Double.parseDouble(price.substring(3));
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public Flight setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public Location getOrigin() {
        return origin;
    }

    public Flight setOrigin(Location origin) {
        this.origin = origin;
        return this;
    }

    public Location getDestination() {
        return destination;
    }

    public Flight setDestination(Location destination) {
        this.destination = destination;
        return this;
    }
}
