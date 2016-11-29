package mapUtils;

import helpers.Helper;
import models.places.Location;
import org.junit.Test;

import java.util.List;

/**
 * Created by royg59 on 11/29/16.
 */
public class AirportFinderTest {

    @Test
    public void getNearbyAirports() throws Exception {
        Location location = new Location("Baton Rouge");
        List<Location> results = AirportFinder.getNearbyAirports(location);
        System.out.println(Helper.GSON_PP.toJson(results));
    }

}
