package models.places;

import com.google.maps.model.LatLng;
import mapUtils.Geocoder;

/**
 * TODO: see 00notes/location-etc/
 */
public class Location {
    private String locationString;
    private String formattedAddress;
    private Double lat;
    private Double lng;
    private LatLng latlng;
    private boolean hasBeenGeoCoded = false;
    private boolean isAirport = false;
    
    public boolean hasNoLatLng() {
        return (lat == null || lng == null);
    }
    
    public boolean hasLatLng() {
        return (lat != null || lng != null);
    }
    
    public boolean hasBeenGeocoded() { return hasBeenGeoCoded; }
    public boolean hasNotBeenGeocoded() { return !hasBeenGeoCoded; }
    
    /**
     * TODO: make this real
     * @return eg "MSY"
     */
    public String getNearestAirportCode() {
        return locationString;
    }
    
    public Location geocode() {
        geocode(false);
        return this;
    }
    
    public Location geocode(boolean force) {
        if (!force && hasBeenGeoCoded)
            return this;
        Geocoder.geocode(this);
        this.latlng = new LatLng(lat, lng);
        hasBeenGeoCoded = true;
        return this;
    }

    public Location() {
    }

    public Location(String locationString) {
        this();
        setLocationString(locationString);
    }

    public Location(String locationString, boolean isAirport) {
        this();
        setLocationString(locationString);
        setIsAirport(isAirport);
    }

    public boolean getIsAirport() { return isAirport; }
    public Location setIsAirport(boolean val) {
        isAirport = val;
        return this;
    }

    public String getLocationString() { return locationString; }
    public Location setLocationString(String locationString) {
        if (locationString.equals(this.locationString))
            return this;
        this.locationString = locationString;
        return this;
    }

    public String getFormattedAddress() { return formattedAddress; }
    public Location setFormattedAddress(String val) {
        formattedAddress = val;
        return this;
    }

    public Double getLat() {
        if (lat == null)
            geocode(true);
        return lat;
    }
    public Location setLat(Double val) {
        this.lat = val;
        resetLatLng();
        return this;
    }
    
    private void resetLatLng() {
        if (lat != null && lng != null)
            this.latlng = new LatLng(this.lat, this.lng);
    }

    public Double getLng() {
        if (lng == null)
            geocode(true);
        return lng;
    }
    public Location setLng(Double val) {
        this.lng = val;
        resetLatLng();
        return this;
    }
    
    public Location setLatLng(LatLng latlng) {
        this.latlng = latlng;
        this.lat = latlng.lat;
        this.lng = latlng.lng;
        this.geocode(true);
        return this;
    }
    
    public Location setLatLng(Double lat, Double lng) {
        this.latlng = new LatLng(lat, lng);
        this.lat = latlng.lat;
        this.lng = latlng.lng;
        this.geocode(true);
        return this;
    }
    
    public Location setLatLng(Double[] latlngPair) {
        if (latlngPair.length != 2)
            return this;
        this.lat = latlngPair[0];
        this.lng = latlngPair[1];
        this.latlng = new LatLng(lat, lng);
        this.geocode(true);
        return this;
    }

    public Double[] getLatLngPair() {
        if (hasNoLatLng())
            geocode();
        return new Double[] { lat, lng };
    }
    
    /**
     * Gives this object's {@link LatLng}  object.
     * @return A {@link LatLng} object.
     */
    public LatLng getLatLng() {
        if (hasNoLatLng())
            geocode();
        return latlng;
    }

    @Override
    public String toString() { return locationString; }
}
