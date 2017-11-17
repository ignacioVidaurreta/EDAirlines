import java.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class FlightAssistant {
    private final String[] days = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
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
}