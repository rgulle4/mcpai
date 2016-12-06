package searchtree;

class Airport{
    private String name;
    private double lo;
    private double la;
    public Airport(String _name, double _la, double _lo){
        name = _name;
        lo = _lo;
        la = _la;
    }
    public String getName(){
        return name;
    }
    public double getLo(){
        return lo;
    }
    public double getLa(){
        return la;
    }
}
