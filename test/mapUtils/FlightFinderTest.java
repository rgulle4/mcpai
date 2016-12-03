package mapUtils;

import helpers.Helper;
import models.places.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FlightFinderTest {
    
    private static final String ORIGIN_AIRPORT = "BTR";
    private static final String DESTINATION_AIRPORT = "SFO";
    private static final String DATE_STRING = "2016-12-25";
    private static final int NUM_SOLUTIONS = 15;
    
    FlightFinder finder = new FlightFinder();
    List<Flight> flights;
    
    boolean hasBeenSetup = false;
    
    @Before
    public void setup() {
        if (hasBeenSetup)
            return;
        Helper.DEBUG_MODE = true;
        finder.setDate(DATE_STRING);
        finder.setMaxPrice("USD31337");
        finder.setOrigin(ORIGIN_AIRPORT);
        finder.setDestination(DESTINATION_AIRPORT);
        finder.setSolutions(NUM_SOLUTIONS);
        flights = finder.getFlights();
        Helper.printObject(flights, "flights");
        hasBeenSetup = true;
    }
    
    @Test
    public void getFlights() throws Exception {
        // there should be results --------------------
        int numResults = flights.size();
        assertTrue(0 < numResults && numResults <= NUM_SOLUTIONS);
    }
    
    @Test
    public void getFlight1() throws Exception {
        // the results should be sorted by price ------
        assertTrue(pricesAreIncreasing());
    }
    
    private boolean pricesAreIncreasing() {
        double previousPrice = 0;
        for (Flight flight : flights) {
            double price = flight.getPrice();
            if (price < previousPrice)
                return false;
        }
        return true;
    }
    
    @Test
    public void getBestFlight() throws Exception {
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
    
    @After
    public void cleanup() {
        Helper.printObject(finder.getFlights(), "All flights:");
        Helper.printObject(finder.requestCounter, "Number of requests:");
    }
}
