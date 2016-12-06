package searchtree;

public class Test {
    public static void main(String[] args){
        double myLo = -91.1403;
        double myLa = 30.4583;
        double goalLo = -84.3880;
        double goalLa = 33.7490;
        double limit = 70;
        
        Search s = new Search(myLa,myLo,goalLa,goalLo,limit);
    }
}
