package mapUtils;

import models.places.Location;

/**
 * Provides methods to get straight line distance between two points on Earth,
 * using the Haversine formula.
 */
public final class StraightLineDistance
{
    // radius of Earth in kilometers (NB 1.60934 meters in a mile)
    private static final double R = 6372.8;
    private static final double KM_PER_MILE = 1.60934;

    // TODO: look into other signatures that might be useful (eg (mapnode, mapnode))
    /**
     * Returns straight line distance between two points on Earth.
     * @param lat1 Latitude of point1.
     * @param lon1 Longitude of point1.
     * @param lat2 Latitude of point2.
     * @param lon2 Longitude of point2.
     * @return distance in miles.
     */
    public static double slDistance(double lat1, double lon1,
                                    double lat2, double lon2) {
        return haversine(lat1, lon1, lat2, lon2);
    }

    public static double slDistance(Location a, Location b) {
        return slDistance(a.getLat(), a.getLng(), b.getLat(), b.getLng());
    }

    /**
     * SAUCE: https://rosettacode.org/wiki/Haversine_formula#Java
     * @return distance in miles.
     */
    private static double haversine(double lat1, double lon1,
                                    double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c / KM_PER_MILE;
    }
}
