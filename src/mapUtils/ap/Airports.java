package mapUtils.ap;

import com.google.maps.model.LatLng;
import helpers.Helper;
import mapUtils.StraightLineDistance;
import models.places.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Hacks for airports
 */
public final class Airports {
    
    /* --------------------------------------------------------- */
    
    public static final double DEFAULT_RADIUS = 500.0;
    public static final int MAX_NUMBER_OF_NEARBY_AIRPORTS = 3;
    public static final int MINIMUM_AIRPORT_SIZE = 350000;
    
    private static final Map<String, LatLng> AIRPORTS_DICT = new HashMap<>();
    
    private static final String DAT_FILE_NAME = "major-airports-only3.dat";
    private static final StraightLineDistance SLD = Helper.SLD;
    
    public static boolean EMPTY = true;
    
    private Airports() { fill(); }
    
    /* --------------------------------------------------------- */
    
    private double radius;
    private double centerLat;
    private double centerLng;
    
    /* --------------------------------------------------------- */
    
    public Airports(double radius) {
        this.radius = radius;
        fill();
    }
    
    
    
    /* --------------------------------------------------------- */
    
    public static void fill()
    {
        if (EMPTY == false)
            return;
        File datFile = new File(DAT_FILE_NAME);
        Scanner fs = null;
        try
        {
            fs = new Scanner(datFile);
            Scanner s = new Scanner(fs.nextLine()); // consume header
            while (fs.hasNextLine())
            {
                s = new Scanner(fs.nextLine());
                s.useDelimiter(";;;");
    
                int i = 1;
                while (s.hasNext())
                {
                    String ord = s.next();          // (uesless)
                    String city = s.next();         // New Orleans
                    String faa = s.next();          // MSY
                    String iata = s.next();         // MSY
                    String icao = s.next();         // KMSY
                    String airport = s.next();      // Louis Armstrong New Orleans International Airport
                    String role = s.next();         // P-M
                    String enplanements = s.next(); // 4577498
                    String state = s.next();        // LOUISIANA
                    double lat = s.nextDouble();    // 29.993389
                    double lng = s.nextDouble();    // -90.258028
                    
                    if (Integer.parseInt(enplanements) >= MINIMUM_AIRPORT_SIZE)
                        put(iata, lat, lng);
                }
            }
            EMPTY = false;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void put(String airportCode, double lat, double lng)
    {
        AIRPORTS_DICT.put(airportCode, new LatLng(lat, lng));
    }
    
    /* --------------------------------------------------------- */
    
    public static LatLng getLatlng(String airportCode)
    {
        if (EMPTY)
            fill();
        return AIRPORTS_DICT.get(airportCode.toUpperCase().trim());
    }
    
    public static double getLat(String airportCode)
    {
        LatLng latlng = getLatlng(airportCode);
        return latlng.lat;
    }
    
    public static double getLng(String airportCode)
    {
        LatLng latlng = getLatlng(airportCode);
        return latlng.lng;
    }
    
    /**
     * Returns sld between your LatLng to an airport.
     * @param airportCode The airport code you're trying to get to.
     * @param yourLatlng Your latlng.
     * @return Straight line distance in miles.
     */
    public static double getSld(String airportCode, LatLng yourLatlng)
    {
        return getSld(airportCode, yourLatlng.lat, yourLatlng.lng);
    }
    
    /**
     * Returns sld between your LatLng to an airport.
     * @param airportCode The airport code you're trying to get to.
     * @param yourLat Your lat.
     * @param yourLng Your lng.
     * @return Straight line distance in miles.
     */
    public static double getSld(String airportCode,
                                double yourLat,
                                double yourLng)
    {
        double apLat = getLat(airportCode);
        double apLng = getLat(airportCode);
        double slDistance = SLD.slDistance(yourLat, yourLng,
                                           apLat, apLng);
        return slDistance;
    }
    
    /* --------------------------------------------------------- */
      
    /**
     * Given a lat, lng, and radius; fill two corresponding arraylists
     * for airports and their distances (you can get them by the same indexes).
     * @param lat 30.5043.
     * @param lng -90.4611.
     * @param radius 150 miles or something.
     * @param airportCodesArrayList
     *           This should be an empty arrayList; you
     *           get to keep the pointer to this pseudo-return value.
     * @param distancesArrayList
     *           This should be an empty arrayList; you
     *           get to keep the pointer to this pseudo-return value.
     */
    public static void getAirportsWithinRadius(
                             double lat,
                             double lng,
                             double radius,
                             ArrayList<String> airportCodesArrayList,
                             ArrayList<Double> distancesArrayList)
    {
        TreeMap<Double, String> r
              = getAirportsWithinRadius(lat, lng, radius);
        
        for (Double distance : r.keySet())
        {
            String code = r.get(distance);
            airportCodesArrayList.add(code);
            distancesArrayList.add(distance);
        }
    }
    
    public static ArrayList<Location> getNearbyAirports(Location location) {
        return getNearbyAirports(location, DEFAULT_RADIUS);
    }
    
    public static ArrayList<Location> getNearbyAirports(Location location,
                                                   double radius)
    {
        location.geocode();
        ArrayList<Location> nearbyAirports = new ArrayList<>();
        TreeMap<Double, String> r
              = getAirportsWithinRadius(location.getLat(),
                                        location.getLng(),
                                        radius);
        for (Double d : r.keySet()) {
            String code = r.get(d);
            LatLng latlng = AIRPORTS_DICT.get(code);
            Location airport = (new Location())
                  .setLatLng(latlng)
//                  .reverseGeocode()
                  .setIsAirport(true)
                  .setAirportCode(code)
                  .setLocationString(code);
            nearbyAirports.add(airport);
            if (nearbyAirports.size() >= MAX_NUMBER_OF_NEARBY_AIRPORTS)
                return nearbyAirports;
        }
        return nearbyAirports;
    }
    
    /* --------------------------------------------------------- */

    /**
     * Dict of distance : airportCode, sorted by distance; for hammond, 150...
     *   {
     *     "37.31": "MSY",
     *     "41.04": "BTR",
     *     "83.13": "GPT",
     *     "93.35": "LFT",
     *     "94.15": "PIB",
     *     "126.94": "JAN",
     *     "132.59": "MOB",
     *     "136.2": "AEX"
     *   }
     * @param lat 30.5043.
     * @param lng -90.4611.
     * @param radius 150 miles or something.
     * @return Map of distance -> code (37.31 -> "MSY").
     */
    public static TreeMap<Double, String> getAirportsWithinRadius(
                                                double lat,
                                                double lng,
                                                double radius)
    {
        if (EMPTY)
            fill();
        TreeMap<Double, String> results = new TreeMap<>();
        for (String code : AIRPORTS_DICT.keySet())
        {
            LatLng coords = AIRPORTS_DICT.get(code);
            Double distance = SLD.slDistance(
                  lat, lng,
                  coords.lat, coords.lng);
            if (distance <= radius)
                results.put(distance, code);
        }
        return results;
    }
    
    /**
     * Dict of distance : airportCode, sorted by distance; for hammond, 150...
     *   {
     *     "37.31": "MSY",
     *     "41.04": "BTR",
     *     "83.13": "GPT",
     *     "93.35": "LFT",
     *     "94.15": "PIB",
     *     "126.94": "JAN",
     *     "132.59": "MOB",
     *     "136.2": "AEX"
     *   }
     * @param latlng eg new LatLng(30.5043, -90.4611).
     * @param radius 150 miles or something.
     * @return Map of distance -> code (37.31 -> "MSY").
     */
    public static TreeMap<Double, String> getAirportsWithinRadius(
                                                LatLng latlng,
                                                double radius)
    {
        return getAirportsWithinRadius(latlng.lat, latlng.lng, radius);
    }
    
    public static ArrayList<String> getAirportsListWithinRadius(
                                         Location location, double radius)
    {
        location.geocode();
        return getAirportsListWithinRadius(
              location.getLat(), location.getLng(), radius);
    }
    
    public static ArrayList<String> getAirportsListWithinRadius(
                                        LatLng latlng, double radius)
    {
        return getAirportsListWithinRadius(latlng.lat, latlng.lng, radius);
    }
    
    public static ArrayList<String> getAirportsListWithinRadius(
                                        double lat, double lng, double radius)
    {
        ArrayList<String> airportsList = new ArrayList<String>();
        TreeMap<Double, String> r = getAirportsWithinRadius(lat, lng, radius);
        for (Double d : r.keySet())
            airportsList.add(r.get(d));
        return airportsList;
    }
    
    /**
     * Assuming your latlng is an airport, return its airport code
     * @param latlng
     * @return
     */
    public static String getAirportCodeOfLatlng(LatLng latlng)
    {
        Map.Entry<Double, String> entry
              = getAirportsWithinRadius(latlng, 50).firstEntry();
        double distance = entry.getKey();
        String code = entry.getValue();
        Helper.printDebug(
              "Code = " + code + ", Distance = " + distance);
        return code;
    }
}
