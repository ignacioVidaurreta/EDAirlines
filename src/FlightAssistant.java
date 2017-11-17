import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

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