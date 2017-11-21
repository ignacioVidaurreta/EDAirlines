import java.util.List;

/**
 *  Generates a string with the format of a KML document
 */
public class KMLCreator {

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private static final String KML_OPENER = "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n<Folder>\n";
    private static final String KML_CLOSER = "</Folder> \n</kml>\n";

    public static String airportsToKML (List<MyFlightPackage> route) {
        StringBuffer str = new StringBuffer();
        MyFlightPackage lastFlight = route.get(route.size()-1); //Obtiene el último viaje;;
        str.append(HEADER);
        str.append(KML_OPENER);
        for (MyFlightPackage flightPackage : route) {
            if (flightPackage== null)continue;
            attachPlacemark(str, flightPackage.flight.getFrom(), buildDescription(flightPackage.flight));
        }
        attachPlacemark(str,lastFlight.flight.getTo(), "Destination");

        str.append(KML_CLOSER);

        System.out.println(str);
        return new String(str);
    }

    private static String buildDescription(Flight flight) {
        StringBuffer str = new StringBuffer();
        str.append("The flight " + flight.getFlightNum());
        str.append(" deploys with destinaton " + flight.getTo().getName());
        return str.toString();
    }

    private static void attachPlacemark (StringBuffer str, Airport airport, String description) {
        str.append("\t<Placemark>\n");
        str.append("\t\t<name>" + airport.getName() + "</name>\n");
        str.append("\t\t<description>" + description + "</description>\n"); // empty description
        attachPoint(str, airport.getLatitude(), airport.getLongitude());
        str.append("\t</Placemark>\n");
    }

    private static void attachPoint (StringBuffer str, double latitude, double longitude) {
        str.append("\t\t<Point>\n");
        str.append("\t\t\t<coordinates>" + longitude + "," + latitude + "</coordinates>\n");
        str.append("\t\t</Point>\n");
    }
}
