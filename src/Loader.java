
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;


public class Loader {
    static int outputNum = 0;

    public static ArrayList<Airport> loadAirportsFromFile(String fileName){
        ArrayList<Airport> airports = new ArrayList<>();
        Airport current;

        String inputLn;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(fileName));
            while ((inputLn = br.readLine()) != null){
                if ((current = parseAirport(inputLn)) != null)
                    airports.add(parseAirport(inputLn));
                else
                    return null;
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("No airport file with that name found :(");
        }
        return airports;
    }

    public static Airport parseAirport(String input){
        Airport airport = new Airport();

        String[] elements = input.split("#");
        if (elements.length < 3)
            return null;
        airport.setName(elements[0]);
        airport.setLatitude(Float.parseFloat(elements[1]));
        airport.setLongitude(Float.parseFloat(elements[2]));

        return airport;
    }

    public static ArrayList<Flight> loadFlightsFromFile(String fileName, FlightAssistant map){
        ArrayList<Flight> flights = new ArrayList<>();
        Flight current;

        String inputLn;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(fileName));
            while ((inputLn = br.readLine()) != null){
                if ((current = parseFlight(inputLn, map)) != null)
                    flights.add(current);
                else
                    return null;
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("No flight file with that name found :(");
        }
        return flights;
    }

    public static Flight parseFlight(String input, FlightAssistant map){
        Flight flight = new Flight();

        String[] elements = input.split("#");
        if (elements.length < 3)
            return null;
        flight.setAirline(elements[0]);
        flight.setFlightNum(Integer.parseInt(elements[1]));
        flight.parseAndSetWeekDays(elements[2]);
        flight.setFrom(map.getAirportMap().get(elements[3]));
        flight.setTo(map.getAirportMap().get(elements[4]));
        flight.setDepartureTime(elements[5]);
        flight.setDuration(elements[6]);
        flight.setPrice(Float.parseFloat(elements[7]));
        return flight;
    }

    public static void saveRouteToFile(LinkedList<Flight> route, String outputType, String outputFormat){
        FileWriter fileWriter;
        try{
            fileWriter = new FileWriter(new File(outputType));
            if (outputFormat.equals("text")){
                fileWriter.write("PRICE");
                fileWriter.write("FLIGHTTIME");
                fileWriter.write("TOTALTIME");
                for (Flight flight:route){
                    fileWriter.write(flight.getFrom().getName() + "#");
                    fileWriter.write(flight.getAirline() + "#");
                    fileWriter.write(flight.getFlightNum() + "#");
                    fileWriter.write(flight.getWeekDays()+ "#");
                    fileWriter.write(flight.getTo().getName() + "\n");
                }
            } else {
                fileWriter.write(KMLCreator.airportsToKML(route));
            }
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

