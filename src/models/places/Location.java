package models.places;

import com.google.maps.model.LatLng;
import helpers.Helper;
import mapUtils.Geocoder;

/**
 * TODO: see 00notes/location-etc/
 */
public class Location {
    private String locationString;
    private String formattedAddress;
    private Double lat;
    private Double lng;
    private boolean isAirport = false;

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
        Geocoder.geoCode(this);
        return this;
    }

    public String getFormattedAddress() { return formattedAddress; }
    public Location setFormattedAddress(String val) {
        formattedAddress = val;
        return this;
    }

    public Double getLat() { return lat; }
    public Location setLat(Double val) {
        this.lat = val;
        return this;
    }

    public Double getLng() { return lng; }
    public Location setLng(Double val) {
        this.lng = val;
        return this;
    }

    public Double[] getLatLngPair() {
        return new Double[] { lat, lng };
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    @Override
    public String toString() { return locationString; }
}
