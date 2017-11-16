import java.util.HashMap;

public class Flight {
    private String airline;
    private int flightNum;
    private HashMap<String, Integer> weekDays;
    private Airport from, to;
    private String origin, destination;
    private String departureTime, duration;
    private double price;

    /**
     * Creates a flight from one airport to another
     * @param airline Name of the airline (max 3 chars)
     * @param flightNum integer that identifies the flight inside an airline
     * @param from from what airport
     * @param to   to what airport
     * @param departureTime
     * @param duration
     * @param price
     */
    public Flight(String airline, int flightNum, String departureDay, String ori, String destination,
                  Airport from, Airport to, String departureTime, String duration, double price) {
        this.airline = airline;
        this.flightNum = flightNum;
        this.origin = ori;
        this.destination = destination;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.duration = duration;
        this.price = price;

        String [] aux = departureDay.split("-");
        for(String s : aux){
            switch(s) {
                case "Lu":
                    this.weekDays.put(s,1);
                    break;
                case "Ma":
                    this.weekDays.put(s,2);
                    break;
                case "Mi":
                    this.weekDays.put(s,3);
                    break;
                case "Ju":
                    this.weekDays.put(s,4);
                    break;
                case "Vi":
                    this.weekDays.put(s,5);
                    break;
                case "Sa":
                    this.weekDays.put(s,6);
                    break;
                case "Do":
                    this.weekDays.put(s,7);
                    break;
            }
        }
    }
    public Flight(){
        //doNothing
    }

    //getters and setters
    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public int getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(int flightNum) {
        this.flightNum = flightNum;
    }

    public HashMap<String, Integer> getWeekDays() {return weekDays;}

    public void setWeekDays(HashMap<String, Integer> weekDays) {this.weekDays = weekDays;}


    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getDuration() {
        return duration;
    }

    public double getDurationInDouble() {
        char[] hours = duration.substring(0, 2).toCharArray();
        char[] minutes = duration.substring(3).toCharArray();
        return ((hours[0] - '0')* 60 + (hours[1] - '0') * 6 + (minutes[0] - '0')) * 10 + (minutes[1] - '0');
    }

    public double getDepartureInDouble() {
        char[] hours = duration.substring(0, 2).toCharArray();
        char[] minutes = duration.substring(3).toCharArray();
        return ((hours[0] - '0')* 60 + (hours[1] - '0') * 6 + (minutes[0] - '0')) * 10 + (minutes[1] - '0');
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
