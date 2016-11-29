package mapUtils;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.*;
import helpers.Helper;
import models.places.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: this currently uses Google Places API, but it sucks. What to do!!??!?
 */
public class AirportFinder {
    private static final String GOOGLE_API_KEY = Helper.GOOGLE_API_KEY;
    private static final Gson GSON = new Gson();

    private static final GeoApiContext
          geoApiContext = Helper.GEO_API_CONTEXT;

    /**
     * Returns a verrrrry small list of airports, within like 31 miles of location.
     * @param location
     * @return
     */
    public static List<Location> getNearbyAirports(Location location) {
        PlacesSearchResponse response;
        List<PlacesSearchResult> results;
        PlaceIdScope scope;
        NearbySearchRequest searchRequest = PlacesApi.nearbySearchQuery(
                    geoApiContext,
                    location.getLatLng());
        searchRequest.rankby(RankBy.DISTANCE);
        searchRequest.type(PlaceType.AIRPORT);
        searchRequest.name("airport");
        response = searchRequest.awaitIgnoreError();
        results = Arrays.asList(response.results);
        List<Location> airports = new ArrayList<Location>();
        for (PlacesSearchResult r : results) {
//            if (r.name.toLowerCase().contains("airport")) {
                Location a = new Location();
                a.setIsAirport(true);
                a.setLocationString(r.name);
                a.setFormattedAddress(r.name);
                a.setLat(r.geometry.location.lat);
                a.setLng(r.geometry.location.lng);
                airports.add(a);
//            }
        }
        return airports;
    }
}
