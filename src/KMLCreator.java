import java.util.List;

/**
 *  Generates a string with the format of a KML document
 */
public class KMLCreator {

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private static final String KML_OPENER = "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n <Folder>\n";
    private static final String KML_CLOSER = "</Folder> \n</kml>\n";

    public static String airportsToKML (List<Flight> route) {
        StringBuffer str = new StringBuffer();
        Flight lastFlight = route.get(route.size()-1); //Obtiene el último viaje
        str.append(HEADER);
        str.append(KML_OPENER);

        for (Flight flight : route)
            attachPlacemark(str, flight.getFrom(), buildDescription(flight));
        attachPlacemark(str,lastFlight.getTo(), "Destination");

        str.append(KML_CLOSER);

        return new String(str);
    }

    private static String buildDescription(Flight flight) {
        StringBuffer str = new StringBuffer();
        str.append("The flight " + flight.getFlightNum());
        str.append(" deploys with destinaton" + flight.getDestination());
        return str.toString();
    }

    private static void attachPlacemark (StringBuffer str, Airport airport, String description) {
        str.append("<Placemark>\n");
        str.append("<name>" + airport.getName() + "</name>\n");
        str.append("<description>" + description + "</description>\n"); // empty description
        attachPoint(str, airport.getLatitude(), airport.getLongitude());
        str.append("</Placemark>\n");
    }

    private static void attachPoint (StringBuffer str, double latitude, double longitude) {
        str.append("<Point>\n");
        str.append("<coordinates>" + longitude + "," + latitude + "</coordinates>\n");
        str.append("</Point>\n");
    }
}
