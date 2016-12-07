package searchtree;

import models.places.Location;

public class Node{
    private String name;
    private Location location;
    private double lng;
    private double lat;
    private boolean isAirport;
    private Node parent;
    private String parentLocationName;
    private double pathCostTime;
    private double pathCostPrice;
    
    public boolean isGoal() {
        return isGoal;
    }
    
    public Node setGoal(boolean goal) {
        isGoal = goal;
        return this;
    }
    
    private boolean isGoal;
    private boolean connection;
    
    public Node(String name, double lat, double lng, boolean isAirport, Node parent){
        this.name = name;
        this.lng = lng;
        this.lat = lat;
        this.isAirport = isAirport;
        if (parent != null) {
            this.parent = parent;
            this.parentLocationName = parent.getLocation().getLocationString();
        }
        connection = true;
    }

    public String getName(){
        return name;
    }
    public boolean isAirport(){
        return isAirport;
    }

    public double getLng(){
        return lng;
    }

    public double getLat(){
        return lat;
    }
    public Node getParent(){
        return parent;
    }
    public double getPathCostTime(){
        return pathCostTime;
    }
    public double getPathCostPrice(){
        return pathCostPrice;
    }
    public void setPathCostTime(double _pathCostTime){
        pathCostTime = _pathCostTime;
    }
    public void setPathCostPrice(double _pathCostPrice){
        pathCostPrice = _pathCostPrice;
    }
    public boolean hasConnection(){
        return connection;
    }
    public void setNoConnection(){
        connection = false;
    }
    
    public Node setName(String name) {
        this.name = name;
        return this;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public Node setLocation(Location location) {
        this.location = location;
        return this;
    }
    
    public Node setLng(double lng) {
        this.lng = lng;
        return this;
    }
    
    public Node setLat(double lat) {
        this.lat = lat;
        return this;
    }
    
    public Node setAirport(boolean airport) {
        isAirport = airport;
        return this;
    }
    
    public Node setParent(Node parent) {
        this.parent = parent;
        return this;
    }
    
    public boolean isConnection() {
        return connection;
    }
    
    public Node setConnection(boolean connection) {
        this.connection = connection;
        return this;
    }
}
