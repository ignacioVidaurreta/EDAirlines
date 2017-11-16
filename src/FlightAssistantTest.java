import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/*
public class FlightAssistantTest {
    private final String EXISTINGAIRPORTNAME = "BUE";
    FlightAssistant fa;
    int airportCounter;
    @Before
    public void Before(){
        fa = new FlightAssistant();
        airportCounter = 0;
        fa.addAirport(EXISTINGAIRPORTNAME, 100, 200);
        airportCounter++;
        fa.addAirport("RIO", 200, 300);
        airportCounter++;
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertFlightAirportDoesntExistTest(){
        fa.insertFlight("RAN", 200, "Lu",
                new Airport("BUE", 100, 200),new Airport("NaN",0,0),
                "12:45", "35m", 1200);
    }
    @Test
    public void insertFlightExistingAirportTest(){
        fa.insertFlight("RAN", 200, "Lu",
                new Airport("BUE", 100, 200),new Airport("RIO",200,300),
                "12:45", "35m", 1200);
        Assert.assertEquals(true, fa.getAirlinesFlights().get("RAN").containsKey(200));
    }
    @Test(expected = IllegalArgumentException.class)
    public void insertFlightMoreThan3LettersAirportTest(){
        fa.insertFlight("RAN", 200, "Lu",
                new Airport("BUENO", 100, 200),new Airport("RIO",200,300),
                "12:45", "35m", 1200);
    }
    @Test(expected = IllegalArgumentException.class)
    public void insertFlightWrongDepartureDayTest(){
        fa.insertFlight("RAN", 200, "Lupas",
                new Airport("BUE", 100, 200),new Airport("RIO",200,300),
                "12:45", "35m", 1200);
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
        fa.addAirport(ar.getName(),ar.getLongitude(), ar.getLatitude());
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
        fa.insertFlight("RAN", 200, "Lu",
                fa.getAirportMap().get(EXISTINGAIRPORTNAME),new Airport("RIO",200,300),
                "12:45", "35m", 1200);

        fa.deleteAirport(EXISTINGAIRPORTNAME);
        Assert.assertFalse(fa.getAirlinesFlights().get("RAN").containsKey(200));
    }
}
*/
