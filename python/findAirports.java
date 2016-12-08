import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class findAirports {
    
    public static void main(String[] args) {
        airport[] airports = new airport[1459]; //stores Airport information
        
        //my location
        double myLo = 30.4583;
        double myLa = -91.1403;
        
        int n = 4; // num of airports
        int[] min = new int[n];
        double[] x = new double[n];
        double[] y = new double[n];
        double currx,curry;
        
        try {
            File text = new File("USAirportCoords.txt");

            Scanner scnr = new Scanner(text);
            int i = 0;
            String name;
            double lo,la;
            while(scnr.hasNextLine()){
                String l = scnr.nextLine();
                String[] line = l.split(",");
                name = line[0].substring(1,4);
                lo = Double.parseDouble(line[1]);
                la = Double.parseDouble(line[2]);
                airports[i] = new airport(name, lo, la);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
        for (int j = 0; j<1459; j++){
            //get coordinates of current Airport
            currx = airports[j].getLo();
            curry = airports[j].getLa();
            
            //get coordinates of min airports
            for (int k = 0; k<n; k++){
                x[k] = airports[min[k]].getLo();
                y[k] = airports[min[k]].getLa();
            }
            
            //check if closer than mins
            for (int k = 0; k<n; k++) {
                if ( (Math.pow(currx-myLo, 2) + Math.pow(curry-myLa, 2)) < (Math.pow(x[k]-myLo, 2) + Math.pow(y[k]-myLa,2)) ){
                    //push back mins
                    for (int h = n-1; h>k; h--) {
                        min[h] = min[h-1];
                    }
                    min[k] = j;
                    break;
                }
            }
        }
        for (int k = 0; k<n; k++){
            System.out.println(airports[min[k]].getName());
        }
    }
}

class airport{
    private String name;
    private double lo;
    private double la;
    public airport(String _name, double _lo, double _la){
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
