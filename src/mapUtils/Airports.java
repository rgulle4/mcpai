package mapUtils;

import com.google.maps.model.LatLng;
import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Hacks for airports
 */
public final class Airports {
    public static final Map<String, LatLng> AIRPORTS_DICT = new HashMap<>();
    private static final StraightLineDistance SLD = Helper.SLD;
    
    public static boolean EMPTY = true;
    
    private Airports() { fill(); }

    public static void fill() {
        if (!EMPTY)
            return;
        File text = new File("major-airports-only.dat");
        Scanner fs = null;
        try {
            fs = new Scanner(text);
            Scanner s = new Scanner(fs.nextLine()); // consume header
            while (fs.hasNextLine()) {
                s = new Scanner(fs.nextLine());
                s.useDelimiter(";;;");
    
                int i = 1;
                while (s.hasNext()) {
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
                    put(iata, lat, lng);
                }
            }
            EMPTY = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void put(String airportCode, double lat, double lng) {
        AIRPORTS_DICT.put(airportCode, new LatLng(lat, lng));
    }
    
    public static LatLng getLatlng(String airportCode) {
        if (EMPTY)
            fill();
        return AIRPORTS_DICT.get(airportCode.toUpperCase().trim());
    }
    
    public static double getLat(String airportCode) {
        LatLng latlng = getLatlng(airportCode);
        return latlng.lat;
    }
    
    public static double getLng(String airportCode) {
        LatLng latlng = getLatlng(airportCode);
        return latlng.lng;
    }
    
    /**
     * Given a lat, lng, and distanceLimit; fill two corresponding arraylists
     * for airports and their distances (you can get them by the same indexes).
     * @param lat 30.5043.
     * @param lng -90.4611.
     * @param distanceLimit 150 miles or something.
     * @param airportCodesArrayList This should be an empty arrayList; you
     *                               get to keep the pointer :).
     * @param distancesArrayList This should be an empty arrayList; you
     *                               get to keep the pointer :).
     */
    public static void getAirPortsWithinRadius(
                                  double lat,
                                  double lng,
                                  double distanceLimit,
                                  ArrayList<String> airportCodesArrayList,
                                  ArrayList<Double> distancesArrayList)
    {
        TreeMap<Double, String> r
              = getAirPortsWithinRadius(lat, lng, distanceLimit);
        
        for (Double distance : r.keySet()) {
            String code = r.get(distance);
            airportCodesArrayList.add(code);
            distancesArrayList.add(distance);
        }
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
     * @param lat 30.5043.
     * @param lng -90.4611.
     * @param distanceLimit 150 miles or something.
     * @return
     */
    public static TreeMap<Double, String> getAirPortsWithinRadius(
          double lat,
          double lng,
          double distanceLimit)
    {
        if (EMPTY)
            fill();
        TreeMap<Double, String> results = new TreeMap<>();
        for (String code : AIRPORTS_DICT.keySet()) {
            LatLng coords = AIRPORTS_DICT.get(code);
            Double distance = SLD.slDistance(
                  lat, lng,
                  coords.lat, coords.lng);
            if (distance <= distanceLimit)
                results.put(distance, code);
        }
        return results;
    }
    
    public static void main(String[] args) {
        Airports.fill();
        double myLat = 30.5043583;
        double myLng = -90.461199;
        double modestoLat = 37.6390971;
        double modestoLng = -120.996;
        double totalSLD = SLD.slDistance(
              myLat, myLng,
              modestoLat, modestoLng
        );
    
        double distanceLimit;
        distanceLimit = totalSLD;
        distanceLimit = 150;

        TreeMap<Double, String> results
              = getAirPortsWithinRadius(myLat, myLng, distanceLimit);
        System.out.println();
        System.out.println();
        Helper.printObject(results, "results");
        System.out.println();
        System.out.println("size = " + results.size());
    }
}
