import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Bensas on 11/2/17.
 */
public class EDAirlines {

    public static void main(String[] args){
        FlightAssistant flightAssistant = new FlightAssistant();

        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("Please enter a command below (enter \"exit\" to end the program):");
        while (!(input = scanner.nextLine()).equals("exit")){
            try{
                processInput(input, flightAssistant);
                System.out.println("Please enter the next command below:");
            } catch (NumberFormatException e){
                System.out.println("There is a problem with the input format.");
            }
        }
    }

    public static void processInput(String input, FlightAssistant map) throws NumberFormatException{
        if (input.contains("insert airport")){
            String[] args = input.split("insert airport ")[1].split(" ");
            if (args.length < 3){
                System.out.println("Not enough arguments.");
                return;
            }
            if (map.addAirport(new Airport(args[0], Float.parseFloat(args[1]), Float.parseFloat(args[2]))))
                System.out.println("Airport inserted successfully!");
            else
                System.out.println("There was an error inserting the airport.");
        }
        else if (input.contains("delete airport")){
            String[] args = input.split("delete airport ");
            if (args.length < 2){
                System.out.println("Not enough arguments.");
                return;
            }
            if (map.deleteAirport(args[1]))
                System.out.println("Airport " + args[1] + " deleted successfully!");
            else
                System.out.println("There was an error deleting the airport from file.");
        }
        else if (input.contains("insert all airports")){
            if (input.split("insert all airports ")[1].split(" ")[1].equals("replace"))
                map = new FlightAssistant();
            ArrayList<Airport> airports = Loader.loadAirportsFromFile(input.split("insert all airports ")[1].split(" ")[0]);
            if (airports != null){
                for (Airport airport: airports)
                    map.addAirport(airport);
                System.out.println("Airports inserted successfully!");
            } else{
                System.out.println("There was an error inserting the airports from file.");
            }
        }
        else if (input.contains("delete all airport")){
            map.setAirportMap(new HashMap<>());
            System.out.println("Airports deleted successfully!");
        }

        else if (input.contains("insert flight")){
            String[] args = input.split("insert flight ")[1].split(" ");
            if (args.length < 8){
                System.out.println("Not enough arguments.");
                return;
            }
            if (map.insertFlight(new Flight(args[0], Integer.parseInt(args[1]), args[2], map.getAirportMap().get(args[3]),
                    map.getAirportMap().get(args[4]), args[5], args[6], Double.parseDouble(args[7]))))
                System.out.println("Flights inserted successfully!");
            else
                System.out.println("There was an error inserting the flight.");
        }
        else if (input.contains("delete flight")){
            try{
                map.getAirlinesFlights().get(input.split("delete flight ")[1].split(" ")[0]).remove(input.split("delete flight ")[1].split(" ")[1]);
                System.out.println("Flight deleted successfully!");
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("There was an error deleting the flights from file.");
            }
        }
        else if (input.contains("insert all flight")){
            if (input.split("insert all flight ")[1].split(" ")[1].equals("replace"))
                map.setAirlinesFlights(new HashMap<>());
            ArrayList<Flight> flights = Loader.loadFlightsFromFile(input.split("insert all flight ")[1].split(" ")[0], map);
            if (flights != null){
                for (Flight flight: flights)
                    map.insertFlight(flight);
                System.out.println("Flights inserted successfully!");
            } else {
                System.out.println("There was an error inserting the flights from file.");
            }
        }
        else if (input.contains("delete all flight")){
            map.setAirlinesFlights(new HashMap<>());
            System.out.println("Flights deleted successfully!");
        }
        else if (input.contains("findRoute")){
            String[] args = input.split("findRoute ")[1].split(" ");
            if (args.length < 4){
                System.out.println("Not enough arguments.");
                return;
            }
            LinkedList<Flight> route = map.getAirportMap().get(args[0]).minDistance(map, map.getAirportMap().get(args[0]),
                                                                                    map.getAirportMap().get(args[1]),
                                                                                    args[2], args[3]);
            if (route == null){
                System.out.println("No route found!");
                return;
            }
            if (map.getOutputType().equals("stdout")){
                System.out.println("PRICE");
                System.out.println("FLIGHTTIME");
                System.out.println("TOTALTIME");
                for (Flight flight:route){
                    System.out.print(flight.getFrom().getName() + "#");
                    System.out.print(flight.getAirline() + "#");
                    System.out.print(flight.getFlightNum() + "#");
                    System.out.print(flight.getWeekDays()+ "#");
                    System.out.print(flight.getTo().getName() + "\n");
                }
            } else{
                Loader.saveRouteToFile(route);
                System.out.println("Route saved to file!");
            }
        }
        else if (input.contains("worldTrip")){
            String[] args = input.split("worldTrip ")[1].split(" ");
            LinkedList<Flight> route = new LinkedList<>(); //TODO: GET THE WORLD TRIP ROUTE
            if (map.getOutputType().equals("stdout")){
                System.out.println("PRICE");
                System.out.print("FLIGHTTIME");
                System.out.print("TOTALTIME");
                for (Flight flight:route){
                    System.out.print(flight.getFrom().getName() + "#");
                    System.out.print(flight.getAirline() + "#");
                    System.out.print(flight.getFlightNum() + "#");
                    System.out.print(flight.getWeekDays().get(0) + "#");
                    System.out.print(flight.getTo().getName() + "\n");
                }
            } else{
                Loader.saveRouteToFile(route);
                System.out.println("Route saved to file!");
            }
        }
        else if (input.contains("outputFormat")){
            String[] args = input.split("outputFormat ")[1].split(" ");
            if (args[0].equals("text")){
                map.setOutputFormat("text");
            } else if (args[0].equals("KML")){
                map.setOutputFormat("KML");
            } else{
                System.out.println("Bad output format.");
                return;
            }
            if (args[1].equals("archivo")){
                map.setOutputType("archivo");
            } else if (args[1].equals("stdout")){
                map.setOutputType("stdout");
            } else {
                System.out.println("Bad output type.");
                return;
            }
        }
        else if (input.contains("help")){
            System.out.println("The command is invalid. Enter \"help\" for a list of commands.");
        }
        else {
            System.out.println("The command is invalid. Enter \"help\" for a list of commands.");
        }
    }

}
