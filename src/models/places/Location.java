package models.places;

import helpers.Helper;
import mapUtils.Geocoder;

/**
 * TODO: see 00notes/location-etc/
 */
public class Location {
    private String locationString;
    private String formattedAddress;
    private double lat;
    private double lng;
    private double[] latLng = new double[] { lat, lng };

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
        Geocoder.geoCode(this);
        return this;
    }

    public String getFormattedAddress() { return formattedAddress; }
    public Location setFormattedAddress(String val) {
        formattedAddress = val;
        return this;
    }

    public double getLat() { return lat; }
    public Location setLat(double val) {
        this.lat = val;
        return this;
    }

    public double getLng() { return lng; }
    public Location setLng(double val) {
        this.lng = val;
        return this;
    }

    public double[] getLatLng() {
        return latLng;
    }

    @Override
    public String toString() { return locationString; }
}
