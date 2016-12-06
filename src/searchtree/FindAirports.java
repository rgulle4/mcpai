package searchtree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class FindAirports {
    private double myLo,myLa;
    int n;
    public FindAirports(double _la, double _lo, int _n){
        myLo = _lo;
        myLa = _la;
        n = _n;
    }

    public Airport[] nearAirports(){
        Airport[] airports = new Airport[1459];
        Airport[] closeAirports = new Airport[n];
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
                la = Double.parseDouble(line[1]);
                lo = Double.parseDouble(line[2]);
                airports[i] = new Airport(name, la, lo);
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
        //for (int k = 0; k<n; k++){
        //    System.out.println(airports[min[k]].getName());
        //}
        for (int k = 0; k<n; k++){
            closeAirports[k] = airports[min[k]];
        }
        return closeAirports;
    }
}
