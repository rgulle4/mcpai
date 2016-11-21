package models.places;

/**
 * TODO: see 00notes/location-etc/
 */
public class Location {
    String locationString;

    public Location() {

    }

    public Location(String locationString) {
        this();
        setLocationString(locationString);
    }

    public boolean isAirport() { return false; }

    public String getLocationString() { return locationString; }
    public Location setLocationString(String locationString) {
        if (locationString.equals(this.locationString))
            return this;
        this.locationString = locationString;
        return this;
    }

    @Override
    public String toString() {
        return getLocationString();
    }
}
