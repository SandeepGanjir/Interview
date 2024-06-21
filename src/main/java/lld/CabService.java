package lld;

import java.util.*;

public class CabService {
}

/**
@startuml

class UUID {
{static} - uidCounter: int
{static} + getUniqueId(): String
}

abstract class User {
# id: String
# name: String
# contactNumber: long
# currentTripId: String
{abstract} + User(name: String, contactNumber: long, uidPrefix: String)
+ getId(): String
}

class Rider {
+ Rider(name: String, contactNumber: long)
+ createTripRequest(pickUpPoint: Address, dropOffPoint: Address): TripRequest
+ createTrip(pickUpPoint: Address, dropOffPoint: Address): Trip
+ completePayment(amount: double): boolean
}

class Driver {
# currentLocation: Address
+ Driver(name: String, contactNumber: long)
+ reserve(tripRequest: TripRequest): boolean
+ finishTrip(): Double
}

class RiderManager {
{static} - Singleton.instance: RiderManager
- riders: Map<String, Rider>
- RiderManager()
{static} + getInstance(): RiderManager
{static} + getRider(riderId: String): Rider
}

class DriverManager {
{static} - Singleton.instance: DriverManager
- drivers: Map<String, Driver>
- DriverManager()
{static} + getInstance(): DriverManager
{static} + getDriver(driverId: String): Driver
{static} + getNearbyDrivers(source: Address, travelTime: double): List<Driver>
}

class Address {
latitude: double
longitude: double
- pinCode: long
- address: String
}

class MapApi {
{static} + getTravelDistance(source: Address, destination: Address): double
{static} + getTravelTime(source: Address, destination: Address): double
}

class TripRequest {
+ id: String
+ riderId: String
+ source: Address
+ destination: Address
- fareEstimate: Fare
+ TripRequest(riderId: String, source: Address, destination: Address)
- _estimateFare(): Fare
+ getFareEstimate(): Fare
}

class Trip {
+ id: String
- riderId: String
- driverId: String
- source: Address
- destination: Address
- fare: Fare
+ Trip(tripRequest: TripRequest, driver: Driver)
- _finalFare(fare: Fare, driver: Driver): Fare
+ requestPayment(): Double
}

class TripManager {
{static} - Singleton.instance: TripManager
{static} - THRESHOLD_DRIVER_TRAVEL_TIME: double = 15.00
- trips: Map<String, Trip>
- TripManager()
{static} + getInstance(): TripManager
{static} + getTrip(tripId: String): Trip
{static} + createTrip(tripRequest: TripRequest): Trip
}

class Fare {
{static} - COST_PER_KM: double = 13.50
{static} - COST_PER_MIN: double = 0.75
+ travelDistance: double
+ travelTime: double
+ cost: double
- totalCost: double
+ Fare(travelDistance: double, travelTime: double)
- _computeCost(travelDistance: double, travelTime: double): double
- surgeMultiplier(): double
+ computeFinalCost(driverTravelDistance: double, driverTravelTime: double)
+ getCost(): double
+ getTotalCost(): double
}

User <|-- Rider
User <|-- Driver

@enduml
*/

class UUID {
    private static int uidCounter = 0;
    public static String getUniqueId() {
        return String.format("%015d", uidCounter++);
    }
}

abstract class User {
    protected final String id;
    protected final String name;
    protected final long contactNumber;
    protected String currentTripId;

    protected User(String name, long contactNumber, String uidPrefix) {
        this.id = uidPrefix + UUID.getUniqueId();
        this.name = name;
        this.contactNumber = contactNumber;
        this.currentTripId = null;
    }

    public String getId() {
        return id;
    }
}

class Rider extends User {

    public Rider(String name, long contactNumber) {
        super(name, contactNumber, "R");
    }

    public TripRequest createTripRequest(Address pickUpPoint, Address dropOffPoint) {
        return new TripRequest(id, pickUpPoint, dropOffPoint);
    }

    public Trip createTrip(Address pickUpPoint, Address dropOffPoint) {
        TripRequest tripRequest = createTripRequest(pickUpPoint, dropOffPoint);
        Trip trip = TripManager.createTrip(tripRequest);
        if (trip != null) super.currentTripId = trip.id;
        return trip;
    }

    public boolean completePayment(double amount) {
        System.out.println(name + " made payment of : " + amount);
        super.currentTripId = null;
        return true;
    }
}

class Driver extends User {

    Address currentLocation;

    public Driver(String name, long contactNumber) {
        super(name, contactNumber, "D");
        this.currentLocation = new Address();
    }

    public boolean reserve(TripRequest tripRequest) {
        if (currentTripId != null) {
            currentTripId = tripRequest.id;
            return true;
        }
        return false;
    }

    public Double finishTrip() {
        if (currentTripId != null) {
            Trip currentTrip = TripManager.getTrip(currentTripId);
            Double fare = currentTrip.requestPayment();
            if (fare != null) currentTripId = null;
            return fare;
        }
        return null;
    }
}

class RiderManager {
    private static class Singleton {
        private static final RiderManager instance = new RiderManager();
    }
    private final Map<String, Rider> riders = new HashMap<>();
    private RiderManager() {}
    public RiderManager getInstance() {
        return Singleton.instance;
    }
    public static Rider getRider(String riderId) {
        return Singleton.instance.riders.get(riderId);
    }
}

