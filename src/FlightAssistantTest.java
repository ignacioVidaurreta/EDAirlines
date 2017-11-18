import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

public class FlightAssistantTest {
    private final String EXISTINGAIRPORTNAME = "BUE";
    FlightAssistant fa;
    int airportCounter;

    @Before
    public void Before(){
        fa = new FlightAssistant();
        airportCounter = 0;
        fa.addAirport(new Airport(EXISTINGAIRPORTNAME, 100, 200));
        airportCounter++;
        fa.addAirport(new Airport("RIO", 200, 300));
        airportCounter++;
    }

    @Test
    public void insertFlightAirportDoesntExistTest(){
        Assert.assertEquals(false, fa.insertFlight(new Flight("RAN", 200, "Lu",
                new Airport("BUE", 100, 200),new Airport("NaN",0,0),
                "12:45", "35m", 1200)));
    }
    @Test
    public void insertFlightExistingAirportTest(){
        fa.insertFlight(new Flight("RAN", 200, "Lu",
                new Airport("BUE", 100, 200),new Airport("RIO",200,300),
                "12:45", "35m", 1200));
        Assert.assertEquals(true, fa.getAirlinesFlights().get("RAN").containsKey(200));
    }
    @Test
    public void insertFlightMoreThan3LettersAirportTest(){
        Assert.assertEquals(false, fa.insertFlight(new Flight("RAN", 200, "Lu",
                new Airport("BUENO", 100, 200),new Airport("RIO",200,300),
                "12:45", "35m", 1200)));
    }
    @Test(expected = IllegalArgumentException.class)
    public void insertFlightWrongDepartureDayTest(){
        fa.insertFlight(new Flight("RAN", 200, "Lupas",
                new Airport("BUE", 100, 200),new Airport("RIO",200,300),
                "12:45", "35m", 1200));
    }

    @Test
    public void addAirportObjectArgumentTest(){
        Airport ar = new Airport("NZN", 200, 500);
        fa.addAirport(ar);
        Assert.assertEquals(airportCounter+1,fa.getAirportList().size());
    }

    @Test
    public void addAirportFieldsArgumentTest(){
        Airport ar = new Airport("NZN", 200, 500);
        fa.addAirport(ar);
        Assert.assertEquals(airportCounter+1,fa.getAirportList().size());
    }

    @Test
    public void deleteAirportRemovesExistingAirportFromListTest(){
        fa.deleteAirport(EXISTINGAIRPORTNAME);
        Assert.assertEquals(airportCounter-1, fa.getAirportList().size());
    }
    @Test
    public void deleteAirportRemovesExistingAirportFromMapTest(){
        fa.deleteAirport(EXISTINGAIRPORTNAME);
        Assert.assertFalse(fa.getAirportMap().containsKey(EXISTINGAIRPORTNAME));
    }

    @Test
    public void deleteAirportRemovesFlightThatHasRemovedAirportAsOriginTest(){
        fa.insertFlight(new Flight("RAN", 200, "Lu",
                fa.getAirportMap().get(EXISTINGAIRPORTNAME),new Airport("RIO",200,300),
                "12:45", "35m", 1200));

        fa.deleteAirport(EXISTINGAIRPORTNAME);
        Assert.assertFalse(fa.getAirlinesFlights().get("RAN").containsKey(200));
    }

    @Test
    public void stronglyConnectedComponentsTest(){
        Assert.assertEquals(2,fa.stronglyConnectedComponents().size());
        fa.insertFlight(new Flight("RAN", 200, "Lu", fa.getAirportMap().get("BUE"), fa.getAirportMap().get("RIO"), "12:45", "35m", 1200));
        fa.insertFlight(new Flight("RAN", 200, "Lu", fa.getAirportMap().get("RIO"), fa.getAirportMap().get("BUE"), "12:45", "35m", 1200));
        Assert.assertEquals(1, fa.stronglyConnectedComponents().size());
        Assert.assertEquals(true, fa.isStronglyConnected());
    }

}

