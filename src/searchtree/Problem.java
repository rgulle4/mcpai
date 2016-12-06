package searchtree;

class Problem {
    private double startLo, startLa, goalLo, goalLa, limit;
    public Problem(double _startLa, double _startLo, double _goalLa, double _goalLo, double _limit){
        startLo = _startLo;
        startLa = _startLa;
        goalLo = _goalLo;
        goalLa = _goalLa;
        limit = _limit;
    }
    public boolean goalTest(Node Node){
        if ((Node.getLa() == goalLa) && (Node.getLo() == goalLo)){
            return true;
        } else {
            return false;
        }
    }
    public double getStartLo(){
        return startLo;
    }
    public double getStartLa(){
        return startLa;
    }
    public double getGoalLa(){
        return goalLa;
    }
    public double getGoalLo(){
        return goalLo;
    }
    public double getLimit(){
        return limit;
    }
}
