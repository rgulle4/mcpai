package mapUtils;

import com.google.gson.Gson;
import helpers.Helper;

/**
 * TODO: need to find a good flights API, and figure out a model that works for us.
 * We basically just need to find price, and time between two airports. Probably
 * just the "best" result; or two of them (best time / best price), or a list of them idk.
 *
 * TODO: deal with the rate limit of 50 requests / day!
 */
public class Flights {
    private static final String GOOGLE_API_KEY = Helper.GOOGLE_API_KEY;
    private static final Gson GSON = new Gson();
}
