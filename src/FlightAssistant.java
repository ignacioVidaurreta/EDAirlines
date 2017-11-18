import java.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages Airports and flights
 */
public class FlightAssistant {
    String outputFormat = "text";
    String outputType = "stdout";
    private HashMap<String, Airport> airportMap;
    private List<Airport> airportList;
    /*
     *  La idea con airlinesFlights es encontrar la aerolinea con la key
     *  y una vez en la aerolínea encuentro el flight con el flightNumber
     */

    private HashMap<String, HashMap<Integer, Flight>> airlinesFlights;

    FlightAssistant(){
        airportMap= new HashMap<>();
        airportList = new ArrayList<>();
        airlinesFlights = new HashMap<>();
    }

    public boolean addAirport(Airport air){
        if (!air.getName().matches("[A-Za-z]{3}")){
            return false;
        }
        airportMap.put(air.getName(), air);
        airportList.add(air);
        return true;
    }


    public boolean deleteAirport(String name){
        Airport a = airportMap.get(name);
        if (a == null) return false;
        for( Flight f : a.getNeighbors()){
            if (f.getFrom().equals(a)) {
                airlinesFlights.get(f.getAirline()).remove(f.getFlightNum());
            }
        }
        airportMap.remove(name);
        airportList.remove(a);
        return true;
    }

    public boolean insertFlight(Flight flight) {
        if (!airportMap.containsKey(flight.getFrom().getName()) || !airportMap.containsKey(flight.getTo().getName())) {
            return false;
        }

        flight.getFrom().getNeighbors().add(flight);

        airlinesFlights.putIfAbsent(flight.getAirline(), new HashMap<>());

        airlinesFlights.get(flight.getAirline()).put(flight.getFlightNum(), flight);
        return true;
    }

    public HashMap<String, Airport> getAirportMap(){
        return airportMap;
    }
    public void setAirportMap(HashMap<String, Airport> airportMap){this.airportMap = airportMap;}

    public HashMap<String, HashMap<Integer, Flight>> getAirlinesFlights(){
        return airlinesFlights;
    }
    public void setAirlinesFlights(HashMap<String, HashMap<Integer, Flight>> newFlights){
        this.airlinesFlights = newFlights;
    }

    //mediante el algoritmo de componentes fuertemente conexas de Tarjan determina si el
    //grafo es fuertemente conexo.
    public boolean isStronglyConnected(){
        return  stronglyConnectedComponents().size() == 1;
    }

    //las dos siguientes funciones utilizan el algoritmo de Tarjan para separar el grafo en componentes
    //fuertemente conexas. Pseudocódigo para el algoritmo tomado de
    //https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    public List<List<String>> stronglyConnectedComponents(){

