package mapUtils;

import helpers.Helper;
import models.places.Location;

/**
 * Figures out total price (gas + hotel) and duration (driving plus sleep) of a
 * road trip. Main inputs are miles and hours (which you can get from
 * {@link RoadDistance}).
 * Key constructor:
 *   {@link DrivingCalculator(double, double)}
 * Key methods:
 *   {@link DrivingCalculator#calc()},
 *   {@link DrivingCalculator#calc(double, double)}
 */
public final class DrivingCalculator {

    /* -- fields -------------------------------------------- */

    private double miles = 0;         // input (required)
    private double hours = 0;         // input (optional, can be calculated)
    
    /* -- calculated fields --------------------------------- */
    
    private double tripPrice = 0;     // calculated
    private double tripDuration = 0;  // calculated

    /* -- customizable assumptions -------------------------- */

    // assumed speed, only used if input hours isn't set
    private static final double DEFAULT_ASSUMED_MILES_PER_HOUR = 70.0;
    private double assumedMilesPerHour = DEFAULT_ASSUMED_MILES_PER_HOUR;

    private double milesPerGallon = 35.0;
    private double dollarsPerGallon = 2.15;

    private int hoursPerDay = 12;
    private int hoursPerSleep = 8;
    private double hotelPrice = 75.0;

    /* -- constructor(s) ------------------------------------ */

    /**
     * Construct a new driving calculator without miles or hours, so you'll need
     * to do setMiles() and setHours() before you get a useful calc().
     */
    public DrivingCalculator() { /* noop */ }

    /**
     * Construct a new driving calculator, and immediately calc tripPrice and
     * tripDuration.
     * @param miles the trip's total driving miles
     * @param hours the trip's total driving hours.
     */
    public DrivingCalculator(double miles, double hours) {
        this.miles = miles;
        this.hours = hours;
        calc();
    }
    
    private static final RoadDistance RD = new RoadDistance();
    
    /**
     * Construct a new driving calculator using two Location objects, and
     * immediately calc tripPrice and tripDuration.
     */
    public DrivingCalculator(Location originLocation, Location destinationLocation) {
        calc(originLocation, destinationLocation);
    }

    /* -- calculate the cost and duration ------------------- */
    
    public boolean hasNoInputForMiles() { return miles == 0; }
    public boolean hasNoInputForHours() { return hours == 0; }
    public boolean hasInputForMiles() { return miles != 0; }
    public boolean hasInputForHours() { return hours != 0; }
    
    private boolean hasNoAssumedMilesPerHour() {
        return assumedMilesPerHour == 0;
    }
    private boolean hasAssumedMilesPerHour() {
        return assumedMilesPerHour != 0;
    }
    private double getAssumedHours() {
        if (hasInputForHours())
            return 0;
        return miles / assumedMilesPerHour;
    }
    
    /**
     * Gets trip price, including hotels, with customizable assumptions:
     *
     *     miles = taken from RoadDistance.getRoadDistance()
     *     hours = taken from RoadDistance.getRoadTime()
     *     assumedMilesPerHour = 70 (only used if hours isn't set)
     *     milesPerGallon = 35.0
     *     dollarsPerGallon = 2.15
     *     hoursPerDay = 12
     *     hoursPerSleep = 8
     *     hotelPrice = 75.0
     */
    public double calc(Location originLocation, Location destinationLocation) {
        miles = RD.getRoadDistance(originLocation, destinationLocation);
        hours = RD.getRoadTime(originLocation, destinationLocation);
        return calc(miles, hours);
    }

    /**
     * Gets trip price, including hotels, with customizable assumptions:
     *
     *     miles = 0 (unless already set);
     *     hours = 0 (unless already set, will be calculated assumedMilesPerHour)
     *     assumedMilesPerHour = 70 (only used if hours isn't set)
     *     milesPerGallon = 35.0
     *     dollarsPerGallon = 2.15
     *     hoursPerDay = 12
     *     hoursPerSleep = 8
     *     hotelPrice = 75.0
     */
    public double calc() {
        double miles = this.miles;
        double hours = this.hours;
        if (hasNoInputForMiles())
            return 0;
        if (hasNoInputForHours())
            hours = getAssumedHours();

        double gasPrice = miles / milesPerGallon * dollarsPerGallon;

        int hoursInt = (int) Math.ceil(hours);

        int numberOfSleeps = hoursInt / hoursPerDay;
        double hotelPrice = numberOfSleeps * this.hotelPrice;


        this.tripDuration = (numberOfSleeps * hoursPerSleep)
              + (numberOfSleeps * hoursPerDay)
              + (hours - (numberOfSleeps * hoursPerDay));

        this.tripPrice = gasPrice + hotelPrice;
        
        printDebug();
        return this.tripPrice;
    }
    
