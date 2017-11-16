import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
/*
public class FlightAssistant {
    private final String[] days = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
    private HashMap<String, Airport> airportMap;
    private List<Airport> airportList;
    /*
     *  La idea con airlinesFlights es encontrar la aerolinea con la key
     *  y una vez en la aerolínea encuentro el flight con el flightNumber
     */
/*
    private HashMap<String, HashMap<Integer, Flight>> airlinesFlights;

    FlightAssistant(){
        airportMap= new HashMap<>();
        airportList = new ArrayList<>();
        airlinesFlights = new HashMap<>();
    }

    public void addAirport(String name, double longitud, double latitud){
        if (!name.matches("[A-Za-z]{3}")){
            throw new IllegalAirportNameException("Airport name must be 3 characters long");
        }
        Airport airport = new Airport(name, longitud, latitud);
        airportMap.put(name, airport);
        airportList.add(airport);
    }
    public void addAirport(Airport air){
        this.addAirport(air.getName(), air.getLongitude(), air.getLatitude());
    }


    public void deleteAirport(String name){
        Airport a = airportMap.get(name);
        if (a == null) throw new NoSuchElementException(); // TODO throw exception or do nothing??{
        for( Flight f : a.getNeighbors()){
            if (f.getFrom().equals(a)) {
                    airlinesFlights.get(f.getAirline()).remove(f.getFlightNum());
            }
            }
        airportMap.remove(name);
        airportList.remove(a);

    }
    /*
    *   Dejo el if de correctArguments para que este el método pero esto lo vamos a validar en el front
     */
/*
    public void insertFlight(String airline, int flightNum, String departureDay, Airport from,
                             Airport to, String departureTime, String duration, double price){
        if (!correctArguments(airline, flightNum, departureDay, from, to, departureTime, duration, price)){
            throw new IllegalArgumentException();
        }

        Flight fl = new Flight(airline,flightNum,departureDay,from,to,departureTime,duration,price);
        from.getNeighbors().add(fl);
        //Al to no lo agrego porque es dirigido, o no?
        if (airlinesFlights.get(airline)== null){
            airlinesFlights.put(airline,new HashMap<>());
        }
        airlinesFlights.get(airline).put(flightNum, fl);
    }

    private boolean correctArguments(String airline, int flightNum, String departureDay,
                                     Airport from, Airport to, String departureTime,
                                     String duration, Double price) {
        if (!airline.matches("[A-Za-z]{3}")) return false;
        if (flightNum<0) return false;
        if (!departureDay.matches("Lu|Ma|Mi|Ju|Vi|Sa|Do|-")) return false; //TODO ver que no haya repetidos
        if (!airportMap.containsKey(from.getName()) || !airportMap.containsKey(to.getName())){
            return false;
        }
        if (!departureTime.matches("[01][0-9]:[0-5][0-9]|2[0-3]:[0-5][0-9]")) return false;
        if (!duration.matches("[0-9][0-9]h[0-5][0-9]m|[0-5][0-9]m")) return false;
        if (price<0) return false;
        String[] split =price.toString().split(".");
        if(split.length>1){
            if(split[1].length()>2) return false;
        }

        return true;


    }

    public HashMap<String, Airport> getAirportMap(){
        return airportMap;
    }

    public HashMap<String, HashMap<Integer, Flight>> getAirlinesFlights(){
        return airlinesFlights;
    }
    public List<Airport> getAirportList(){
        return airportList;
    }
}
*/
