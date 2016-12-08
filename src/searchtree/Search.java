package searchtree;

import helpers.Helper;
import mapUtils.DrivingCalculator;
import mapUtils.Flight;
import mapUtils.FlightFinder;
import mapUtils.RoadDistance;
import models.places.Location;

import java.util.*;

public class Search {
    int num = 4;
    Problem p;
    List<Location> startAirports;
    List<Location> goalAirports;
    boolean found;
    double limit;
    
    public enum CostType { TIME, PRICE }
    
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
    }
    
    public double getLimit() {
        return limit;
    }
    
    public Search setLimit(double limit) {
        this.limit = limit;
        return this;
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
        Helper.printDebug(unorganizedSearchData);
    }
    
    /** Search by either costType.TIME or costType.PRICE */
    public void uniformSearch(CostType costType) {
        
        CompareBy myComparator = CompareBy.TIME;
        if (costType == CostType.PRICE)
            myComparator = CompareBy.PRICE;
        
        Node initialNode
              = new Node("root",
              p.getOriginLat(),
              p.getOriginLng(),
              false,
              null);
        initialNode.setLocation(a);
        initialNode.setPathCostTime(0);
        initialNode.setPathCostPrice(0);
        
        // for drawing
        searchDataRoot = new SearchData();
        searchDataRoot.name = initialNode.getLocation().getLocationString();
        addToAllTheNodes(initialNode);
        searchData = new SearchData();
        searchData.name = initialNode.getLocation().getLocationString();
        
        
        PriorityQueue<Node> frontier
              = new PriorityQueue<>(20, myComparator);
        
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
                System.out.println("----------------------");
                writeOutSearchData();
                return;
            }
            
            // There are three cases:
            // current is initial node
            if (current.getParent() == null)
            {
                // add straight drive to goal
                Node goalDrivenTo = new Node(b.getLocationString(),
                      b.getLat(),
                      b.getLng(),
                      false,
                      current);
                goalDrivenTo.setLocation(b);
                setPathCost(current, goalDrivenTo);
                addToAllTheNodes(goalDrivenTo);
                
                // add nearby airports to frontier
                for (Location airport : startAirports)
                {
                    Node child = new Node(airport.getLocationString(),
                          airport.getLat(),
                          airport.getLng(),
                          true,
                          current);
                    child.setLocation(airport);
                    addToAllTheNodes(child);
                    setPathCost(current, child);
                    if ((child.hasConnection()) &&
                        (!explored.contains(child)) &&
                        (child.getPathCostPrice() < p.getLimit())){
                            frontier.add(child);
                    }
                }
                
                Node child = new Node("goal",
                      p.getGoalLat(),
                      p.getGoalLng(),
                      false,current);
                child.setLocation(b);
                child.setGoal(true);
                addToAllTheNodes(child);
                setPathCost(current,child);
                
                if ((child.hasConnection()) &&
                    (!explored.contains(child)) &&
                    (child.getPathCostPrice() < p.getLimit()))
                {
                        frontier.add(child);
                }
            // current is A airport
            }
            else if (!current.getParent().isAirport())
            {
                for (Location airport : goalAirports)
                {
                    Node child = new Node(airport.getLocationString(),
                          airport.getLat(),
                          airport.getLng(),
                          true,
                          current);
                    child.setLocation(airport);
                    addToAllTheNodes(child);
                    setPathCost(current,child);
                    if ((child.hasConnection()) &&
                        (!explored.contains(child)) &&
                        (child.getPathCostPrice() < p.getLimit()))
                    {
                            frontier.add(child);
                    }
                }
            }
            // current is B airport
            else if (current.getParent().isAirport())
            {
                Node child = new Node("goal",
                      p.getGoalLat(),
                      p.getGoalLng(),
                      false,current);
                child.setLocation(b);
                addToAllTheNodes(child);
                setPathCost(current,child);
                if ((child.hasConnection()) &&
                    (!explored.contains(child)) &&
                    (child.getPathCostPrice()<p.getLimit()))
                {
                        frontier.add(child);
                }
            }
        }
        while(!frontier.isEmpty());
        writeOutSearchData();
        return;
    }
    
    private final FlightFinder FF = new FlightFinder();
    private final RoadDistance RD = new RoadDistance();
    private final DrivingCalculator DC = new DrivingCalculator();
    
    public void setPathCost(Node _nodeA, Node _nodeB){
        FF.setMaxPrice(String.valueOf((int) p.getLimit()));
        FF.setMyComparator("duration");
        
        Node nodeA = _nodeA; //from
        Node nodeB = _nodeB; //to
        Location locA = nodeA.getLocation();
        Location locB = nodeB.getLocation();
        double time = 0;
        double price;
        price = 0;
        if ((nodeA.isAirport() == true) && (nodeB.isAirport() == true))
        {
            // Use flight finder to get time and price
            // put nodeB.setNoConnection() if no connection was found
            Flight cheapestFlight = FF.getBestFlight(
                  nodeA.getLocation().getAirportCode(),
                  nodeB.getLocation().getAirportCode(),
                  "2016-12-25");
            if (cheapestFlight == null)
            {
                nodeB.setNoConnection();
            }
            else
            {
                time = cheapestFlight.getDuration();
                price = cheapestFlight.getPrice();
            }
        }
        else
        {
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

final class CompareBy implements Comparator<Node> {
    
    public static final CompareBy TIME
          = new CompareBy(Search.CostType.TIME);
    public static final CompareBy PRICE
          = new CompareBy(Search.CostType.PRICE);
    
    private CompareBy() {}
    private Search.CostType costType;
    private CompareBy(Search.CostType _costType) {
        costType = _costType;
    }
    
    @Override
    public int compare(Node a, Node b){
        double costA = getCost(a);
        double costB = getCost(b);
        if (costA > costB)
            return 1;
        else if (costA < costB)
            return -1;
        return 0;
    }
    
    private double getCost(Node node) {
        if (costType == Search.CostType.PRICE)
            return node.getPathCostPrice();
        return node.getPathCostTime();
    }
}
