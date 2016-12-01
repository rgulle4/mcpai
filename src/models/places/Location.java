package models.places;

import com.google.maps.model.LatLng;
import mapUtils.ap.Airports;
import mapUtils.Geocoder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * TODO: see 00notes/location-etc/
 */
public class Location {
    public String getAirportCode() {
        return airportCode.toUpperCase();
    }
    
    public Location setAirportCode(String airportCode) {
        this.airportCode = airportCode.toUpperCase();
        return this;
    }
    
    public Location setAirportCodeFromLatlng() {
        String code = Airports.getAirportCodeOfLatlng(this.getLatLng());
        this.setAirportCode(code);
        return this;
    }
    
    /* --------------------------------------------------------- */
    
    private String locationString;
    private String formattedAddress;
    private String placeId;
    
    private boolean isAirport = false;
    private String airportCode;
    
    private Double lat;
    private Double lng;
    private LatLng latlng;
    private boolean hasBeenGeocoded = false;
    private boolean hasBeenReverseGeocoded = false;
    
    /**
     * Nearest airports, ordered by distance, converse of distancesAirportCodes;
     * kv pairs are like so:
     *   {
     *       "MSY" -> 37.31
     *       "BTR" -> 41.04
     *       "GPT" -> 83.13
     *       "LFT" -> 93.35
     *   }
     */
    private Map<String, Double> airportCodesDistances = new LinkedHashMap<>();
    
    /**
     * Nearest airports, ordered by distance, converse of airportCodesDistances;
     * kv pairs are like so:
     *   {
     *       37.31 -> "MSY"
     *       41.04 -> "BTR"
     *       83.13 -> "GPT"
     *       93.35 -> "LFT"
     *   }
     */
    private Map<Double, String> distancesAirportCodes = new LinkedHashMap<>();
    
    /**
     * The result from Airports.getAirportsWithinRadius()
     *   {
     *       37.31 -> "MSY"
     *       41.04 -> "BTR"
     *       83.13 -> "GPT"
     *       93.35 -> "LFT"
     *   }
     */
    private TreeMap<Double, String> airportsWithinRadius = new TreeMap<>();
    
    /* --------------------------------------------------------- */
    
    public boolean hasNoLatLng() {
        return (lat == null || lng == null);
    }
    
    public boolean hasLatLng() {
        return (lat != null && lng != null);
    }
    
    public boolean hasBeenGeocoded() { return hasBeenGeocoded; }
    public boolean hasNotBeenGeocoded() { return !hasBeenGeocoded; }
    
    public boolean hasBeenReverseGeocoded() {
        return hasBeenReverseGeocoded;
    }
    public boolean hasNotBeenReverseGeocoded() {
        return !hasBeenReverseGeocoded;
    }

    /* --------------------------------------------------------- */

    public boolean isAirport() { return isAirport; }
    public Location setIsAirport(boolean airport) {
        isAirport = airport;
        return this;
    }
    
    /* --------------------------------------------------------- */
     
    /**
     * TODO: make this real
     * @return eg "MSY"
     */
    public String getNearestAirportCode() {
        return locationString;
    }
    
    public Map<String, Double> getApDist(double radius) {
        acquireAirportsWithinRadius(radius);
        return airportCodesDistances;
    }
    
    public Map<Double, String> getDistAp(double radius) {
        acquireAirportsWithinRadius(radius);
        return distancesAirportCodes;
    }
    
    private void acquireAirportsWithinRadius(double radius) {
        // grab results, alias it to 'r'
        TreeMap<Double, String> r = airportsWithinRadius;
        r = Airports.getAirportsWithinRadius(this.lat, this.lng, radius);
    
        // alias our two mutually converse dictionaries
        Map<String, Double> apDist = airportCodesDistances;
        Map<Double, String> distAp = distancesAirportCodes;
        
        // put the results into our dictionaries, ordered by distance
        for (Double distance : r.keySet()) {
            String airportCode = r.get(distance);
            
            apDist.put(airportCode, distance);
            distAp.put(distance, airportCode);
        }
    }
    
    /* --------------------------------------------------------- */
    
    public Location geocode() {
        geocode(false);
        hasBeenGeocoded = true;
        return this;
    }
    
    public Location geocode(boolean force) {
        if (!force && hasBeenGeocoded)
            return this;
        Geocoder.geocode(this);
        this.latlng = new LatLng(lat, lng);
        hasBeenGeocoded = true;
        return this;
    }
    
    public Location reverseGeocode() {
        reverseGeocode(false);
        return this;
    }
    
    public Location reverseGeocode(boolean force) {
        if (!force && hasBeenReverseGeocoded)
            return this;
        Geocoder.reverseGeocode(this);
        return this;
    }
    
    /* --------------------------------------------------------- */
    
    public Location() {
    }

    public Location(String locationString) {
        this();
        setLocationString(locationString);
    }
    
    /* --------------------------------------------------------- */


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
    
    public String getPlaceId() { return placeId; }
    public Location setPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }
    
    /* --------------------------------------------------------- */
    
    public Location setLatLng(Double lat, Double lng) {
        this.latlng = new LatLng(lat, lng);
        this.lat = lat;
        this.lng = lng;
        return this;
    }
    
    public Location setLatLng(LatLng latlng) {
        this.latlng = latlng;
        this.lat = latlng.lat;
        this.lng = latlng.lng;
        return this;
    }
    
    public Double getLat() {
        if (hasNoLatLng())
            geocode(true);
        return lat;
    }
    
    public Double getLng() {
        if (hasNoLatLng())
            geocode(true);
        return lng;
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
    
    /* --------------------------------------------------------- */
    
    @Override
    public String toString() { return locationString; }
}
