import java.util.*;

public class Airport {
    private String name;
    private double longitude, latitude;
    private List<Flight> neighbors = new ArrayList<>();
    private boolean visited;
    private int index = -1;
    private int lowLink = -1;


    /**
     * Creates an airport. Two aiports are equals if they hace the same name
     * @param name
     * @param longitude
     * @param latitude
     */
    Airport(String name, double longitude, double latitude){
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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
        Integer dptDay;
        double d;
        LinkedList<Flight> route;

        public PQAirport(Airport a, double d, LinkedList<Flight> route, Flight f) {
            this.a = a;
            this.d = d;
            this.route = new LinkedList<>();
            if (route != null)
                for(Flight aux : route)
                    this.route.add(aux);
            this.route.add(f);
        }

        public PQAirport(Airport a, double d, Integer dptDay, LinkedList<Flight> route, Flight f) {
            this.a = a;
            this.d = d;
            this.dptDay = dptDay;
            this.route = new LinkedList<>();
            if (route != null)
                for(Flight aux : route)
                    this.route.add(aux);
            this.route.add(f);
        }

        public int compareTo(PQAirport other) {
            return Double.valueOf(d).compareTo(other.d);
        }
    }

    // Los pesos deben ser positivos
    public LinkedList<Flight> minDistance(FlightAssistant map, Airport from, Airport to, String fmt, String departureDays){
        Airport f = from;
        Airport t = to;
        if( f == null || t == null || !departureDays.matches("(Lu|Ma|Mi|Ju|Vi|Sa|Do|-)+"))
            return null;
        String [] s = departureDays.split("-");
        LinkedList<Flight> bestRoute = null;
        double bestCost = 0;
        for (String posDay: s) {
            boolean firstCycle = true;
            PriorityQueue<PQAirport> pq = new PriorityQueue();
            pq.offer(new PQAirport(f, 0, null, null, null));
            while(!pq.isEmpty()){
                PQAirport aux = pq.poll();
                if(aux.a == t) {
                    if(bestCost == 0 || aux.d <= bestCost) {
                        bestRoute = aux.route;
                        bestCost = aux.d;
                    }
                }
                if(!aux.a.visited) {
                    aux.a.visited = true;
                    for (Flight flght : aux.a.neighbors) {
                        if(firstCycle && flght.getWeekDays().containsKey(posDay)){
                            firstCycle = false;
                            if (fmt.equals("pr")) {
                                pq.offer(new PQAirport(flght.getTo(), aux.d + flght.getPrice(), null, flght));
                            } else if (fmt.equals("ft")) {
                                pq.offer(new PQAirport(flght.getTo(), aux.d + flght.getDurationInDouble(), null, flght));
                            } else {
                                double arrival = getArrival(aux.route.getLast());
                                Integer nextDptDay = getBestDay(flght.getWeekDays(), flght.getDepartureInDouble(), aux.dptDay, arrival);
                                if (nextDptDay > 0) {
                                    double totalDuration = getTotalTime(arrival, aux.dptDay, flght.getDepartureInDouble(), nextDptDay);
                                    pq.offer(new PQAirport(flght.getTo(), aux.d + totalDuration, nextDptDay, aux.route, flght));
                                }
                            }
                        } else {
                            if (!flght.getTo().visited) {
                                if (fmt.equals("pr")) {
                                    pq.offer(new PQAirport(flght.getTo(), aux.d + flght.getPrice(), aux.route, flght));
                                } else if (fmt.equals("ft")) {
                                    pq.offer(new PQAirport(flght.getTo(), aux.d + flght.getDurationInDouble(), aux.route, flght));
                                } else {
                                    double arrival = getArrival(aux.route.getLast());
                                    Integer nextDptDay = getBestDay(flght.getWeekDays(), flght.getDepartureInDouble(), aux.dptDay, arrival);
                                    if (nextDptDay > 0) {
                                        double totalDuration = getTotalTime(arrival, aux.dptDay, flght.getDepartureInDouble(), nextDptDay);
                                        pq.offer(new PQAirport(flght.getTo(), aux.d + totalDuration, nextDptDay, aux.route, flght));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Airport airport:map.getAirportList())
                airport.setVisited(false);
        }
        if(bestRoute != null) {
            return bestRoute;
        }
        return null;
    }


    public double getArrival(Flight f) {
        double aux = f.getDepartureInDouble() + f.getDurationInDouble();
        if(aux >= 1440)
            aux -= 1440;
        return aux;
    }

    public Integer getBestDay(HashMap<String, Integer> hm, double dpt, Integer d, double arrival) {
        Integer best = 0;
        boolean firstCycle = true;
        for(Integer availableDay: hm.values()) {
            Integer aux = availableDay - d;
            if(aux <= 0){
                if(aux == 0 && dpt > arrival)
                    return availableDay;
                aux += 7;
            }
            if(firstCycle) {
                best = aux;
                firstCycle = false;
            } else {
                if(aux > best)
                    return best;
                best = aux;
            }
        }
        return best;
    }

    public double getTotalTime(double arrivalTime, Integer day, double departureTime, Integer departureDay) {
        int addDay = 0;
        double arrivalAux = arrivalTime;
        while(arrivalAux >= 1440) {
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

    public void printRoute(LinkedList<Flight> l) {
        for (Flight f: l) {
            System.out.println("Origin: " + f.getFrom().getName() + ", To: " + f.getTo().getName() + ", Airline: " + f.getAirline());
        }
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
