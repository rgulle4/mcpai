package models.places;

public final class Airport extends Location {

    private String airportSearchString;
    private String airportCode;

    public Airport() { super(); }
    public Airport(String airportSearchString) {
        this();
        setAirportSearchString(airportSearchString);

    }

    @Override
    public boolean isAirport() { return true; }

    public String getAirportSearchString() { return airportSearchString; }
    public void setAirportSearchString(String airportSearchString) {
        this.airportSearchString = airportSearchString;
    }

    public String getAirportCode() { return airportCode; }
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    private void findAirPortCode() {
        // TODO: implement this... not sure if this is useful though
    }

}
