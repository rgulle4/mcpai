package mapUtils;

import com.google.gson.Gson;
import helpers.Helper;
import helpers.Minify;
import mapUtils.cache.RoadDistanceCache;
import models.places.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Gives simple distance, and driving time between two places (no driving
 * directions though); a place can be coordinates, a zip, an address, or any
 * string that would turn up a result in Google Maps. Uses Google Maps
 * Distance Matrix API. Main methods are:  * <br>
 * <br>
 *   - {@link RoadDistance#getRoadDistance(String, String)} <br>
 *   - {@link RoadDistance#getRoadTime(String, String)} <br>
 * <br>
 *   - {@link RoadDistance#getRoadDistance(Location, Location)} <br>
 *   - {@link RoadDistance#getRoadTime(Location, Location)} <br>
 * <br>
 * TODO: persist the RESPONSE_CACHE, make it better
 */
public final class RoadDistance {

    private static String GOOGLE_API_KEY
          = Helper.getApiKey();
    private static final Gson GSON
          = new Gson();
    private static final String BASE_URL
          = "https://maps.googleapis.com/"
          + "maps/api/distancematrix/json?"
          + "&mode=car"
          + "&sensor=false"
          + "&units=imperial";
    private static final String BASE_DESTINATION_PARAM
          = "&destinations=" ;
    private static final String BASE_ORIGINS_PARAM
          = "&origins=";
    private static final String SEPARATOR
          = "|";
    
    /* -- TODO: cache these objects ---------------------------- */
    
    private static final RoadDistanceCache RESPONSE_CACHE
          = RoadDistanceCache.getInstance();
    
    // inputs
    private String combinedInputString;
    private String originString;
    private String destinationString;
    
    // results
    private double roadDistance;
    private double roadTime;
    
    // api results
    private Response response;
    private String responseJson;
    public int apiRequestsCounter = 0;
    public int cacheHits = 0;
    
    // metadata
    
    /* -- constructors and cache setup ------------------------- */
    
    public RoadDistance() {
        
    }
    
    /** this is called in getResponseFromAPI() when it's finished */
    private void updatePersistentCache() {
        // TODO: persist the inputs, result, counter, and whatever else
    }
    
    private Response getResponseFromCache() {
//        String newCombinedInputString
//              = buildCombinedInput(originString, destinationString);
//        if (response != null) {
//            if (areEqual(newCombinedInputString, combinedInputString)) {
//                cacheHits++;
//                return response;
//            }
//        }
    
//        combinedInputString = newCombinedInputString;
        String cachedResponseJson = RESPONSE_CACHE.get(originString, destinationString);
        
        if (cachedResponseJson != null) {
            cacheHits++;
            response = GSON.fromJson(cachedResponseJson, Response.class);
            return response;
        }
        Response actualResponse = getResponseFromAPI();
        response = actualResponse;
        RESPONSE_CACHE.put(originString, destinationString, responseJson);
//        RESPONSE_CACHE.put(originString, destinationString, response);
        return actualResponse;
    }
    
    /* -- the good stuff  -------------------------------------- */
    
    /**
     * Returns car distance in miles between two places as given by Google Maps.
     * @param origin eg 70806, msy, or houston, tx
     * @param destination eg 70806, msy, or houston, tx
     * @return car distance in miles.
     */
    public double getRoadDistance(String origin, String destination) {
        response = getResponse(origin, destination);
        roadDistance = response.rows[0].elements[0].distance.getMiles();
        return roadDistance;
    }
    
    /**
     * See {@link RoadDistance#getRoadDistance(String, String)}
     * @param origin a Location object.
     * @param destination a Location object.
     * @return
     */
    public double getRoadDistance(Location origin, Location destination) {
        String originString = parseLocationString(origin);
        String destinationString = parseLocationString(destination);
        return getRoadDistance(originString, destinationString);
    }
    
    /**
     * Returns car time in hours between two places as given by Google Maps.
     * @param origin eg 70806, msy, or houston, tx
     * @param destination eg 70806, msy, or houston, tx
     * @return car time in hours eg 33.5.
     */
    public double getRoadTime(String origin, String destination) {
        response = getResponse(origin, destination);
        roadTime = response.rows[0].elements[0].duration.getDuration();
        return roadTime;
    }
    
    /**
     * See {@link RoadDistance#getRoadTime(Location, Location)}
     * @param origin a Location object.
     * @param destination a Location object.
     * @return
     */
    public double getRoadTime(Location origin, Location destination) {
        String originString = parseLocationString(origin);
        String destinationString = parseLocationString(destination);
        return getRoadTime(originString, destinationString);
    }
    
    /* -- deal with the api ------------------------------------ */

    private String parseLocationString(Location location) {
        if (location.hasLatLng()) {
            StringBuilder sb = new StringBuilder();
            sb.append(location.getLat()).append(",").append(location.getLng());
            return sb.toString();
        } else {
            return location.getLocationString();
        }
    }

    private String sanitize(String str) {
        return str.replaceAll("\\s+", "+");
    }

    private String buildUrl(String origin, String destination) {
        origin = sanitize(origin);
        destination = sanitize(destination);
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append(BASE_DESTINATION_PARAM).append(destination);
        sb.append(BASE_ORIGINS_PARAM).append(origin);
        return sb.toString();
    }
    
    private Response getResponse(String origin, String destination) {
        originString = origin;
        destinationString = destination;
        return getResponseFromCache();
    }
    
    private String buildCombinedInput(String origin, String destination) {
        return (new StringBuilder())
              .append(origin.trim())
              .append(destination.trim()).toString();
    }
    
//    private Response getResponseFromCache() {
//        Response cachedResponse = RESPONSE_CACHE.get(combinedInputString);
//        if (cachedResponse != null)
//            return cachedResponse;
//        Response actualResponse = getResponseFromAPI();
//        RESPONSE_CACHE.put(combinedInputString, actualResponse);
//        return actualResponse;
//    }
    
    private Response getResponseFromAPI() {
        String urlString = buildUrl(originString, destinationString);
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(
                  new InputStreamReader(conn.getInputStream()));
            StringBuilder sbResponse = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
                sbResponse.append(line);
            rd.close();
            conn.disconnect();
            
            this.apiRequestsCounter++;
            this.responseJson = (new Minify()).minify(sbResponse.toString());
            this.response = GSON.fromJson(responseJson, Response.class);
            this.updatePersistentCache();
            
            return this.response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static final String OK_STRING = "ok";
    private boolean responseIsOk(Response response) {
        boolean responseIsValid = areEqual(response.status, OK_STRING);
        if (!responseIsValid)
            printError("ERROR: RoadDistance API call failed... \n"
                  + Helper.toJsonPretty(response));
        return responseIsValid;
    }

    /**
     * https://developers.google.com/maps/documentation/distance-matrix/intro
     */
    public class Response
    {
        String status;  // "OK"
        String[] destination_addresses; // [ "Baton Rouge, LA 70803, USA" ]
        String[] origin_addresses; // [ "New Orleans, LA 70115, USA", "Lake Charles, LA 70601, USA", "Houston, TX 77001, USA" ]
        Row[] rows;

        class Row {
            Element[] elements;
        }
        class Element {
            Distance distance;
            Duration duration;
            String status; // "Ok"
        }
        class Distance {
            String text; // "83.2 mi"
            int value;   // 133917 (meters)
            public double getMiles() { return value / 1609.34 ; }
        }
        class Duration {
            String text; // "1 hour 27 mins"
            int value;   // 5200 (seconds)
            public double getDuration() { return value / 3600.0; }
        }
    }
    
    /* -- helpers ---------------------------------------------- */

    private void printError(Object o) {
        Helper.printError(o);
    }
    
    private static boolean areEqual(String s1, String s2) {
        return Helper.areEqual(s1, s2);
    }
}
