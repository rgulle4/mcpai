package searchtree;

import models.places.Location;

public class Test {
    public static void main(String[] args){
        Location a = new Location("Amite, LA");
        Location b = new Location("Hayward, CA");
        Search s1 = new Search(a, b);
    
        System.out.println("***********************");
        System.out.print("Search by time... ");
        System.out.println(a.getLocationString() + " to " + b.getLocationString());
        s1.setLimit(500); // dollars
        s1.uniformSearch(Search.CostType.TIME);
        
        // =>
        // ----------------------
        // Time: 8.305277777777778
        // Price: 412.2736541510362
        // Hayward, CA
        // SJC
        // BTR
        // Amite, LA
        // ----------------------
        
        System.out.println("***********************");
        System.out.print("Search by price... ");
        System.out.println(a.getLocationString() + " to " + b.getLocationString());
        s1.setLimit(24); // hours
        s1.uniformSearch(Search.CostType.PRICE);
    
        // =>
        // ----------------------
        // Time: 8.598055555555556
        // Price: 364.2446236167799
        // Hayward, CA
        // SJC
        // MSY
        // Amite, LA
        // ----------------------
    }
}
