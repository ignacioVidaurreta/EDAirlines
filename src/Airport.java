import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

public class Airport {
    private String name, location;
    private double longitude, latitude;
    private List<Flight> neighbors;
    private boolean visited;
    private int index = -1;
    private int lowLink = -1;


    /**
     * Creates an airport. Two aiports are equals if they hace the same name
     * @param name
     * @param longitude
     * @param latitude
     */
    Airport(String name, String location, double longitude, double latitude){
        this.name = name;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.neighbors = new ArrayList<>();
        visited = false;
    }

    Airport(){
        this.visited=false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if (!(o instanceof Airport)) return false;
        Airport other = (Airport)o;
        return name.equals(other.getName());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(name);
    }

    public class PQAirport implements Comparable<PQAirport> {
        Airport a;
        Integer arrivalDay;
        double d;

        public PQAirport(Airport a, double d, Integer arrivalDay) {
            this.a = a;
            this.d = d;
            this.arrivalDay = arrivalDay;
        }

        public int compareTo(PQAirport other) {
            return Double.valueOf(d).compareTo(other.d);
        }
    }

    // Los pesos deben ser positivos
    public double minDistance(String from, String to, String fmt, String departureDays){
        // Airport f = airports.get(from);
        // Airport t = airports.get(to);
        Airport f = null;
        Airport t = null;
        if( f == null || t == null)
            return -1;
        boolean thereIsPossilbeFlight = false;
        for (Flight flight : f.neighbors) {
            String [] s = departureDays.split("-");
            for (String aux : s) {
                if (flight.getWeekDays().containsKey(aux)) {
                    thereIsPossilbeFlight = true;
                    break;
                }
            }
            if(thereIsPossilbeFlight)
                break;
        }
        if (!thereIsPossilbeFlight)
            return -1;
        PriorityQueue<PQAirport> pq = new PriorityQueue();
        pq.offer(new PQAirport(f, 0, null));
        double prevAux = 0;
        while(!pq.isEmpty()){
            PQAirport aux = pq.poll();
            if(aux.a == t)
                return aux.d;
            if(!aux.a.visited) {
                aux.a.visited = true;
                for (Flight flght : aux.a.neighbors) {
                    if(!flght.getTo().visited) {
                        if(fmt.equals("pr")) {
                            pq.offer(new PQAirport(flght.getTo(), aux.d + flght.getPrice(), null));
                        } else if(fmt.equals("ft")) {
                            pq.offer(new PQAirport(flght.getTo(), aux.d + flght.getDurationInDouble(), null));
                        } else {

                        }
                    }
                }
            }
        }
        return -1;
    }


    public double getTotalTime(double arrivalTime, Integer day, double departureTime, Integer departureDay) {
        int addDay = 0;
        double arrivalAux = arrivalTime;
        while(arrivalAux > 1440) {
            addDay += 1;
            arrivalAux -= 1440;
        }
        int changesWeek = departureDay < day + addDay ? 1 : 0;
        double amountOfdays = departureDay.doubleValue() - day.doubleValue() + 7 * changesWeek;
        double fulltime = (departureTime + amountOfdays * 1440) - arrivalAux;
        if (fulltime < 0)
            return fulltime + 7 * 1440;
        return fulltime;
    }


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLowLink() {
        return lowLink;
    }

    public void setLowLink(int lowLink) {
        this.lowLink = lowLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



    public List<Flight> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Flight> neighbors) {
        this.neighbors = neighbors;
    }
}
