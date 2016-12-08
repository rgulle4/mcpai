package searchtree;

import models.places.Location;

public class Test {
    public static void main(String[] args){
        Location a = new Location("Amite, LA");
        Location b = new Location("Hayward, CA");
        Search s1 = new Search(a, b);
        s1.setLimit(900);
        s1.uniformSearch(Search.CostType.TIME);
        
        // =>
        // ----------------------
        // Time: 9.223611111111111
        // Price: 370.0789399026043
        // Hayward, CA
        // SJC
        // GPT
        // Amite, LA
        // ----------------------

        s1.uniformSearch(Search.CostType.PRICE);
    
        // =>
        // ----------------------
        // Time: 9.223611111111111
        // Price: 370.0789399026043
        // Hayward, CA
        // SJC
        // GPT
        // Amite, LA
        // ----------------------
    }
}
