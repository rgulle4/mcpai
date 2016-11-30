package mapUtils;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import helpers.Helper;
import models.places.Location;

/**
 * TODO: get coordinates, formatted address, and maybe other info from Google
 *    In the response, we probably just want - results[i].formatted_address
 *                                           - results[i].geometry.location
 *     Pls change the places.Location class as necessary also.
 *
 *     https://gist.github.com/rgulle4/7cdf66ce833d14431dbf926c7a91dbbf
 *     https://maps.googleapis.com/maps/api/geocode/json?&address=baton%20rouge%2C%20LA
 *
 *     https://gist.github.com/rgulle4/df6665dde76b8c779cf799d380e021ca
 *     https://maps.googleapis.com/maps/api/geocode/json?&address=baton%20rouge%2C%20LA
 */
public final class Geocoder {
    private static final String GOOGLE_API_KEY
          = "AIzaSyCVcA7zkndeuPwvejRsbtngKfC-H40Gq8Y";
//          = "AIzaSyCxC53nAkMaeEDWFCmCT8JXOA8Qcv8ej0g";
    //          = Helper.GOOGLE_API_KEY;
    private static final Gson GSON = Helper.GSON;

    private static final GeoApiContext
          geoApiContext = Helper.GEO_API_CONTEXT;
    
    /**
     * Geocodes a location; if latlng exists, reverseGeocode using latlng (latlng will
     * be unchanged, but all other fields will potentially change), next try
     * geocoding using locationString.
     * @param location A Location object.
     * @return The same Location object, after it's been fully geocoded.
     */
    public static Location geocode(Location location) {
        GeocodingResult[] results;
        if (location.hasLatLng())
            results = reverseGeocode(location.getLatLng());
        else
            results = geocode(location.getLocationString());
        GeocodingResult r = results[0];
        location.setFormattedAddress(r.formattedAddress);
        location.setLat(r.geometry.location.lat);
        location.setLng(r.geometry.location.lng);
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
        try { results = GeocodingApi
                  .geocode(geoApiContext, locationString)
                  .awaitIgnoreError();
        } catch (Exception e) { /* idc */ }
        return results;
    }
}
