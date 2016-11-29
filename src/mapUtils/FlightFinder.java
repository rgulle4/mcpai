package mapUtils;

import com.google.gson.Gson;
import helpers.Helper;
import models.places.Location;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: need to find a good flights API, and figure out a model that works for us.
 * We basically just need to find price, and time between two airports. Probably
 * just the "best" result; or two of them (best time / best price), or a list of them idk.
 *
 * TODO: deal with the rate limit of 50 requests / day!
 */
public final class FlightFinder {
    private static final String GOOGLE_API_KEY = Helper.GOOGLE_API_KEY;
    private static final Gson GSON = new Gson();

    public int numResults;

    String origin = "BTR";
    String destination = "SFO";
    String date = "2016-12-25";
    String maxPrice = "USD700";
    String earliestTime = "";
    String latestTime = "";
    int solutions = 10;

    public FlightFinder setOrigin(String val) {
        origin = val;
        return this;
    }

    public FlightFinder setDestination(String val) {
        destination = val;
        return this;
    }
    public FlightFinder setDate(String val) {
        date = val;
        return this;
    }
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



    public Flight getBestFlight() {
        List<Flight> flights = getFlights();
        if (flights != null)
            return flights.get(0);
        return flights.get(0);
    }

    public List<Flight> getFlights() {
        List<Flight> flights = new ArrayList<Flight>();
        QpxResponse response = getResponse();
        QpxResponse.Trip.TripOption[] tripOptions
              = response.trips.tripOption;
        numResults = tripOptions.length;
        Flight f;
        for (int i = 0; i < numResults; i++) {
            f = new Flight();
            f.setPrice(tripOptions[i].saleTotal);
            f.setDuration(tripOptions[i].slice[0].duration / 60.0);
            f.setOrigin(new Location(origin));
            f.setDestination(new Location(tripOptions[i].
                        pricing[0].fare[tripOptions[i].
                        pricing[0].fare.length - 1].destination));
            flights.add(f);
        }
        return flights;
    }

    public static int requestCounter = 0;

    private QpxResponse getResponse() {
        requestCounter++;
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
}
