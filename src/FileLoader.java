
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class FileLoader {

    public ArrayList<Airport> loadAirportsFromFile(String fileName){
        ArrayList<Airport> airports = new ArrayList<>();

        String inputLn;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(fileName));
            while ((inputLn = br.readLine()) != null){
                airports.add(parseAirport(inputLn));
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("No airport file with that name found :(");
        }
        return airports;
    }

    public Airport parseAirport(String input){
        Airport airport = new Airport();

        String[] elements = input.split("#");
        if (elements.length < 3)
            return null;
        airport.setName(elements[0]);
        airport.setLatitude(Float.parseFloat(elements[1]));
        airport.setLongitude(Float.parseFloat(elements[2]));

        return airport;
    }

    public ArrayList<Flight> loadFlightsFromFile(String fileName){
        ArrayList<Flight> flights = new ArrayList<>();

        String inputLn;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(fileName));
            while ((inputLn = br.readLine()) != null){
                flights.add(parseFlight(inputLn));
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("No airport file with that name found :(");
        }
        return flights;
    }

    public Flight parseFlight(String input){
        Flight flight = new Flight();

        String[] elements = input.split("#");
        if (elements.length < 3)
            return null;
        flight.setAirline(elements[0]);
        flight.setFlightNum(Integer.parseInt(elements[1]));
    //    flight.setWeekDays(elements[2]);
        flight.setOrigin(elements[3]);
        flight.setDestination(elements[4]);
        flight.setDepartureTime(elements[5]);
        flight.setDuration(elements[6]);
        flight.setPrice(Float.parseFloat(elements[7]));
        return flight;
    }
}

