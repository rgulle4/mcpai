package mapUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by royg59 on 11/25/16.
 */
public class RoadDistanceTest {

    private static final RoadDistance RD = new RoadDistance();

    @Test
    public void testGetRoadDistance() throws Exception {
        // should be 70.38102576211367 miles
        assertEquals(70.38103, RD.getRoadDistance("70806", "MSY"), 1);

        // should be 272.27062025426574 miles
        assertEquals(272.2706, RD.getRoadDistance("70806", "Houston, TX"), 1);
    }

    @Test
    public void testGetRoadTime() throws Exception {
        // "70806" to "Houston, TX" should be around 4 hours
        assertEquals(4.0, RD.getRoadTime("70806", "Houston, TX"), 0.5);
    }
}
