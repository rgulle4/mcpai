package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import mapUtils.DrivingCalculator;
import mapUtils.RoadDistance;
import models.places.Location;

import java.time.LocalDate;

public final class MainModel {

    private String testString;

    private Location startLocation = new Location("Hammond, LA");
    private Location destinationLocation = new Location("Vallejo, CA");
    public Double totalDriveTime;
    public Double totalDriveDistance;
    public Double drivePrice;
    public Double driveDuration;
    
    public DoubleProperty totalDriveTimeProperty = new SimpleDoubleProperty();
    public DoubleProperty totalDriveDistanceProperty = new SimpleDoubleProperty();
    public DoubleProperty drivePriceProperty = new SimpleDoubleProperty();
    public DoubleProperty driveDurationProperty = new SimpleDoubleProperty();
    
    private final RoadDistance RD = new RoadDistance();
    private final DrivingCalculator DC = new DrivingCalculator();

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean isRoundTrip = false;
    private int maxBudget = 700;
    private double timeWeight = 100;

    public MainModel() {
        calculateDrive();
    }

    public MainModel(String testString) {
        this();
        setTestString(testString);
    }

    private String searchStrings;

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
        
        return this;
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
