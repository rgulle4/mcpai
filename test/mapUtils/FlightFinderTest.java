package mapUtils;

import helpers.Helper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FlightFinderTest {
    
    FlightFinder finder = new FlightFinder();
    List<Flight> flights;
    
    boolean hasBeenSetup = false;
    
    @Before
    public void setup() {
        Helper.DEBUG_MODE = true;
        finder.setDate("2016-12-25");
        finder.setMaxPrice("USD31337");
        finder.setOrigin("BTR");
        finder.setDestination("SFO");
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
    }
}
