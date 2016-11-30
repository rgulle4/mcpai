package mapUtils;

import models.places.Location;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by royg59 on 11/25/16.
 */
public class RoadDistanceTest {

    private static final RoadDistance RD = new RoadDistance();
    Location msy = new Location("MSY Airport");
    Location iah = new Location("IAH Airport");
    double expectedDistance = 347.80717561236287;
    double expectedDuration = 5.1177777777777775;

    @Test
    public void testGetRoadDistance() throws Exception {
        // should be 70.38102576211367 miles
        assertEquals(70.38103, RD.getRoadDistance("70806", "MSY"), 1);

        // should be 272.27062025426574 miles
        assertEquals(272.2706, RD.getRoadDistance("70806", "Houston, TX"), 1);
    
        // should be 347.80717561236287 miles, 5.1177777777777775 hours
        double distance = RD.getRoadDistance(msy, iah);
        System.out.println("distance = " + distance);
        
        double delta = 0.01 * expectedDistance;
        assertEquals(expectedDistance, distance, delta);
    
        System.out.println("RoadDistance.apiRequestsCounter = "
                    + RoadDistance.apiRequestsCounter);
    }

    @Test
    public void testGetRoadTime() throws Exception {
        // "70806" to "Houston, TX" should be around 4 hours
        assertEquals(4.0, RD.getRoadTime("70806", "Houston, TX"), 0.5);
        
        /// should be 347.80717561236287 miles, 5.1177777777777775 hours
        double duration = RD.getRoadTime(msy, iah);
        System.out.println("duration = " + duration);
        double delta = 0.01 * expectedDuration;
        assertEquals(expectedDuration, duration, delta);
    
        System.out.println("RoadDistance.apiRequestsCounter = "
              + RoadDistance.apiRequestsCounter);
    }
}
