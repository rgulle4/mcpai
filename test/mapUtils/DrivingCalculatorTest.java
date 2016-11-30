package mapUtils;

import models.places.Location;
import org.junit.Test;

import static org.junit.Assert.*;

public class DrivingCalculatorTest {
    @Test
    public void calc() throws Exception {
        DrivingCalculator dCalc = new DrivingCalculator();
        
        double p = dCalc.calc(272);
        assertEquals(16.71, p, 0.1);
        assertEquals(16.71, dCalc.getTripPrice(), 0.01);
        assertEquals(3.89, dCalc.getTripDuration(), 0.01);
        
        double q = dCalc.calc(272, 4.1);
        assertEquals(16.71, q, 0.1);
        assertEquals(16.71, dCalc.getTripPrice(), 0.01);
        assertEquals(4.10, dCalc.getTripDuration(), 0.01);
    
        double r = dCalc.calc(2241.0, 31.60);
        assertEquals(287.66, r, 0.1);
        assertEquals(287.66, dCalc.getTripPrice(), 0.01);
        assertEquals(47.60, dCalc.getTripDuration(), 0.01);
    
        double s = dCalc.calc(2241.0);
        assertEquals(287.66, s, 0.1);
        assertEquals(287.66, dCalc.getTripPrice(), 0.01);
        assertEquals(48.01, dCalc.getTripDuration(), 0.01);
    
        dCalc.setHotelPrice(2 * dCalc.getHotelPrice());
        double t = dCalc.calc(2241.0, 31.60);
        assertNotEquals(287.66, t, 0.1);
        assertNotEquals(287.66, dCalc.getTripPrice(), 0.01);
        assertEquals(47.60, dCalc.getTripDuration(), 0.01);
    
        Location bogalusa = new Location("Bogalusa, LA");
        Location sfo = new Location("SFO airport");
        dCalc = new DrivingCalculator(bogalusa, sfo);
        double expectedTripPrice = 290.25;
        double expectedTripDuration = 48.40;
        assertEq(expectedTripPrice, dCalc.getTripPrice());
        assertEq(expectedTripDuration, dCalc.getTripDuration(), 0.1);
    }
    
    private static final void assertEq(double expected, double actual) {
        double delta = expected * 0.05;
        assertEquals(expected, actual, delta);
    }
    
    private static final void assertEq(double expected, double actual,
                                       double pctDelta)
    {
            assertEquals(expected, actual, expected * pctDelta);
    }
    
}
