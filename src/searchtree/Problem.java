package searchtree;

public class Problem {
    private double originLat, originLng, goalLat, goalLng, limit;
    
    public Problem(double _originLat, double _originLng,
                   double _goalLat, double _goalLng)
    {
        this(_originLat, _originLng, _goalLat, _goalLng, Double.MAX_VALUE);
    }
    public Problem(double _originLat, double _originLng,
                   double _goalLat, double _goalLng, double _limit)
    {
        originLng = _originLng;
        originLat = _originLat;
        goalLng = _goalLng;
        goalLat = _goalLat;
        limit = _limit;
    }
    
    public boolean goalTest(Node node) {
        if ((node.getLat() == goalLat) && (node.getLng() == goalLng)){
            return true;
        } else {
            return false;
        }
    }
    
    public double getOriginLng(){
        return originLng;
    }
    public double getOriginLat(){
        return originLat;
    }
    public double getGoalLat(){
        return goalLat;
    }
    public double getGoalLng(){
        return goalLng;
    }
    public double getLimit(){
        return limit;
    }
}
