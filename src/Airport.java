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

    /**
     * Wrapper Class of Airport used to run Dijkstra's algorithm
     */
    public class PQAirport implements Comparable<PQAirport> {
        Airport a;
        Integer dptDay;
        double d;
        double totalTime;
        LinkedList<MyFlightPackage> route;

        public PQAirport(Airport a, double d, Integer dptDay, LinkedList<MyFlightPackage> route, MyFlightPackage f, double totalTime) {
            this.a = a;
            this.d = d;
            this.dptDay = dptDay;
            this.route = new LinkedList<>();
            if (route != null)
                for(MyFlightPackage aux : route)
                    this.route.add(aux);
            this.route.add(f);
            this.totalTime = totalTime;
        }

        public int compareTo(PQAirport other) {
            return Double.valueOf(d).compareTo(other.d);
        }
    }

    /**
     * Dijkstra's Algorithm
     * @param map
     * @param from
     * @param to
     * @param fmt
     * @param departureDays
     * @return
     */
    // Los pesos deben ser positivos
    public MyRoutePackage minDistance(FlightAssistant map, Airport from, Airport to, String fmt, String departureDays) {
        for (Airport airport : map.getAirportList())
            airport.setVisited(false);

        Airport f = from;
        Airport t = to;
        if (f == null || t == null || !departureDays.matches("(Lu|Ma|Mi|Ju|Vi|Sa|Do|-)+"))
            return null;
        //Divide las fechas dadas
        String[] s = departureDays.split("-");
        LinkedList<MyFlightPackage> bestRoute = null;
        double arrival;
        Integer nextDptDay, sumOneDay;
        double bestCost, bestTotalTime, auxData;
        bestCost = auxData = bestTotalTime = 0;
        MyFlightPackage mfp;
        //Hacemos un dijkstra por cada fecha
        for (String posDay : s) {
            boolean firstCycle = true;
            PriorityQueue<PQAirport> pq = new PriorityQueue();
            pq.offer(new PQAirport(f, 0, null, null, null, 0));
            while (!pq.isEmpty()) {
                PQAirport aux = pq.poll();
                if (aux.a == t) {
                    if (bestCost == 0 || aux.d <= bestCost) {
                        bestRoute = aux.route;
                        bestCost = aux.d;
                        bestTotalTime = aux.totalTime;
                    }
                }
                if (!aux.a.visited) {
                    aux.a.visited = true;
                    for (Flight flght : aux.a.neighbors) {
                        if (firstCycle) {
                            if (flght.getWeekDays().containsKey(posDay)) {
                                if (fmt.equals("pr")) {
                                    auxData = flght.getPrice();
                                } else {
                                    auxData = flght.getDurationInDouble();
                                }
                                Integer day = getDayInInteger(posDay);
                                mfp= new MyFlightPackage(day, flght);
                                pq.offer(new PQAirport(flght.getTo(), auxData, flght.getWeekDays().get(posDay), aux.route, mfp, flght.getDurationInDouble()));
                            }
                        } else {
                            if (!flght.getTo().visited) {
                                arrival = getArrival(aux.route.getLast().flight);
                                sumOneDay = checkTime(aux.route.getLast().flight);
                                nextDptDay = getBestDay(flght.getWeekDays(), flght.getDepartureInDouble(), aux.dptDay + sumOneDay, arrival);
                                double totalDuration = getTotalTime(arrival, aux.dptDay, flght.getDepartureInDouble(), nextDptDay, sumOneDay);
                                if (fmt.equals("pr")) {
                                    auxData = aux.d + flght.getPrice();
                                } else if (fmt.equals("ft")) {
                                    auxData = aux.d + flght.getDurationInDouble();
                                } else {
                                    auxData = totalDuration + aux.d;
                                }
                                mfp = new MyFlightPackage(nextDptDay, flght);
                                pq.offer(new PQAirport(flght.getTo(), auxData, nextDptDay, aux.route, mfp, aux.totalTime + totalDuration));
                            }
                        }
                    }
                    firstCycle = false;
                }
            }
            for (Airport airport : map.getAirportList())
                airport.setVisited(false);
        }
        if (bestRoute != null) {
            MyRoutePackage route = new MyRoutePackage(bestCost, bestTotalTime, bestRoute);
            return route;
        }
        return null;
    }


    public Integer checkTime(Flight f) {
        if(f.getDepartureInDouble() + f.getDurationInDouble() >= 1440)
            return 1;
        return 0;
    }

    public static Integer getDayInInteger(String d) {
        switch (d) {
            case "Lu":
                return 1;
            case "Ma":
                return 2;
            case "Mi":
                return 3;
            case "Ju":
                return 4;
            case "Vi":
                return 5;
            case "Sa":
                return 6;
            case "Do":
                return 7;
        }
        return 0;
    }

    public static String getDayFromIngeter(int i){
        switch (i) {
            case 1:
                return "Lu";
            case 2:
                return "Ma";
            case 3:
                return "Mi";
            case 4:
                return "Ju";
            case 5:
                return "Vi";
            case 6:
                return "Sa";
            case 7:
                return "Do";
        }
        return null;
    }

    public double getArrival(Flight f) {
        double aux = f.getDepartureInDouble() + f.getDurationInDouble();
        if(aux >= 1440)
            aux -= 1440;
        return aux;
    }

    public Integer getBestDay(HashMap<String, Integer> hm, double dpt, Integer arrivalDay, double arrival) {
        Integer best, ret;
        best = ret = 0;
        boolean firstCycle = true;
        Integer aux;
        for(Integer availableDay: hm.values()) {
            aux = availableDay - arrivalDay;
            if(aux <= 0){
                if(aux == 0 && dpt >= arrival)
                    return availableDay;
                aux += 7;
            }
            if(!firstCycle) {
                if(aux > best)
                    return ret;
            }
            best = aux;
            ret = availableDay;
            firstCycle = false;
        }
        return ret;
    }

    public double getTotalTime(double arrivalTime, Integer day, double departureTime, Integer departureDay, Integer shouldAddDay) {
        int changesWeek = departureDay < day + shouldAddDay ? 1 : 0;
        double amountOfdays = departureDay.doubleValue() - (day.doubleValue() + shouldAddDay.doubleValue()) + 7 * changesWeek;
        double fulltime = (departureTime + amountOfdays * 1440) - arrivalTime;
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
