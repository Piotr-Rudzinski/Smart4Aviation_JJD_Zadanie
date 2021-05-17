package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.FlightEntity.Flight;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FlightsRepository {
    private List<Flight> flightDB;
    private final Path PATH = Paths.get("src", "main", "resources", "FlightEntity.json");

    public void readFlights() {
        try {
            FileReader reader = new FileReader(String.valueOf(PATH));
            Type listType = new TypeToken<ArrayList<Flight>>(){}.getType();
            flightDB = new Gson().fromJson(reader, listType);
        }
        catch (IOException e) {
            System.out.printf("File FlightEntity.json can not be read. Program will be terminated");
            System.exit(0);
        }
    }

    public Set<String> getAvailableAirportCodes() {
        Set<String> availableAirportCodes = new HashSet<>();

        for (Flight flight : flightDB) {
            availableAirportCodes.add(flight.getDepartureAirportIATACode());
            availableAirportCodes.add(flight.getArrivalAirportIATACode());
        }
        return availableAirportCodes;
    }

    public Set<Integer> getAvailableFlightNumbers() {
        Set<Integer> availableFlightNumbers = new HashSet<>();

        for (Flight flight : flightDB) {
            availableFlightNumbers.add(flight.getFlightNumber());
        }
        return availableFlightNumbers;
    }

    public Set<String> getAvailableDates() {
        Set<String> availableFlightDates = new HashSet<>();

        for (Flight flight : flightDB) {
            availableFlightDates.add(flight.getDepartureDate());
        }
        return availableFlightDates;
    }

    public Flight getFlight(Integer flightNumber, String date) {
        List<Flight> flight = new ArrayList<>();
        flight = flightDB.stream()
                .filter(x -> x.getFlightNumber().equals(flightNumber))
                .filter(x -> x.getDepartureDate().equals(date))
                .collect(Collectors.toList());

        return flight.get(0);
    }

    public List<Flight> getFlightsWithDefinedFlightNumber(Integer flightNumber) {
        List<Flight> flightsWithDefinedFlightNumber = flightDB.stream()
                .filter(x -> x.getFlightNumber().equals(flightNumber))
                .collect(Collectors.toList());

        return  flightsWithDefinedFlightNumber;
    }

    public List<Flight> getDepartingList(String airportCode) {
        List<Flight> departingList = new ArrayList<>();

        departingList = flightDB.stream()
                .filter(x -> x.getDepartureAirportIATACode().equals(airportCode.toUpperCase()))
                .collect(Collectors.toList());

        return departingList;
    }

    public List<Flight> getArrivingList(String airportCode) {
        List<Flight> arrivingList = new ArrayList<>();

        arrivingList = flightDB.stream()
                .filter(x -> x.getArrivalAirportIATACode().equals(airportCode.toUpperCase()))
                .collect(Collectors.toList());

        return arrivingList;
    }
}
