package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;

public final class Helper {

    public static final String GOOGLE_API_KEY = "AIzaSyCVcA7zkndeuPwvejRsbtngKfC-H40Gq8Y";
//          = System.getenv("MCPAIGOOGLEAPIKEY");

    public static final GeoApiContext
          GEO_API_CONTEXT = new GeoApiContext().setApiKey(GOOGLE_API_KEY);

    public static final Gson GSON_PP
          = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON
          = new Gson();

    public static void printObject(Object o) {
        System.out.print(o + ": ");
        System.out.println(GSON_PP.toJson(o));
    }

    public static void printObject(Object o, String label) {
        System.out.print("\"" + label + "\": ");
        printObject(o);
    }

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }

    public static String toJsonPretty(Object o) {
        return GSON_PP.toJson(o);
    }
}
