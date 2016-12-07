package searchtree;

import helpers.Helper;
import mapUtils.DrivingCalculator;
import mapUtils.Flight;
import mapUtils.FlightFinder;
import mapUtils.RoadDistance;
import models.places.Location;

import java.util.*;
//import java.util.Collections;
//import java.util.List;
//import java.util.ArrayList;


public class Search {
    int num = 4;
    Problem p;
    List<Location> startAirports;
    List<Location> goalAirports;
    boolean found;
    
    public Search(Location a, Location b) {
        this(a, b, Double.MAX_VALUE);
    }
    
    public Location a;
    public Location b;
    
    public Search(Location a, Location b, double limit) {
        this.a = a;
        this.b = b;
        startAirports = a.getNearbyAirports();
        goalAirports = b.getNearbyAirports();
        p = new Problem(a.getLat(),
              a.getLng(),
              b.getLat(),
              b.getLng(),
              limit);
        uniformSearch();
    }
    
    private void writeOutSearchData() {
//        System.out.println("----------------------");
//        System.out.println("----------------------");
//        System.out.println("----------------------");
//        Helper.printObject(allTheNodes);
//        System.out.println("----------------------");
//        System.out.println("----------------------");
//        System.out.println("----------------------");
//        System.out.println("----------------------");
//        Helper.printObject(searchData);
//        searchData = buildSearchDataTree(searchDataRoot);
//        Helper.printObject(searchData);
    }
    
    private SearchData buildSearchDataTree(SearchData currentNode) {
        // TODO
        return currentNode;
    }
    
    Set<String> unorganizedSearchData = new HashSet<>();
    SearchData searchData;
    SearchData searchDataRoot;
    Set<Node> allTheNodes = new LinkedHashSet<>();
    
    private void addToAllTheNodes(Node n) {
        allTheNodes.add(n);
        unorganizedSearchData.add(Helper.GSON_PP.toJson(n));
        System.out.println("***********************************");
        System.out.println("***********************************");
        Helper.printDebug(unorganizedSearchData);
        System.out.println("***********************************");
        System.out.println("***********************************");
    
    }
    
