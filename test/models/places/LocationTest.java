package models.places;

import helpers.Helper;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by royg59 on 12/7/16.
 */
public class LocationTest {
    @Test
    public void getNearestAirportCode() throws Exception {
        Location loc = new Location("Laplace, LA");
        String result = loc.getNearestAirportCode();
        assertEquals("MSY", result);
    }
    
    @Test
    public void getNearbyAirports() throws Exception {
        Location loc = new Location("Donaldsonville, LA");
        List<Location> smallList = loc.getNearbyAirports(60);
        List<Location> bigList = loc.getNearbyAirports(300);
        assertTrue(bigList.size() >= smallList.size());
        assertTrue(bigList.get(0).getAirportCode().equals(
              smallList.get(0).getAirportCode()));
    }
    
}