class DriverManager {
    private static class Singleton {
        private static final DriverManager instance = new DriverManager();
    }
    private final Map<String, Driver> drivers = new HashMap<>();
    private DriverManager() {}
    public DriverManager getInstance() {
        return Singleton.instance;
    }
    public static Driver getDriver(String driverId) {
        return Singleton.instance.drivers.get(driverId);
    }

    public static List<Driver> getNearbyDrivers(Address source, double travelTime) {
        Map<Double, Driver> map = new TreeMap<>();
        for (Driver driver: Singleton.instance.drivers.values()) {
            double timeRequired = MapApi.getTravelTime(driver.currentLocation, source);
            if (timeRequired < travelTime) {
                map.put(timeRequired, driver);
            }
        }
        return new ArrayList<>(map.values());
    }
}

class Address {
    double latitude;
    double longitude;
    private long pinCode;
    private String address;
}

class MapApi {
    public static double getTravelDistance(Address source, Address destination) {
        double lat1 = Math.toRadians(source.latitude);
        double lon1 = Math.toRadians(source.longitude);
        double lat2 = Math.toRadians(destination.latitude);
        double lon2 = Math.toRadians(destination.longitude);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    public static double getTravelTime(Address source, Address destination) {
        return getTravelDistance(source, destination) / 0.30;
    }
}

class TripRequest {
    final String id;
    final String riderId;
    final Address source;
    final Address destination;
    private final Fare fareEstimate;

    public TripRequest (String riderId, Address source, Address destination) {
        this.id = "T" + UUID.getUniqueId();
        this.riderId = riderId;
        this.source = source;
        this.destination = destination;
        this.fareEstimate = _estimateFare();
    }

    private Fare _estimateFare() {
        double travelDistance = MapApi.getTravelDistance(source, destination);
        double travelTime = MapApi.getTravelTime(source, destination);
        return new Fare(travelDistance, travelTime);
    }

    public Fare getFareEstimate() {
        return fareEstimate;
    }
}

class Trip {
    final String id;
    private final String riderId;
    private final String driverId;
    private final Address source;
    private final Address destination;
    private final Fare fare;

    public Trip(TripRequest tripRequest, Driver driver) {
        this.id = tripRequest.id;
        this.riderId = tripRequest.riderId;
        this.driverId = driver.getId();
        this.source = tripRequest.source;
        this.destination = tripRequest.destination;
        this.fare = _finalFare(tripRequest.getFareEstimate(), driver);
    }

    private Fare _finalFare(Fare fare, Driver driver) {
        double travelDistance = MapApi.getTravelDistance(driver.currentLocation, source);
        double travelTime = MapApi.getTravelTime(driver.currentLocation, source);
        fare.computeFinalCost(travelDistance, travelTime);
        return fare;
    }

    public Double requestPayment() {
        boolean paymentCompleted = false;
        Rider rider = RiderManager.getRider(riderId);
        double amount = fare.getTotalCost();
        while (!paymentCompleted) {
            paymentCompleted = rider.completePayment(amount);
        }
        return amount;
    }
}

class TripManager {
    private static class Singleton {
        private static final TripManager instance = new TripManager();
    }
    private static final double THRESHOLD_DRIVER_TRAVEL_TIME = 15.00;
    private final Map<String, Trip> trips = new HashMap<>();
    private TripManager() {}
    public TripManager getInstance() {
        return Singleton.instance;
    }
    public static Trip getTrip(String tripId) {
        return Singleton.instance.trips.get(tripId);
    }

    public static Trip createTrip(TripRequest tripRequest) {
        List<Driver> viableDrivers = DriverManager.getNearbyDrivers(tripRequest.source, THRESHOLD_DRIVER_TRAVEL_TIME);
        for (Driver driver: viableDrivers) {
            boolean reserved = driver.reserve(tripRequest);
            if (reserved) {
                Trip bookedTrip = new Trip(tripRequest, driver);
                Singleton.instance.trips.put(bookedTrip.id, bookedTrip);
                return bookedTrip;
            }
        }
        return null;
    }
}

class Fare {
    private static final double COST_PER_KM = 13.50;
    private static final double COST_PER_MIN = 0.75;
    private final double travelDistance;
    private final double travelTime;
    private final double cost;
    private double totalCost;
    public Fare(double travelDistance, double travelTime) {
        this.travelDistance = travelDistance;
        this.travelTime = travelTime;
        this.cost = _computeCost(this.travelDistance, this.travelTime);
    }

    private double _computeCost(double travelDistance, double travelTime) {
        return (travelDistance*COST_PER_KM + travelTime*COST_PER_MIN);
    }

    private double surgeMultiplier()  {
        return 1.10;
    }

    public void computeFinalCost(double driverTravelDistance, double driverTravelTime) {
        double additionalCost = _computeCost(driverTravelDistance, driverTravelTime);
        this.totalCost = (this.cost + additionalCost) * surgeMultiplier();
    }

    public double getCost() {
        return cost;
    }

    public double getTotalCost() {
        return totalCost;
    }
}