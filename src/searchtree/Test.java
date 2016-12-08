package searchtree;

import models.places.Location;

public class Test {
    public static void main(String[] args){
        Location a = new Location("Amite, LA");
        Location b = new Location("Hayward, CA");
        Search s1 = new Search(a, b);
        
        s1.setLimit(900); // dollars
        s1.uniformSearch(Search.CostType.TIME);
        
        // =>
        // ----------------------
        // Time: 6.392222222222222
        // Price: 530.3890315284527
        // Hayward, CA
        // SFO
        // MSY
        // Amite, LA
        // ----------------------
    
        s1.setLimit(900); // hours
        s1.uniformSearch(Search.CostType.PRICE);
    
        // =>
        // ----------------------
        // Time: 31.408055555555556
        // Price: 286.94826184292054
        // Hayward, CA
        // Amite, LA
        // ----------------------
    }
}
