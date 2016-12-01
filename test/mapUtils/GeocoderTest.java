package mapUtils;

import helpers.Helper;
import models.places.Location;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by royg59 on 11/29/16.
 */
public class GeocoderTest {
    @Test
    public void geocode() throws Exception {
        Location msy = new Location("msy");
        
        // msy shouldn't have a latlng yet...
        Helper.printObject(msy);
        assertFalse(msy.hasLatLng());
        
        // msy should have a latlng after we explicitly geocode it...
        msy.geocode();
        Helper.printObject(msy);
        assertTrue(msy.hasLatLng());

        // msy should have these exact latlng values...
        double expectedLat = 29.9922012;
        double expectedLng = -90.25901119999999;
        assertEquals(expectedLat, msy.getLat(), 0.01);
        assertEquals(expectedLng, msy.getLng(), 0.01);
    }
    
}
