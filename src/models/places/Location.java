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
    
    public Location geoCode() {
        geoCode(false);
        return this;
    }
    
    public Location geoCode(boolean force) {
        if (!force && hasBeenGeoCoded)
            return this;
        Geocoder.geoCode(this);
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
        if (hasNoLatLng())
            geoCode();
        return lat;
    }
    public Location setLat(Double val) {
        this.lat = val;
        return this;
    }

    public Double getLng() {
        if (hasNoLatLng())
            geoCode();
        return lng;
    }
    public Location setLng(Double val) {
        this.lng = val;
        return this;
    }

    public Double[] getLatLngPair() {
        if (hasNoLatLng())
            geoCode();
        return new Double[] { lat, lng };
    }
    
    /**
     * Gives this object's {@link LatLng}  object.
     * @return A {@link LatLng} object.
     */
    public LatLng getLatLng() {
        if (hasNoLatLng())
            geoCode();
        return latlng;
    }

    @Override
    public String toString() { return locationString; }
}
