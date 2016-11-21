package mapUtils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by royg59 on 11/25/16.
 */
public class StraightLineDistanceTest {

    @Test
    public void testSlDistance() throws Exception {
        assertEquals(
              1794.06,
              StraightLineDistance.slDistance(
                    36.12, -86.67,
                    33.94, -118.40),
              1);
    }
}
