package searchtree;

class Node {
    private String name;
    private double lo;
    private double la;
    private boolean airport;
    private Node parent;
    private int pathCostTime;
    private double pathCostPrice;
    private boolean connection;
    public Node(String _name, double _la, double _lo, boolean _airport, Node _parent){
        name = _name;
        lo = _lo;
        la = _la;
        airport = _airport;
        parent = _parent;
        connection = true;
    }
    
    public String getName(){
        return name;
    }
    public boolean isAirport(){
        return airport;
    }
    
    public double getLo(){
        return lo;
    }
    
    public double getLa(){
        return la;
    }
    public Node getParent(){
        return parent;
    }
    public int getPathCostTime(){
        return pathCostTime;
    }
    public double getPathCostPrice(){
        return pathCostPrice;
    }
    public void setPathCostTime(int _pathCostTime){
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
}
