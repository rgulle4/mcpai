package mapUtils;

import com.google.gson.Gson;
import helpers.Helper;
import models.places.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Gives simple distance, and driving time between two places (no driving
 * directions though); a place can be coordinates, a zip, an address, or any
 * string that would turn up a result in Google Maps. Uses Google Maps
 * Distance Matrix API.
 *
 * TODO: persist the responseCache, make it better
 */
public final class RoadDistance {

    private static final String GOOGLE_API_KEY = Helper.GOOGLE_API_KEY;
    private static final Gson GSON = new Gson();
    private static final String BASE_URL = "https://maps.googleapis.com/" +
          "maps/api/distancematrix/json?" +
          "&mode=car" +
          "&sensor=false" +
          "&units=imperial";
    private static final String BASE_DESTINATION_PARAM = "&destinations=" ;
    private static final String BASE_ORIGINS_PARAM = "&origins="; // multiple origins separated by "|"
    private static final String ZIPS_SEPARATOR = "|"; // multiple zips separated by "|"

    private static final Map<String, Response> responseCache = new HashMap<String, Response>();
    /**
     * Returns car distance in miles between two places as given by Google Maps.
     * @param origin eg 70806, msy, or houston, tx
     * @param destination eg 70806, msy, or houston, tx
     * @return car distance in miles.
     */
    public double getRoadDistance(String origin, String destination) {
        StringBuilder sb = new StringBuilder();
        String cacheKey = sb.append(origin.trim()).append(destination.trim()).toString();
        Response response = responseCache.get(cacheKey);

        if (response == null) {
            response = getResponse(origin, destination);
            if (!response.status.toLowerCase().equals("ok")) {
                printError("ERROR: getRoadDistance(" + origin + ", " + destination + ") response not 'ok'");
                return -1;
            }
            responseCache.put(cacheKey, response);
        }

        double distance = response.rows[0].elements[0].distance.getMiles();
        return distance;
    }
    
    public double getRoadDistance(Location origin, Location destination) {
        String originString = parseLocationString(origin);
        String destinationString = parseLocationString(destination);
        return getRoadDistance(originString, destinationString);
    }
    
    public double getRoadTime(Location origin, Location destination) {
        String originString = parseLocationString(origin);
        String destinationString = parseLocationString(destination);
        return getRoadTime(originString, destinationString);
    }
    
    private String parseLocationString(Location location) {
        if (location.hasLatLng()) {
            StringBuilder sb = new StringBuilder();
            sb.append(location.getLat()).append(",").append(location.getLng());
            return sb.toString();
        } else {
            return location.getLocationString();
        }
    }

    /**
     * Returns car time in hours between two places as given by Google Maps.
     * @param origin eg 70806, msy, or houston, tx
     * @param destination eg 70806, msy, or houston, tx
     * @return car time in hours eg 33.5.
     */
    public double getRoadTime(String origin, String destination) {
        StringBuilder sb = new StringBuilder();
        String cacheKey = sb.append(origin.trim()).append(destination.trim()).toString();
        Response response = responseCache.get(cacheKey);

        if (response == null) {
            response = getResponse(origin, destination);
            if (!response.status.toLowerCase().equals("ok")) {
                printError("ERROR: getRoadDistance(" + origin + ", " + destination + ") response not 'ok'");
                return -1;
            }
            responseCache.put(cacheKey, response);
        }
        double roadTime = response.rows[0].elements[0].duration.getDuration();
        return roadTime;
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

    public static int apiRequestsCounter = 0;

    private Response getResponse(String origin, String destination) {
        apiRequestsCounter++;
        String urlString = buildUrl(origin, destination);
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
            String responseString = sbResponse.toString();
            return GSON.fromJson(responseString, Response.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * https://developers.google.com/maps/documentation/distance-matrix/intro
     */
    private class Response
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

    private void printError(Object o) {
        System.err.println(o);
    }
}