        clearMarks();
        List<List<String>> result = new ArrayList<>();
        Deque<Airport> stack = new LinkedList<>();
        for(Airport n : airportList){
            if(n.getIndex() == -1) {
                strongConnect(n,result,stack,0);
            }
        }
        return result;
    }

    private void strongConnect(Airport n, List<List<String>> result, Deque<Airport> stack, int index){

        n.setIndex(index);
        n.setLowLink(index);
        stack.push(n);
        n.setVisited(true);

        for(Flight e : n.getNeighbors()){

            if(e.getTo().getIndex() == -1) {
                index += 1;
                strongConnect(e.getTo(), result, stack, index + 1);
                n.setLowLink(Math.min(n.getLowLink(), e.getTo().getLowLink()));
            }
            else if(e.getTo().isVisited())
                n.setLowLink(Math.min(n.getLowLink(), e.getTo().getIndex()));
        }

        Airport aux;
        List<String> l = new LinkedList<>();
        if(n.getLowLink() == n.getIndex()){
            do {
                aux = stack.pop();
                l.add(aux.getName());
                aux.setVisited(false);
            }
            while(!aux.equals(n));
        }
        if(l.size() != 0)
            result.add(l);
    }

    private void clearMarks(){
        for(Airport a: airportList){
            a.setVisited(false);
            a.setIndex(-1);
            a.setLowLink(-1);
        }
    }

    public List<Airport> getAirportList(){
        return airportList;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public List<Flight> worldTour(Airport startAirport, String condition){
        if(condition.equals("tt"))  return null;
        List<Airport>   notVisitedAirports = new LinkedList<>(airportList);
        notVisitedAirports.remove(startAirport);
        List<Flight>    worldTourList = new LinkedList<>();
        Airport currentAirport = startAirport;
        Flight bestFlight;
        List<Flight>    currentDijkstra;
        double currentDijkstraCost;
        List<Flight> bestDijkstra = new LinkedList<>();
        double bestDijkstraCost = 0;
        boolean first;

        while(notVisitedAirports.size() == 0){
            first = true;
            bestFlight = selectMinFlightByCondition(currentAirport.getNeighbors(),condition,notVisitedAirports);
            //hace un dijkstra para cada aeropuerto no visitado y agarrra el de menor peso
            if(bestFlight == null){
                for(Airport a: notVisitedAirports){
                    currentDijkstra = new LinkedList<>();
                    currentDijkstraCost = dijkstra(currentAirport,a,currentDijkstra,condition);
                    if(first || currentDijkstraCost < bestDijkstraCost){
                        bestDijkstra = currentDijkstra;
                        bestDijkstraCost = currentDijkstraCost;
                    }
                    first = false;
                }
                for(Flight f: bestDijkstra){
                    currentAirport = f.getTo();
                    notVisitedAirports.remove(currentAirport);
                    worldTourList.add(f);
                }
                currentAirport = worldTourList.get(worldTourList.size() - 1).getTo();
            }else{
               currentAirport = bestFlight.getTo();
               worldTourList.add(bestFlight);
               notVisitedAirports.remove(currentAirport);
            }

        }
        currentDijkstra = new ArrayList<>();
        dijkstra(currentAirport,startAirport,currentDijkstra,condition);
        worldTourList.addAll(currentDijkstra);
        return worldTourList;
    }

    private class PQA implements Comparable<PQA>{
        Airport airport;
        Double cost;
        Flight flight;
        PQA prev;
        public PQA(Airport airport,Flight flight,Double cost,PQA prev){
            this.airport = airport;
            this.cost = cost;
            this.flight = flight;
            this.prev = prev;
        }
        public int compareTo(PQA other){    return cost.compareTo(other.cost);}
    }

    private double dijkstra(Airport a,Airport b,List<Flight> result,String condition){
        clearAirportVisited();
        PriorityQueue<PQA> pq = new PriorityQueue<>();
        pq.offer(new PQA(a,null,0.0,null));
        PQA aux = null;
        double cost = 0;
        while(!pq.isEmpty()){
            aux = pq.poll();
            if(!aux.airport.isVisited()){
                aux.airport.setVisited(true);
                if(aux.airport.equals(b)){
                 cost = aux.cost;
                 break;
                }
                for(Flight f:aux.airport.getNeighbors()){
                    if(!f.getTo().isVisited()){
                        if(condition.equals("pr"))  pq.offer(new PQA(f.getTo(),f,aux.cost + f.getPrice(),aux));
                        else if(condition.equals("ft")) pq.offer(new PQA(f.getTo(),f,aux.cost + f.getDurationInDouble(),aux));
                    }
                }
            }
        }
        while(aux.prev != null){
         result.add(0,aux.flight);
         aux = aux.prev;
        }
        return cost;
    }

    //FUNCION AUXILIAR DEL WORLDTOUR
    private Flight  selectMinFlightByCondition(List<Flight> flightList,String condition,List<Airport> notVisitedAirports){
        Flight minCostFlight = null;
        for(Flight f: flightList){
            if(notVisitedAirports.contains(f.getTo())){
                if(minCostFlight == null)   minCostFlight = f;
                else if(condition.equals("pr") && f.getPrice() < minCostFlight.getPrice()) minCostFlight = f;
                else if(condition.equals("ft") && f.getDurationInDouble() < minCostFlight.getDurationInDouble()) minCostFlight = f;
            }
        }
        return minCostFlight;
    }

    private void clearAirportVisited(){
        for(Airport a:airportList)  a.setVisited(false);
    }
}