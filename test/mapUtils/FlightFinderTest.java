package mapUtils;

import helpers.Helper;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FlightFinderTest {
    @Test
    public void getFlights() throws Exception {
        FlightFinder finder = new FlightFinder();
//        FlightFinder.QpxResponse response = finder.getResponse();
//        System.out.println(Helper.GSON_PP.toJson(response));
        finder.setDate("2016-12-25");
        finder.setMaxPrice("USD31337");
        finder.setOrigin("BTR");
        finder.setDestination("SFO");
        finder.setSolutions(8);
        List<Flight> flights = finder.getFlights();
        System.out.println(Helper.GSON_PP.toJson(flights));
        assertEquals(8, flights.size());
    }

//    @Test
//    public void buildPostParams() throws Exception {
//        FlightFinder f = new FlightFinder();
//        String pp  = f.buildPostParams();
//        assertNotNull(pp);
//    }
}
