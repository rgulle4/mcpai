package mapUtils;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
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
    private static final String GOOGLE_API_KEY = Helper.GOOGLE_API_KEY;
    private static final Gson GSON = new Gson();

    private static final GeoApiContext
          geoApiContext = Helper.GEO_API_CONTEXT;

    public static Location geoCode(Location location) {
        String locationString = location.getLocationString();
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.geocode(
                  geoApiContext,
                  locationString).awaitIgnoreError();
        } catch (Exception e) { /* idc */ }
        GeocodingResult r = results[0];
        location.setFormattedAddress(r.formattedAddress);
        location.setLat(r.geometry.location.lat);
        location.setLng(r.geometry.location.lng);
        return location;
    }
}
