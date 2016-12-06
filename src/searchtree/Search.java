package searchtree;

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;
//import java.util.Collections;
//import java.util.List;
//import java.util.ArrayList;
import java.util.Comparator;

class Search {
    int num = 4;
    Problem p;
    Airport[] startAirports = new Airport[num];
    Airport[] goalAirports = new Airport[num];
    boolean found;

    public Search(double startLa, double startLo,
                  double goalLa, double goalLo, double limit){
        p = new Problem(startLa,startLo,goalLa,goalLo,limit);
        getAirports();
        uniform_search();
    }

    public void uniform_search(){
        Node initialNode = new Node("root",p.getStartLa(),p.getStartLo(),false, null);
        initialNode.setPathCostTime(0);
        initialNode.setPathCostPrice(0);
        PriorityQueue<Node> frontier = new PriorityQueue<Node>(20,
            new Comparator<Node>(){
            //override compare method
                public int compare(Node nodeA, Node nodeB){
                    if (nodeA.getPathCostTime() > nodeB.getPathCostTime()){
                        return 1;
                    } else if (nodeA.getPathCostTime() < nodeB.getPathCostTime()){
                        return -1;
                    } else{
                        return 0;
                    }
                }
            }
        );

        frontier.add(initialNode);
        Set<Node> explored = new HashSet<Node>();
        found = false;

        do{
            Node current = frontier.poll();
            explored.add(current);

            if (p.goalTest(current)){
                found = true;
                System.out.println("Time: "+current.getPathCostTime());
                System.out.println("Price: "+current.getPathCostPrice());
                while (current != null){
                    System.out.println(current.getName());
                    current = current.getParent();
                }
                return;
            }
            // There are three cases:
            // current is initial node
            if (current.getParent() == null){
                for (Airport airports : startAirports){
                    Node child = new Node(airports.getName(),
                                          airports.getLa(),
                                          airports.getLo(),
                                          true,
                                          current);
                    pathCost(current,child);
                    if ((child.hasConnection()) &&
                        (!explored.contains(child)) &&
                        (child.getPathCostPrice()<p.getLimit())){
                            frontier.add(child);
                    }
                }
                Node child = new Node("goal", p.getGoalLa(), p.getGoalLo(), false,current);
                pathCost(current,child);
                if ((child.hasConnection()) &&
                    (!explored.contains(child)) &&
                    (child.getPathCostPrice()<p.getLimit())){
                        frontier.add(child);
                }
            // current is A airport
            } else if (!current.getParent().isAirport()){
                for (Airport airports : goalAirports){
                    Node child = new Node(airports.getName(),
                                          airports.getLa(),
                                          airports.getLo(),
                                          true,
                                          current);
                    pathCost(current,child);
                    if ((child.hasConnection()) &&
                        (!explored.contains(child)) &&
                        (child.getPathCostPrice()<p.getLimit())){
                            frontier.add(child);
                    }
                }
            // current is B airport
            } else if (current.getParent().isAirport()){
                Node child = new Node("goal", p.getGoalLa(), p.getGoalLo(), false,current);
                pathCost(current,child);
                if ((child.hasConnection()) &&
                    (!explored.contains(child)) &&
                    (child.getPathCostPrice()<p.getLimit())){
                        frontier.add(child);
                }
            }
        } while(!frontier.isEmpty());
        return;
    }



    public void getAirports(){
        FindAirports a = new FindAirports(p.getStartLa(),p.getStartLo(),num);
        startAirports = a.nearAirports();
        FindAirports b = new FindAirports(p.getGoalLa(),p.getGoalLo(),num);
        goalAirports = b.nearAirports();
    }

    public void pathCost(Node _nodeA, Node _nodeB){
        Node nodeA = _nodeA; //from
        Node nodeB = _nodeB; //to
        int time = 0;
        double price;
        price = 0;
        if ((nodeA.isAirport() == true) && (nodeB.isAirport() == true)){
            // Use flight finder to get time and price
            // put nodeB.setNoConnection() if no connection was found
            if (nodeA.getName().equals("MSY")){
                if (nodeB.getName().equals("ATL")){
                    time = 82;
                    price = 53.10;
                } else if (nodeB.getName().equals("MGE")){
                    time = 400;
                    price = 400;
                } else {
                    nodeB.setNoConnection();
                }
            } else if (nodeA.getName().equals("BTR")){
                if (nodeB.getName().equals("ATL")){
                    time = 300;
                    price = 350;
                } else {
                    nodeB.setNoConnection();
                }
            } else {
                nodeB.setNoConnection();
            }
            // Use road finder
        } else if((nodeA.isAirport() == false) && (nodeB.isAirport() == true)) {
            if (nodeB.getName().equals("BTR")){
                time = 13;
                price = 0.74;
            } else if (nodeB.getName().equals("ARA")){
                time = 83;
                price = 7.7;
            } else if (nodeB.getName().equals("LFT")){
                time = 63;
                price = 6.1;
            } else if (nodeB.getName().equals("MSY")){
                time = 61;
                price = 7.1;
            }
        } else if ((nodeA.isAirport() == true) && (nodeB.isAirport() == false)){
            if (nodeA.getName().equals("ATL")){
                time = 16;
                price = 1;
            } else if (nodeA.getName().equals("FTY")){
                time = 19;
                price = 1;
            } else if (nodeA.getName().equals("PDK")){
                time = 24;
                price = 1.3;
            } else if (nodeA.getName().equals("MGE")){
                time = 24;
                price = 1.7;
            }
        } else if ((nodeA.isAirport() == false) && (nodeB.isAirport() == false)){
            time = 445;
            price = 52.5;
        }
        nodeB.setPathCostTime(time + nodeA.getPathCostTime());
        nodeB.setPathCostPrice(price + nodeA.getPathCostPrice());
    }
}