    public void uniformSearch() {
        
        Node initialNode
              = new Node("root",
              p.getOriginLat(),
              p.getOriginLng(),
              false,
              null);
        initialNode.setLocation(a);
        initialNode.setPathCostTime(0);
        initialNode.setPathCostPrice(0);
        
        searchDataRoot = new SearchData();
        searchDataRoot.name = initialNode.getLocation().getLocationString();
        addToAllTheNodes(initialNode);
    
        searchData = new SearchData();
        searchData.name = initialNode.getLocation().getLocationString();
        
        
        PriorityQueue<Node> frontier = new PriorityQueue<Node>(20,
            new Comparator<Node>() {
            //override compare method
                public int compare(Node nodeA, Node nodeB){
                    if (nodeA.getPathCostTime() > nodeB.getPathCostTime()) {
                        return 1;
                    } else if (nodeA.getPathCostTime() < nodeB.getPathCostTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        );
    
        
        
        frontier.add(initialNode);
        Set<Node> explored = new HashSet<Node>();
        found = false;

        do {
            Node current = frontier.poll();
            explored.add(current);
            
            // write to JSON shape, if current node is the root
            if (searchData == null) {
                searchData = new SearchData();
                searchData.name = current.getLocation().getLocationString();
            } else {
                
            }

            if (p.goalTest(current)){
                found = true;
                System.out.println("----------------------");
                System.out.println("----------------------");
                System.out.println("Time: "+current.getPathCostTime());
                System.out.println("Price: "+current.getPathCostPrice());
                while (current != null){
                    System.out.println(current.getLocation().getLocationString());
                    current = current.getParent();
                }
                System.out.println("----------------------");
                writeOutSearchData();
                return;
            }
            // There are three cases:
            // current is initial node
            if (current.getParent() == null){
                for (Location airport : startAirports){
                    Node child = new Node(airport.getLocationString(),
                                          airport.getLat(),
                                          airport.getLng(),
                                          true,
                                          current);
                    child.setLocation(airport);
                    addToAllTheNodes(child);
                    pathCost(current,child);
                    if ((child.hasConnection()) &&
                        (!explored.contains(child)) &&
                        (child.getPathCostPrice()<p.getLimit())){
                            frontier.add(child);
                    }
                }
                Node child = new Node("goal", p.getGoalLat(), p.getGoalLng(), false,current);
                child.setLocation(b);
                child.setGoal(true);
                addToAllTheNodes(child);
                pathCost(current,child);
                if ((child.hasConnection()) &&
                    (!explored.contains(child)) &&
                    (child.getPathCostPrice()<p.getLimit())){
                        frontier.add(child);
                }
            // current is A airport
            } else if (!current.getParent().isAirport()){
                for (Location airport : goalAirports){
                    Node child = new Node(airport.getLocationString(),
                                          airport.getLat(),
                                          airport.getLng(),
                                          true,
                                          current);
                    child.setLocation(airport);
                    addToAllTheNodes(child);
                    pathCost(current,child);
                    if ((child.hasConnection()) &&
                        (!explored.contains(child)) &&
                        (child.getPathCostPrice()<p.getLimit())){
                            frontier.add(child);
                    }
                }
            // current is B airport
            } else if (current.getParent().isAirport()){
                Node child = new Node("goal", p.getGoalLat(), p.getGoalLng(), false,current);
                child.setLocation(b);
                addToAllTheNodes(child);
                pathCost(current,child);
                if ((child.hasConnection()) &&
                    (!explored.contains(child)) &&
                    (child.getPathCostPrice()<p.getLimit())){
                        frontier.add(child);
                }
            }
        } while(!frontier.isEmpty());
        writeOutSearchData();
        return;
    }
    
    private final FlightFinder FF = new FlightFinder();
    private final RoadDistance RD = new RoadDistance();
    private final DrivingCalculator DC = new DrivingCalculator();
    
    public void pathCost(Node _nodeA, Node _nodeB){
        Node nodeA = _nodeA; //from
        Node nodeB = _nodeB; //to
        Location locA = nodeA.getLocation();
        Location locB = nodeB.getLocation();
        double time = 0;
        double price;
        price = 0;
        if ((nodeA.isAirport() == true) && (nodeB.isAirport() == true)) {
            // Use flight finder to get time and price
            // put nodeB.setNoConnection() if no connection was found
            Flight cheapestFlight = FF.getBestFlight(nodeA.getLocation().getAirportCode(),
                  nodeB.getLocation().getAirportCode(), "2016-12-25");
            if (cheapestFlight == null)
                nodeB.setNoConnection();
            else {
                time = cheapestFlight.getDuration();
                price = cheapestFlight.getPrice();
            }
            // Use road finder
//        } else if((nodeA.isAirport() == false) && (nodeB.isAirport() == true)) {
//            time = RD.getRoadTime(nodeA.getLocation(), nodeB.getLocation());
//            price = DC.getTripPrice(nodeA.getLocation(), nodeB.getLocation());
//
//        } else if ((nodeA.isAirport() == true) && (nodeB.isAirport() == false)){
//            time = RD.getRoadTime(nodeA.getLocation(), nodeB.getLocation());
//            price = DC.getTripPrice(nodeA.getLocation(), nodeB.getLocation());
//
//        } else if ((nodeA.isAirport() == false) && (nodeB.isAirport() == false)){
        } else {
            time = RD.getRoadTime(nodeA.getLocation(), nodeB.getLocation());
            price = DC.getTripPrice(nodeA.getLocation(), nodeB.getLocation());
        }
        nodeB.setPathCostTime(time + nodeA.getPathCostTime());
        nodeB.setPathCostPrice(price + nodeA.getPathCostPrice());
    }
    
    class SearchData {
        String name;
        Boolean isgoal;
        Double value;
        Double timeCost;
        Double priceCost;
        ArrayList<SearchData> children;
    }
}
