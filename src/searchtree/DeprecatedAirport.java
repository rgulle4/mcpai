package searchtree;

class DeprecatedAirport {
    private String name;
    private double lng;
    private double lat;
    public DeprecatedAirport(String _name, double _la, double _lo){
        name = _name;
        lng = _lo;
        lat = _la;
    }
    public String getName(){
        return name;
    }
    public double getLng(){
        return lng;
    }
    public double getLat(){
        return lat;
    }
}
