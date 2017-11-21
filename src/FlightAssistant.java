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
        for(Flight f : a.getNeighbors()){
            if (f.getFrom().equals(a)) {
                airlinesFlights.get(f.getAirline()).remove(f.getFlightNum());
            }
        }
        airportMap.remove(name);
        airportList.remove(a);
        return true;
    }

    public boolean insertFlight(Flight flight) {
        try{
            if (!airportMap.containsKey(flight.getFrom().getName()) || !airportMap.containsKey(flight.getTo().getName())) {
                return false;
            }
        } catch (NullPointerException e){
            System.out.println("Some flights going to or from airports that are not on the map! (Not inserted)");
        }


        flight.getFrom().getNeighbors().add(flight);

        airlinesFlights.putIfAbsent(flight.getAirline(), new HashMap<>());

        airlinesFlights.get(flight.getAirline()).put(flight.getFlightNum(), flight);
        return true;
    }

    public void removeFlight(String airline, int flightNum){
        for (Airport port: airportList){
            for (Flight flight: port.getNeighbors())
                if (flight.equals(airlinesFlights.get(airline).get(flightNum))){
                    port.getNeighbors().remove(flight);
                    break;
                }
        }
        airlinesFlights.get(airline).remove(flightNum);
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

    public MyRoutePackage worldTour(Airport startAirport, String condition){
        clearAirportVisited();
        if(!isStronglyConnected())  return null;
        if(condition.equals("tt"))  return null;
        List<Airport>   notVisitedAirports = new LinkedList<>(airportList);
        notVisitedAirports.remove(startAirport);
        LinkedList<MyFlightPackage>    worldTourList = new LinkedList<>();
        Airport currentAirport = startAirport;
        Flight bestFlight;
        List<Flight>    currentDijkstra;
        double currentDijkstraCost;
        List<Flight> bestDijkstra = new LinkedList<>();
        double bestDijkstraCost = 0;
        boolean first;

        while(notVisitedAirports.size() != 0){
            first = true;
            bestFlight = selectMinFlightByCondition(currentAirport.getNeighbors(),condition,notVisitedAirports);
            //hace un dijkstra para cada aeropuerto no visitado y toma el de menor peso
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
                    Integer weekDay = getFirstWeekDay(f);
                    worldTourList.add(new MyFlightPackage(weekDay,f));
                }
                currentAirport = worldTourList.get(worldTourList.size() - 1).flight.getTo();
            }else{
               currentAirport = bestFlight.getTo();
                Integer weekDay = getFirstWeekDay(bestFlight);
                worldTourList.add(new MyFlightPackage(weekDay,bestFlight));
               notVisitedAirports.remove(currentAirport);
            }

        }
        currentDijkstra = new ArrayList<>();
        dijkstra(currentAirport,startAirport,currentDijkstra,condition);
        List<MyFlightPackage> finalStretch = new LinkedList<>();
        for(Flight f: currentDijkstra)
            finalStretch.add(new MyFlightPackage(getFirstWeekDay(f),f));

        worldTourList.addAll(finalStretch);
        return new MyRoutePackage(getPrice(worldTourList), getTime(worldTourList), worldTourList);
    }

    private class PQA implements Comparable<PQA>{
        Airport airport;
        Double cost;
        Flight flight;
        PQA prev;
        PQA(Airport airport,Flight flight,Double cost,PQA prev){
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

    private double getPrice(List<MyFlightPackage> route){
        double price = 0;
        for(MyFlightPackage m: route)
            price += m.flight.getPrice();
        return price;
    }

    private double getTime(List<MyFlightPackage> route){
        double time = 0;
        double timeInADay = 24*60;
        Flight first = route.get(0).flight;
        Integer firstDay = route.get(0).day;
        time += first.getDurationInDouble();
        double lastArrivalTime = (first.getDepartureInDouble() + first.getDurationInDouble())%(timeInADay);
        for(int i = 1; i < route.size(); i++){
            time += getDayTime(lastArrivalTime, route.get(i).day, firstDay, first) + first.getDurationInDouble();
            first = route.get(i).flight;
            firstDay = firstDay + (lastArrivalTime + route.get(i).flight.getDurationInDouble()) > (timeInADay) ?
                    ((firstDay + 1) % 7) + 1 : firstDay;
            lastArrivalTime = (lastArrivalTime + route.get(i).flight.getDurationInDouble())%(timeInADay);
        }
        return time;
    }

    private double getDayTime(double lastArrivalTime, Integer departureDay, Integer startDay, Flight f){
        double time = 0;
        double timeInADay = 24*60;
        if(startDay == departureDay && lastArrivalTime < f.getDepartureInDouble()){
            return f.getDepartureInDouble() - lastArrivalTime;
        }
        while(startDay != departureDay -1 && !(startDay == 7 && departureDay == 1)){
            time += timeInADay;
            startDay++;
            if(startDay == 8) startDay = 1;
        }
        time += timeInADay - lastArrivalTime + f.getDepartureInDouble();
        return time;
    }

    private Integer getFirstWeekDay(Flight f){
        Integer weekDay = 0;
        for(String s: f.getWeekDays().keySet()) {
            weekDay = f.getWeekDays().get(s);
            return weekDay;
        }
        return weekDay;
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

    public static void main(String[] args){

        FlightAssistant fa = new FlightAssistant();
        fa.addAirport(new Airport("BUE", 100, 200));
        fa.addAirport(new Airport("RIO", 200, 300));
        fa.addAirport(new Airport("HKG",20,0));
        fa.addAirport(new Airport("MAD",30,0));
        fa.addAirport(new Airport("LON",40,0));
        fa.addAirport(new Airport("NYC",50,0));
        fa.addAirport(new Airport("BER",60,0));
        fa.addAirport(new Airport("VOL",70,0));
        fa.addAirport(new Airport("LIM",80,0));

        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("BUE"), fa.getAirportMap().get("RIO"),"12:00","12h00m",99));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("RIO"), fa.getAirportMap().get("HKG"),"12:00","12h00m",100));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("HKG"), fa.getAirportMap().get("BUE"),"12:00","12h00m",98));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("HKG"), fa.getAirportMap().get("MAD"),"12:00","12h00m",97));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("MAD"), fa.getAirportMap().get("LON"),"12:00","12h00m",55));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("LON"), fa.getAirportMap().get("NYC"),"12:00","12h00m",32));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("NYC"), fa.getAirportMap().get("BER"),"12:00","12h00m",6));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("BER"), fa.getAirportMap().get("VOL"),"12:00","12h00m",66));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("VOL"), fa.getAirportMap().get("MAD"),"12:00","12h00m",80));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("VOL"), fa.getAirportMap().get("LIM"),"12:00","12h00m",15));
        fa.insertFlight(new Flight("abc",1,"Lu",fa.getAirportMap().get("LIM"), fa.getAirportMap().get("BUE"),"12:00","12h00m",33));

        MyRoutePackage listita = fa.worldTour(fa.getAirportMap().get("BUE"),"pr");
        System.out.println(listita.totalTime);
        System.out.println(listita.best);
        for(MyFlightPackage m: listita.route) {
            System.out.println(m.flight.getTo().getName());
        }
    }
}