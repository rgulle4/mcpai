package searchtree;

import models.places.Location;

public class Test {
    public static void main(String[] args){
        Search s2 = new Search(new Location("Amite, LA"),
                       new Location("Hayward, CA"));
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