    private void printDebug() {
        if (!Helper.DEBUG_MODE)
            return;
        System.out.printf("DrivingCalculator.calc(%.1f miles, %.1f hours)...\n",
              this.miles,
              this.hours);
        System.out.printf("    tripPrice    = %.2f\n",
              this.tripPrice);
        System.out.printf("    tripDuration = %.2f\n",
              this.tripDuration);
        Helper.println();
    }

    /**
     * Gets trip price, including hotels, with customizable assumptions:
     *
     *     hours = 0 (we'll calculate it using assumedMilesPerHour)
     *     assumedMilesPerHour = 70;
     *     milesPerGallon = 35.0
     *     dollarsPerGallon = 2.15
     *     hoursPerDay = 12
     *     hoursPerSleep = 8
     *     hotelPrice = 75.0
     */
    public double calc(double miles) {
        this.miles = miles;
        this.hours = 0;
        return calc();
    }


    /**
     * Gets trip price, including hotels, with customizable assumptions:
     *
     *     milesPerGallon = 35.0
     *     dollarsPerGallon = 2.15
     *     hoursPerDay = 12
     *     hoursPerSleep = 8
     *     hotelPrice = 75.0
     */
    public double calc(double miles, double hours) {
        this.miles = miles;
        this.hours = hours;
        return calc();
    }

    
    /* -- getters and setters for input fields -------------- */
    
    public double getMiles() {
        return miles;
    }
    
    public DrivingCalculator setMiles(double miles) {
        this.miles = miles;
        return this;
    }
    
    public double getHours() {
        return hours;
    }
    
    public DrivingCalculator setHours(double hours) {
        this.hours = hours;
        return this;
    }
    
    /* -- getters and setters for calculated fields --------- */
    
    public double getTripPrice() {
        calc();
        return tripPrice;
    }
    
    public double getTripPrice(Location a, Location b) {
        calc(a, b);
        return tripPrice;
    }
    
    public double getTripDuration() {
        calc();
        return tripDuration;
    }
    
    public double getTripDuration(Location a, Location b) {
        calc(a, b);
        return tripDuration;
    }
    
    /* -- getters and setters for assumptions fields -------- */
    
    public double getAssumedMilesPerHour() {
        return assumedMilesPerHour;
    }
    
    public DrivingCalculator setAssumedMilesPerHour(
          double assumedMilesPerHour)
    {
        this.assumedMilesPerHour = assumedMilesPerHour;
        return this;
    }
    
    public double getMilesPerGallon() {
        return milesPerGallon;
    }
    
    public DrivingCalculator setMilesPerGallon(double milesPerGallon) {
        this.milesPerGallon = milesPerGallon;
        return this;
    }
    
    public double getDollarsPerGallon() {
        return dollarsPerGallon;
    }
    
    public DrivingCalculator setDollarsPerGallon(double dollarsPerGallon) {
        this.dollarsPerGallon = dollarsPerGallon;
        return this;
    }
    
    public int getHoursPerDay() {
        return hoursPerDay;
    }
    
    public DrivingCalculator setHoursPerDay(int hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
        return this;
    }
    
    public int getHoursPerSleep() {
        return hoursPerSleep;
    }
    
    public DrivingCalculator setHoursPerSleep(int hoursPerSleep) {
        this.hoursPerSleep = hoursPerSleep;
        return this;
    }
    
    public double getHotelPrice() {
        return hotelPrice;
    }
    
    public DrivingCalculator setHotelPrice(double hotelPrice) {
        this.hotelPrice = hotelPrice;
        return this;
    }
}
