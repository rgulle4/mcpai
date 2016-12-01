package mapUtils;

import com.google.maps.model.LatLng;
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

    /**
     * Returns straight line distance between two points on Earth.
     * @param latA Latitude of pointA.
     * @param lngA Longitude of pointA.
     * @param latB Latitude of pintB.
     * @param lngB Longitude of pintB.
     * @return Straight line distance in miles.
     */
    public static double slDistance(double latA, double lngA,
                                    double latB, double lngB)
    {
        return haversine(latA, lngA, latB, lngB);
    }
    
    /**
     * Returns straight line distance between two points on Earth.
     * @param latlngA LatLng of pointA.
     * @param latlngB LatLng of pointB.
     * @return Straight line distance in miles.
     */
    public static double slDistance(LatLng latlngA, LatLng latlngB) {
        return slDistance(
              latlngA.lat, latlngA.lng,
              latlngB.lat, latlngB.lng);
    }
    
    /**
     * Returns straight line distance between two points on Earth.
     * @param a A valid Location object that has a coordinate.
     * @param b A valid Location object that has a coordinate.
     * @return distance in miles.
     */
    public static double slDistance(Location a, Location b)
    {
        return slDistance(a.getLat(), a.getLng(), b.getLat(), b.getLng());
    }

    /**
     * SAUCE: https://rosettacode.org/wiki/Haversine_formula#Java
     * @return Straight line distance in miles.
     */
    private static double haversine(double latA, double lngA,
                                    double latB, double lngB)
    {
        double dLat = Math.toRadians(latB - latA);
        double dLng = Math.toRadians(lngB - lngA);
        latA = Math.toRadians(latA);
        latB = Math.toRadians(latB);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLng / 2),2) * Math.cos(latA) * Math.cos(latB);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c / KM_PER_MILE;
    }
}
