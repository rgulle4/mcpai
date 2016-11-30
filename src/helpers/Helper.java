package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;

public final class Helper {

    public static boolean DEBUG_MODE = true;

    public static final String GOOGLE_API_KEY
//          = "AIzaSyCVcA7zkndeuPwvejRsbtngKfC-H40Gq8Y";
          = "AIzaSyCxC53nAkMaeEDWFCmCT8JXOA8Qcv8ej0g";
//          = System.getenv("MCPAIGOOGLEAPIKEY");

    public static final GeoApiContext
          GEO_API_CONTEXT = new GeoApiContext().setApiKey(GOOGLE_API_KEY);

    public static final Gson GSON_PP
          = new GsonBuilder().setPrettyPrinting().create();
    public static final Gson GSON
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

    public static void printlnDebug(Object o) {
        if (DEBUG_MODE) System.out.println(o);
    }

    public static void printDebug(Object o) {
        if (DEBUG_MODE) System.out.print(o);
    }

    public static void println(Object o) {
        System.out.println(o);
    }

    public static void println() {
        System.out.println();
    }

    public static void print(Object o) {
        System.out.print(o);
    }
}
