package mapUtils;

import helpers.Helper;
import models.places.Location;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FlightFinderTest {
    
    private static final String ORIGIN_AIRPORT = "BTR";
    private static final String DESTINATION_AIRPORT = "SFO";
    private static final String DATE_STRING = "2016-12-25";
    
    FlightFinder finder = new FlightFinder();
    List<Flight> flights;
    
    boolean hasBeenSetup = false;
    
    @Before
    public void setup() {
        Helper.DEBUG_MODE = true;
        finder.setDate(DATE_STRING);
        finder.setMaxPrice("USD31337");
        finder.setOrigin(ORIGIN_AIRPORT);
        finder.setDestination(DESTINATION_AIRPORT);
        finder.setSolutions(8);
        flights = finder.getFlights();
        Helper.printObject(flights, "flights");
        hasBeenSetup = true;
    }
    
    @Test
    public void getFlightsANDgetBestFlight() throws Exception {
        
        // testing getFlights() -----------------------
        
        assertEquals(8, flights.size());
    
        // testing getBestFlight() --------------------
        
        Flight bestFlight = finder.getBestFlight();
        Flight worstFlight = flights.get(flights.size() - 1);
        Helper.printObject(bestFlight, "bestFlight");
        Helper.printObject(worstFlight, "worstFlight");
        assertTrue(bestFlight.getPrice() <= worstFlight.getPrice());
    
        // testing getBestFlight() --------------------
        
        Flight anotherBestFlight = finder.getBestFlight(
              ORIGIN_AIRPORT, DESTINATION_AIRPORT,  DATE_STRING);
        assertEquals(bestFlight.getPrice(), anotherBestFlight.getPrice(), 1);
    }
}
