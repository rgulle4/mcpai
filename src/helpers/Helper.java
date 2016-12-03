package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import mapUtils.StraightLineDistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Scanner;

public final class Helper {

    public static boolean DEBUG_MODE = true;

    /* ---------------------------------------------------- */
    
    private static String GOOGLE_API_KEY;
    
    private static final String CREDENTIALS_PATH
          = "config/credentials/";
    private static final String KEYS_FILE_NAME
          = "google.txt";
    private static final String KEYS_FILE_PATH
          = CREDENTIALS_PATH + KEYS_FILE_NAME;
    private static final File KEYS_FILE
          = new File(KEYS_FILE_PATH);
    
    private static Scanner fileScanner;
    private static Scanner lineScanner;
    
    /** get a particular api key */
    public static String getApiKey(int n) {
        try {
            int i = 0;
            fileScanner = new Scanner(KEYS_FILE);
            while (i <= n && fileScanner.hasNextLine()) {
                if (i != n) {
                    fileScanner.nextLine();
                } else {
                    lineScanner = new Scanner(fileScanner.nextLine());
                    GOOGLE_API_KEY = lineScanner.next();
                    lineScanner.close();
                    fileScanner.close();
                    return GOOGLE_API_KEY;
                }
                i++;
            }
            lineScanner.close();
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.err.println("ERROR: cant getApiKey(" + n + ")");
        return GOOGLE_API_KEY;
    }
    
    /** get the last used api key, or first key if not yet set */
    public static String getApiKey() {
        if (GOOGLE_API_KEY == null)
            GOOGLE_API_KEY = getApiKey(0);
        return GOOGLE_API_KEY;
    }
    
    /* ---------------------------------------------------- */

    public static final GeoApiContext GEO_API_CONTEXT
          = new GeoApiContext().setApiKey(getApiKey());
    public static final Gson GSON_PP
          = new GsonBuilder().setPrettyPrinting().create();
    public static final Gson GSON
          = new Gson();

    /* ---------------------------------------------------- */

    public static final StraightLineDistance SLD
          = new StraightLineDistance();
    
    /* ---------------------------------------------------- */
    
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
    
    /* ---------------------------------------------------- */
    
    public static void printlnDebug(Object o) {
        if (DEBUG_MODE) System.out.println(o);
    }

    public static void printDebug(Object o) {
        if (DEBUG_MODE) System.out.print(o);
    }
    
    /* ---------------------------------------------------- */

    public static void println(Object o) {
        System.out.println(o);
    }

    public static void println() {
        System.out.println();
    }

    public static void print(Object o) {
        System.out.print(o);
    }
    
    public static void printError(Object o) {
        System.err.println(o);
    }
    
    public static boolean areEqual(String s1, String s2) {
        return s1.equalsIgnoreCase(s2);
    }
    
    public static boolean isThere(String s) {
        return !(isNotThere(s));
    }
    
    public static boolean isNotThere(String s) {
        return (s == null || s.isEmpty() || s.trim().isEmpty());
    }
}
