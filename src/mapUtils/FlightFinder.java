package mapUtils;

import com.google.gson.Gson;
import helpers.Helper;
import models.places.Location;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Finds flights. See {@link FlightFinder#getBestFlight(String, String, String)}
 * and {@link FlightFinder#getFlights()}.
 */
public final class FlightFinder {
    private static String GOOGLE_API_KEY = Helper.getApiKey();
    private static final Gson GSON = Helper.GSON;
    private static final long SLEEP_TIME = 1000;
    
    /* -- TODO: cache these objects ---------------------------- */
    
    // required inputs
    private String combinedInputs;
    private String origin = "BTR";
    private String destination = "SFO";
    private String date = "2016-12-25";
    
    // optional inputs
    private String maxPrice = "USD1000";
    private String earliestTime = "";
    private String latestTime = "";
    private int solutions = 15;
    
    // results
    private Flight bestFlight;
    private List<Flight> flights;
    public int numResults;
    
    // api results
    public int requestCounter = 0;
    
    
    
    /* -- constructors  ---------------------------------------- */
    
    /**
     * Empty constructor, but there's three required options before the query:
     *   1. {@link FlightFinder#origin} -- best to use IATA codes
     *      like "BTR", "MSY", etc;
     *   2. {@link FlightFinder#destination} -- same;
     *   3. {@link FlightFinder#date} -- like "2016-12-25".
     */
    public FlightFinder() { /* noop */ }
    
    /**
     * Construct a FlightFinder with the required options: origin, destination,
     * and date.
     * @param origin best to use IATA codes like "BTR", "MSY", etc.
     * @param destination best to use IATA codes like "BTR", "MSY", etc.
     * @param date like "2016-12-25".
     */
    public FlightFinder(String origin, String destination, String date) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }
    
    /* -- required options ------------------------------------- */
    
    /**
     * REQUIRED; best to use IATA codes ("BTR", "MSY", etc).
     * @param val best to use IATA codes ("BTR", "MSY", etc)
     */
    public FlightFinder setOrigin(String val) {
        origin = val;
        return this;
    }
    
    /**
     * Required; best to use IATA codes ("BTR", "MSY", etc).
     * @param val best to use IATA codes ("BTR", "MSY", etc)
     */
    public FlightFinder setDestination(String val) {
        destination = val;
        return this;
    }
    
    /**
     * REQUIRED; like "2016-12-25".
     * @param val like "2016-12-25".
     * @return
     */
    public FlightFinder setDate(String val) {
        date = val;
        return this;
    }
    
    /* -- optional options ------------------------------------- */

    public FlightFinder setMaxPrice(String val) {
        maxPrice = val;
        return this;
    }
    public FlightFinder setEarliestTime(String val) {
        earliestTime = val;
        return this;
    }
    public FlightFinder setLatestTime(String val) {
        latestTime = val;
        return this;
    }
    public FlightFinder setSolutions(int val) {
        this.solutions = val;
        return this;
    }
    
    /* -- the good stuff  -------------------------------------- */
    
    private CompareBy myComparator = CompareBy.TIME;
    
    public CompareBy getMyComparator() {
        return myComparator;
    }
    
    public FlightFinder setMyComparator(String priceOrDuration) {
        myComparator = CompareBy.TIME;
        if (priceOrDuration.trim().equalsIgnoreCase("price")
              || priceOrDuration.trim().equalsIgnoreCase("money")
              || priceOrDuration.trim().equalsIgnoreCase("dallars")
              || priceOrDuration.trim().equalsIgnoreCase("robert")
              || priceOrDuration.trim().equalsIgnoreCase("deniro"))
            myComparator = CompareBy.PRICE;
        return this;
    }
    
    /**
     * Gets the best flight.
     * @param origin eg "btr" (String)
     * @param destination eg "sfo" (String)
     * @param date eg "2016-12-25" (String)
     * @return a {@link Flight} object for the best flight.
     */
    public Flight getBestFlight(String origin, String destination, String date) {
        setOrigin(origin);
        setDestination(destination);
        setDate(date);
        return getBestFlight();
    }
    
    public Flight getBestFlight() {
        List<Flight> flights = getFlights();
        if (flights == null)
            return null;
        return flights.get(0);
    }
    
    /**
     * List of Flights, sorted by price or duration.
     * @return List of Flights, sorted by price or duration.
     */
    public List<Flight> getFlights() {
        List<Flight> flights = new ArrayList<Flight>();
        QpxResponse response = getResponse();
        QpxResponse.Trip.TripOption[] tripOptions
              = response.trips.tripOption;
        if (tripOptions == null)
            return null;
        numResults = tripOptions.length;
        if (numResults == 0)
            return null;
        List<QpxResponse.Trip.TripOption> tripOpts
              = new ArrayList<QpxResponse.Trip.TripOption>(
              Arrays.asList(response.trips.tripOption));
        Flight f;
        for (int i = 0; i < numResults; i++) {
            QpxResponse.Trip.TripOption tripOption = tripOptions[i];
            QpxResponse.Trip.TripOption.Slice firstSlice = tripOption.getFirstSlice();
    
            f = new Flight();
            f.setPrice(tripOption.saleTotal);
            f.setDuration(firstSlice.duration / 60.0);
            f.setOrigin(new Location(origin));
            f.setOrigin(new Location(firstSlice
                  .getFirstSegment().getFirstLeg().origin));
            f.setDestination(new Location(firstSlice
                  .getLastSegment().getLastLeg().destination));
            flights.add(f);
        }
        flights.sort(myComparator);
        return flights;
    }
    
    private static boolean isNotThere(String s) {  return Helper.isNotThere(s); }
    
    private boolean inputsAreInvalid() {
        return isNotThere(origin)
              || isNotThere(destination)
              || isNotThere(date);
    }
    
    /* -- deal with the api ------------------------------------ */
    

    private QpxResponse getResponse() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (inputsAreInvalid())
            return null;
        String urlString = buildUrl();
        System.out.println(urlString);
        String postParams = buildPostParams();
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // send the POST request
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json");
            conn.connect();
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(postParams);
            osw.flush();
            osw.close();

            // get the response
            BufferedReader rd = new BufferedReader(
                  new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
                sb.append(line);
            rd.close();
            conn.disconnect();
            String responseString = sb.toString();
            QpxResponse qpxResponse
                  = GSON.fromJson(responseString, QpxResponse.class);
    
            requestCounter++;
            return qpxResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buildUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.googleapis.com/qpxExpress/v1/trips/search?key=");
        sb.append(GOOGLE_API_KEY);
        return sb.toString();
    }

    private String buildPostParams() {
        PostParams params = new PostParams();
        params.setOrigin(origin);
        params.setDestination(destination);
        params.setDate(date);
        params.setMaxPrice(maxPrice);
        params.setEarliestTime(earliestTime);
        params.setLatestTime(latestTime);
        params.setSolutions(solutions);
        return GSON.toJson(params);
    }

    class PostParams {
        Request request = new Request();

        class Request {
            Slice[] slice = { new Slice() };
            Passengers passengers = new Passengers();
            int solutions = 10;
            String maxPrice;
            boolean refundable = false;

            class Slice {
                String origin = "MSY";
                String destination = "ATL";
                String date = "2016-12-10";
                PermittedDepartureTime permittedDepartureTime
                      = new PermittedDepartureTime();

                class PermittedDepartureTime {
                    String earliestTime = "";
                    String latestTime = "";
                }
            }

            class Passengers {
                int adultCount = 1;
                int childCount = 0;
            }
        }
        PostParams setMaxPrice(String val) {
            maxPrice = val;
            return this;
        }
        PostParams setOrigin(String val) {
            request.slice[0].origin = val;
            return this;
        }
        PostParams setDestination(String val) {
            request.slice[0].destination = val;
            return this;
        }
        PostParams setDate(String val) {
            request.slice[0].date = val;
            return this;
        }
        PostParams setEarliestTime(String val) {
            request.slice[0]
                  .permittedDepartureTime.earliestTime = val;
            return this;
        }
        PostParams setLatestTime(String val) {
            request.slice[0]
                  .permittedDepartureTime.latestTime = val;
            return this;
        }
        PostParams setAdultCount(int val) {
            request.passengers.adultCount = val;
            return this;
        }
        PostParams setSolutions(int val) {
            request.solutions = val;
            return this;
        }
        PostParams setRefundable(boolean val) {
            request.refundable = val;
            return this;
        }
    }

    public class QpxResponse {
        String kind;
        Trip trips;

        class Trip {
            Data data;
            String kind;
            String requestId;
            TripOption[] tripOption;

            class Data {
                Aircraft[] aircraft;
                Airport[] airport;
                Carrier[] carrier;
                City[] city;
                String kind;
//            Tax tax;

                class Aircraft {
                    String code;
                    String kind;
                    String name;
                }

                class Airport {
                    String city;
                    String code;
                    String kind;
                    String name;
                }

                class Carrier {
                    String code;
                    String kind;
                    String name;
                }

                class City {
                    String code;
                    String kind;
                    String name;
                }
            }

            class TripOption {
                String id;
                String kind;
                Pricing[] pricing;
                String saleTotal;
                Slice[] slice;
                
                Slice getLastSlice() {
                    int len = slice.length;
                    if (slice == null || len < 1)
                        return null;
                    return slice[len - 1];
                }
    
                public Slice getFirstSlice() {
                    int len = slice.length;
                    if (slice == null || len < 1)
                        return null;
                    return slice[0];
                }
    
                class Pricing {
                    String baseFareTotal;
                    String saleFareTotal;
                    String saleTaxTotal;
                    String saleTotal;
                    Fare[] fare;

                    class Fare {
                        String basisCode;
                        String carrier;
                        String destination;
                        String id;
                        String kind;
                        String origin;
                    }
                }

                class Slice {
                    int duration;
                    String kind;
                    Segment[] segment;
                    private Segment lastSegment;
    
                    public Segment getFirstSegment() {
                        int len = segment.length;
                        if (segment == null || len < 1)
                            return null;
                        return segment[0];
                    }
    
                    public Segment getLastSegment() {
                        int len = segment.length;
                        if (segment == null || len < 1)
                            return null;
                        return segment[len - 1];
                    }
    
                    class Segment {
                        String bookingCode;
                        int bookingCodeCount;
                        String cabin;
                        int connectiionDuration;
                        int duration;
                        Flight flight;
                        String id;
                        String kind;
                        Leg[] leg;
    
                        public Leg getFirstLeg() {
                            int len = leg.length;
                            if (leg == null || len < 1)
                                return null;
                            return leg[0];
                        }
    
                        public Leg getLastLeg() {
                            int len = leg.length;
                            if (leg == null || len < 1)
                                return null;
                            return leg[len - 1];
                        }
    
                        class Flight {
                            String carrier;
                            String number;
                        }

                        class Leg {
                            String aircraft;
                            String arrivalTime;
                            String departureTime;
                            String destination;
                            String destinationTerminal;
                            int duration;
                            String id;
                            String kind;
                            int mileage;
                            int onTimePerformance;
                            String origin;
                            boolean secure;
                        }
                    }
                }
            }
        }
    }
    
    public enum CostType { TIME, PRICE }
}

final class CompareBy implements Comparator<Flight> {
    
    public static final CompareBy TIME
          = new CompareBy(FlightFinder.CostType.TIME);
    public static final CompareBy PRICE
          = new CompareBy(FlightFinder.CostType.PRICE);
    
    private CompareBy() {}
    private FlightFinder.CostType costType;
    private CompareBy(FlightFinder.CostType _costType) {
        costType = _costType;
    }
    
    @Override
    public int compare(Flight a, Flight b){
        double costA = getCost(a);
        double costB = getCost(b);
        if (costA > costB)
            return 1;
        else if (costA < costB)
            return -1;
        return 0;
    }
    
    private double getCost(Flight f) {
        if (costType == FlightFinder.CostType.PRICE)
            return f.getPrice();
        return f.getDuration();
    }
}
