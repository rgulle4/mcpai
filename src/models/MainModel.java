package models;

import com.google.maps.model.LatLng;
import controllers.TripInfoController;
import helpers.Helper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import mapUtils.*;
import models.places.Location;
import searchtree.Search;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public final class MainModel {

    private String testString;
    public TripInfoController tripInfoController;

    private Location startLocation = new Location("MSY");
    private Location destinationLocation = new Location("SFO");
    public Double totalDriveTime;
    public Double totalDriveDistance;
    public double drivePrice;
    public Double driveDuration;
    public double flightPrice;
    public Double flightDuration;
    
    public DoubleProperty totalDriveTimeProperty = new SimpleDoubleProperty();
    public DoubleProperty totalDriveDistanceProperty = new SimpleDoubleProperty();
    public DoubleProperty drivePriceProperty = new SimpleDoubleProperty();
    public DoubleProperty driveDurationProperty = new SimpleDoubleProperty();
    public DoubleProperty flightPriceProperty = new SimpleDoubleProperty();
    public DoubleProperty flightDurationProperty = new SimpleDoubleProperty();
          
    private final RoadDistance RD = new RoadDistance();
    private final DrivingCalculator DC = new DrivingCalculator();
    public Flight bestFlight;
    private final FlightFinder FF = new FlightFinder();
    
    public static final DateTimeFormatter formatter
          = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private LocalDate startDate = LocalDate.of(2016, 12, 25);
    private LocalDate endDate = LocalDate.of(2017, 1, 12);

    private boolean isRoundTrip = false;
    private int maxBudget = 700;
    private double timeWeight = 100;
    
        public MainModel() {
//        calculateDrive();
//        calculateFlight();
    }

    public MainModel(String testString) {
        this();
        setTestString(testString);
    }

    private String searchStrings;
    
    public MainModel search() {
        Location a = this.startLocation.geocode();
        Location b = this.destinationLocation.geocode();
        
        Search s = new Search(a, b, Double.MAX_VALUE);
        
        return this;
    }

    public MainModel calculateDrive() {
        Location a = this.startLocation;
        Location b = this.destinationLocation;
        
        totalDriveTime = RD.getRoadTime(a, b);
        totalDriveTimeProperty.set(totalDriveTime);

        totalDriveDistance = RD.getRoadDistance(a, b);
        totalDriveDistanceProperty.set(totalDriveDistance);
        
        drivePrice = DC.getTripPrice(a, b);
        drivePriceProperty.set(drivePrice);
        
        driveDuration = DC.getTripDuration(a, b);
        driveDurationProperty.set(driveDuration);
    
        checkIfFlyingIsBetter();
        return this;
    }
    
    public MainModel calculateFlight() {
        Location a = this.startLocation;
        Location b = this.destinationLocation;
        
        FF.setOrigin(a.getNearestAirportCode());
        FF.setDestination(b.getNearestAirportCode());
        FF.setDate(startDate.format(formatter));
        bestFlight = FF.getBestFlight();
    
        if (bestFlight == null) {
            flightPrice = 0.0;
            flightDuration = 0.0;
        } else {
            flightPrice = bestFlight.getPrice();
            flightDuration = bestFlight.getDuration();
        }
        
        flightPriceProperty.set(flightPrice);
        flightDurationProperty.set(flightDuration);
        Helper.printDebug("startDate = \"" + startDate.format(formatter) + "\"");
        Helper.printDebug("flightPrice = " + flightPrice);
        Helper.printDebug("flightDuration = " + flightDuration);
        checkIfFlyingIsBetter();
        return this;
    }
    
    public Boolean flyingIsBetter = false;
    public BooleanProperty flyingIsBetterProperty;
    
    public boolean checkIfFlyingIsBetter() {
//        if (flightPrice == null || drivePriceProperty == null) {
//            flyingIsBetter = false;
//            return flyingIsBetter;
//        }
//        if (flightPrice <= drivePrice)
//            flyingIsBetter = true;
//        else
//            flyingIsBetter = false;
//        flyingIsBetterProperty.set(flyingIsBetter);
        return flyingIsBetter;
    }

    public String getTestString() { return testString; }
    public MainModel setTestString(String testString) {
        this.testString = testString;
        return this;
    }

    public Location getStartLocation() { return startLocation; }
    public MainModel setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
        return this;
    }
    public MainModel setStartLocation(String locationString) {
        startLocation.setLocationString(locationString);
        return this;
    }

    public Location getDestinationLocation() { return destinationLocation; }
    public MainModel setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
        return this;
    }
    public MainModel setDestinationLocation(String locationString) {
        destinationLocation.setLocationString(locationString);
        return this;
    }

    public LocalDate getStartDate() { return startDate; }
    public MainModel setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() { return endDate; }
    public MainModel setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getMaxBudget() { return maxBudget; }
    public MainModel setMaxBudget(int maxBudget) {
        if (maxBudget > 0)
            this.maxBudget = maxBudget;
        return this;
    }

    public boolean getIsRoundTrip() { return isRoundTrip; }
    public MainModel setIsRoundTrip(boolean roundTrip) {
        isRoundTrip = roundTrip;
        return this;
    }

    public double getTimeWeight() { return timeWeight; }
    public MainModel setTimeWeight(double timeWeight) {
        this.timeWeight = timeWeight;
        return this;
    }


}
