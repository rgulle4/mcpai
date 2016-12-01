package mapUtils;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import helpers.Helper;
import mapUtils.ap.Airports;
import models.places.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Geocodes a Location object.
 */
public final class Geocoder {
    private static String GOOGLE_API_KEY = Helper.getApiKey();
    private static final Gson GSON = Helper.GSON;

    private static final GeoApiContext
          geoApiContext = Helper.GEO_API_CONTEXT;
    
    /**
     * Geocodes a location, ie finds its latlng.
     * @param location A Location object.
     * @return The same Location object, after it's been fully geocoded.
     */
    public static Location geocode(Location location) {
        GeocodingResult[] results = geocode(location.getLocationString());
        GeocodingResult r = results[0];
        
        location.setLatLng(r.geometry.location.lat, r.geometry.location.lng);
        setStuff(location, r);
        return location;
    }
    
    private static void setStuff(Location location, GeocodingResult r) {
        location.setFormattedAddress(r.formattedAddress);
        location.setPlaceId(r.placeId);
        location.setIsAirport(resultIsAirport(r));
        if (resultIsAirport(r))
            location.setAirportCodeFromLatlng();
    }
    
    /**
     * Sets a location's name, address, etc using its latlng.
     * @param location A location with a latlng.
     * @return The same location, with data filled in.
     */
    public static Location reverseGeocode(Location location) {
        if (location.hasNoLatLng()) {
            System.err.print("ERROR: can't reverse geocode, bc no latlng " +
                  "for location " + location + "");
            return null;
        }
        GeocodingResult[] results = reverseGeocode(location.getLatLng());
        GeocodingResult r = results[0];
        setStuff(location, r);
        return location;
    }
    
    private static GeocodingResult[] reverseGeocode(double lat, double lng) {
        return reverseGeocode(new LatLng(lat, lng));
    }
    
    private static GeocodingResult[] reverseGeocode(LatLng latlng) {
        GeocodingResult[] results = new GeocodingResult[0];
        try { results = GeocodingApi
                  .reverseGeocode(geoApiContext, latlng)
                  .awaitIgnoreError();
        } catch (Exception e) { /* idc */ }
        return results;
    }
    
    private static GeocodingResult[] geocode(String locationString) {
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi
                  .geocode(geoApiContext, locationString)
                  .awaitIgnoreError();
        } catch (Exception e) { /* idc */ }
        return results;
    }
    
    private static boolean resultIsAirport(GeocodingResult r) {
        for (AddressType type : r.types) {
            if (type == AddressType.AIRPORT)
                return true;
        }
        return false;
    }
}
