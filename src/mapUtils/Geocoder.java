package mapUtils;

import com.google.gson.Gson;
import helpers.Helper;

/**
 * TODO: get coordinates, formatted address, and maybe other info from Google
 *    In the response, we probably just want - results[i].formatted_address
 *                                           - results[i].geometry.location
 *     Pls change the places.Location class as necessary also.
 *
 *     https://gist.github.com/rgulle4/7cdf66ce833d14431dbf926c7a91dbbf
 *     https://maps.googleapis.com/maps/api/geocode/json?&address=baton%20rouge%2C%20LA
 *
 *     https://gist.github.com/rgulle4/df6665dde76b8c779cf799d380e021ca
 *     https://maps.googleapis.com/maps/api/geocode/json?&address=baton%20rouge%2C%20LA
 */
public class Geocoder {
    private static final String GOOGLE_API_KEY = Helper.GOOGLE_API_KEY;
    private static final Gson GSON = new Gson();
}
