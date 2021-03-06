package mapUtils;

import com.google.maps.model.LatLng;
import helpers.Helper;
import mapUtils.ap.Airports;
import models.places.Location;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by royg59 on 11/30/16.
 */
public class AirportsTest {
    @Test
    public void getLatlng() throws Exception {
        check("btr", 30.533, -91.149);
        check("iAh ",29.980055, -95.343170);
        check("msy", 29.994522, -90.270720);
        check("sfo", 37.624874, -122.396373);
    }
    
    private void check(String code, double eLat, double eLng) {
        final double delta = 0.1;
        LatLng r = Airports.getLatlng(code);
        assertEquals(r.lat, eLat, delta);
        assertEquals(r.lng, eLng, delta);
    }
    
    @Test
    public void getAirPortsWithinRadius() throws Exception {
        // Hammond
        double myLat = 30.504;
        double myLng = -90.461;
        double radius = 150; // miles
        TreeMap<Double, String> result
              = Airports.getAirportsWithinRadius(myLat, myLng, radius);
        assertEquals(4, result.size());
        assertTrue(result.containsValue("MSY"));
        assertTrue(result.containsValue("BTR"));
        assertTrue(result.containsValue("GPT"));
        assertFalse(result.containsValue("SFO"));
    
        assertTrue(result.firstEntry().getValue().equals("MSY"));
    }
    
    @Test
    public void getNearbyAirports() throws Exception {
        Location loc = new Location("Berkeley, CA");
        ArrayList<Location> nearbyAirports
              = Airports.getNearbyAirports(loc);
        Helper.printObject(nearbyAirports);
        System.out.println("size: " + nearbyAirports.size());
        assertTrue(nearbyAirports.size() > 1);
        assertTrue(nearbyAirports.size()
              <= Airports.MAX_NUMBER_OF_NEARBY_AIRPORTS);
    }
    
    @Test
    public void getAirPortsWithinRadius1() throws Exception {
        // Hammond
        double myLat = 30.504;
        double myLng = -90.461;
        double radius = 150; // miles
        
        ArrayList<String> airportCodes = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        Airports.getAirportsWithinRadius(
              myLat, myLng, radius,
              airportCodes, distances);

        assertTrue(airportCodes.get(0).equals("MSY"));
        assertEquals(37.31, distances.get(0), 1.5);

        assertTrue(airportCodes.get(1).equals("BTR"));
        assertEquals(41.04, distances.get(1), 1.5);
    }
}
