package org.example;

import org.example.CargoEntity.LuggageEntity;
import org.example.FlightEntity.Flight;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
    FlightsRepository flightsRepository;
    CargoRepository cargoRepository;
    final Double RATE = 0.45;

    Double totalWeight;
    Integer[] cargoWeight;
    Integer[] baggageWeight;
    Double totalCargoWeight;
    Double totalBaggageWeight;

    Integer numberOfFlightsDepartingFromThisAirport;
    Integer numberOfFlightsArrivingToThisAirport;
    Integer numberOfBaggageArrivingToThisAirport;
    Integer numberOfBaggageDepartingFromThisAirport;
    String airportCode;

    public static void main( String[] args )
    {
        App app = new App();
        app.displayMenu();
    }

    private void displayMenu() {
        Integer flightNumber;
        String date;
        Integer firstLevelMenuChoice;
        flightsRepository = new FlightsRepository();
        flightsRepository.readFlights();

        cargoRepository = new CargoRepository();
        cargoRepository.readCargo();

        while (true) {
            displayMainMenu();
            firstLevelMenuChoice = getMenuChoice();

            switch (firstLevelMenuChoice) {
                case 1:
                    displayFirstOptionFlightNumberMenu(flightsRepository.getAvailableFlightNumbers());
                    flightNumber = getFirstOptionMenuChoice(flightsRepository.getAvailableFlightNumbers());
                    List<Flight> flight = flightsRepository.getFlightsWithDefinedFlightNumber(flightNumber);

                    if (flight.size() == 1) {
                        System.out.println();
                        System.out.println("There is only one date for such flight number in the database.");
                        date = flight.get(0).getDepartureDate();
                    } else {
                        System.out.println();
                        System.out.println("Select date for chosen flight number. Type any number from 1 to " + flight.size() + ".");

                        for (int i = 0; i < flight.size(); i++) {
                            String[] dateAndTime = flight.get(i).getDepartureDate().split("T");
                            System.out.println((i+1) + ". " + dateAndTime[0] + " " + dateAndTime[1]);
                        }

                        System.out.print("Choice: ");
                        Integer dateChoice = getFirstOptionMenuDateChoice(flight.size());
                        date = flight.get(dateChoice).getDepartureDate();
                    }

                    System.out.println("You've chosen flight number " + flightNumber + " on " + date + ".");

                    calculateCargoAndBaggageWeightForRequestedFlight(flightNumber, date);
                    displayFirstMenuResults();
                    break;
                case 2:
                    displaySecondOptionAirportCodesMenu(flightsRepository.getAvailableAirportCodes());
                    airportCode = getSecondOptionMenuChoice(flightsRepository.getAvailableAirportCodes());
                    calculateResultsForRequestedAirportCode();
                    displaySecondMenuResults();
                    break;
                case 3:
                    System.out.println();
                    System.out.println("End.");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    private void calculateResultsForRequestedAirportCode() {
        List<Flight> departingList = flightsRepository.getDepartingList(airportCode);
        List<Flight> arrivingList = flightsRepository.getArrivingList(airportCode);
        numberOfFlightsDepartingFromThisAirport = departingList.size();
        numberOfFlightsArrivingToThisAirport = arrivingList.size();
        List<LuggageEntity> luggageArrivingList = cargoRepository.getBaggageList(arrivingList);
        List<LuggageEntity> luggageDepartingList = cargoRepository.getBaggageList(departingList);
        numberOfBaggageArrivingToThisAirport = calculateNumberOfBaggage(luggageArrivingList);
        numberOfBaggageDepartingFromThisAirport = calculateNumberOfBaggage(luggageDepartingList);
    }

    private Integer calculateNumberOfBaggage(List<LuggageEntity> luggage) {
        Integer luggageAmount = 0;

        for (int i = 0; i < luggage.size(); i++) {
            luggageAmount = luggageAmount + luggage.get(i).getPieces();
        }
        return luggageAmount;
    }

    private void calculateCargoAndBaggageWeightForRequestedFlight(Integer flightNumber, String date) {
        Flight flight = flightsRepository.getFlight(flightNumber, date);
        Integer flightId = flight.getFlightId();
        cargoWeight = cargoRepository.calculateCargoWeight(flightId);
        totalCargoWeight = (cargoWeight[0] + (cargoWeight[1] * RATE));
        baggageWeight = cargoRepository.calculateBaggageWeight(flightId);
        totalBaggageWeight = (baggageWeight[0] + (baggageWeight[1] * RATE));

        totalWeight = totalCargoWeight + totalBaggageWeight;
    }

    private Integer getFirstOptionMenuDateChoice(Integer flightSize){
        boolean isDateChoiceNotValid = true;
        Integer dateChoice = 0;

        while (isDateChoiceNotValid) {
            try {
                isDateChoiceNotValid = false;
                dateChoice = getMenuIntegerChoice();

                if (!(dateChoice > 0 && dateChoice <= flightSize)) {
                    isDateChoiceNotValid = true;
                    System.out.print("Value out of the scope. Type correct value: ");
                }
            } catch (Exception e) {
                isDateChoiceNotValid = true;
                System.out.print("Wrong value. Try again: ");;
            }
        }
        System.out.println();
        return dateChoice - 1;
    }

    private Integer getMenuChoice() {
        boolean isMenuChoiceNotValid = true;
        Integer menuChoice = 0;

        while (isMenuChoiceNotValid) {

            try {
                isMenuChoiceNotValid = false;
                menuChoice = getMenuIntegerChoice();

                if (!(menuChoice > 0 && menuChoice < 4)) {
                    isMenuChoiceNotValid = true;
                    System.out.println("Value out of the scope. Type correct value: ");
                }
            } catch (Exception e) {
                isMenuChoiceNotValid = true;
                System.out.println("Wrong value. Try again: ");;
            }
        }
        System.out.println();
        return menuChoice;
    }

    private Integer getFirstOptionMenuChoice(Set<Integer> availableFlightNumbers) {
        boolean isMenuChoiceNotValid = true;
        Integer menuChoice = 0;

        while (isMenuChoiceNotValid) {

            try {
                isMenuChoiceNotValid = false;
                menuChoice = getMenuIntegerChoice();

                if (!availableFlightNumbers.contains(menuChoice)) {
                    isMenuChoiceNotValid = true;
                    System.out.print("Value out of the scope. Type correct value: ");
                }
            } catch (Exception e) {
                isMenuChoiceNotValid = true;
                System.out.print("Wrong value. Try again: ");;
            }
        }
        System.out.println();
        return menuChoice;
    }

    private String getSecondOptionMenuChoice(Set<String> availableAirportCodes) {
        boolean isMenuChoiceNotValid = true;
        String airportCode = "";

        while (isMenuChoiceNotValid) {

            try {
                isMenuChoiceNotValid = false;
                airportCode = getMenuStringChoice().toUpperCase();

                if (!availableAirportCodes.contains(airportCode)) {
                    isMenuChoiceNotValid = true;
                    System.out.print("Airport code out of the scope. Type correct one: ");
                }
            } catch (Exception e) {
                isMenuChoiceNotValid = true;
                System.out.print("Wrong code. Try again: ");;
            }
        }
        System.out.println();
        return airportCode;
    }

    private Integer getMenuIntegerChoice () throws Exception {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private String getMenuStringChoice () throws Exception {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void displayMainMenu() {
        System.out.println("*************************************************************");
        System.out.println("*    FLIGHT APPLICATION");
        System.out.println("*");
        System.out.println("*    MENU");
        System.out.println("*");
        System.out.println("* 1. Flight Number and date");
        System.out.println("* 2. IATA Airport Code and date");
        System.out.println("* 3. Exit");
        System.out.println("*************************************************************");
        System.out.print  ("* Your choice: ");
    }

    private void displayFirstOptionFlightNumberMenu(Set<Integer> flightNumbers) {
        System.out.println();
        System.out.println("*************************************************************");
        System.out.println("*    MENU: 1. Flight Number and date");
        System.out.println("*");
        System.out.println("* Type flight number " + flightNumbers.toString() + ".");
        System.out.println("*************************************************************");
        System.out.print  ("* Your choice: ");
    }

    private void displaySecondOptionAirportCodesMenu(Set<String> availableAirportCodes) {
        System.out.println();
        System.out.println("*************************************************************");
        System.out.println("*    MENU: 2. IATA Airport Code");
        System.out.println("*");
        System.out.println("* TIATA Airport Code " + availableAirportCodes.toString() + ".");
        System.out.println("*************************************************************");
        System.out.print  ("* Your choice: ");
    }

    private void displayFirstMenuResults() {
        System.out.println();
        System.out.println("**********************************************************************");
        System.out.println("* Results:");
        System.out.println("*");
        System.out.println("* Cargo Weight for requested Flight: " + cargoWeight[0] + "kg + " + cargoWeight[1] + "lb. Total is " + totalCargoWeight + "kg.");
        System.out.println("* Baggage Weight for requested Flight: = " + baggageWeight[0] + "kg + " + baggageWeight[1] + "lb. Total is " + totalBaggageWeight + "kg.");
        System.out.println("* Total weight is " + totalWeight + "kg.");
        System.out.println("**********************************************************************");
        System.out.println();
    }

    private void displaySecondMenuResults() {
        System.out.println();
        System.out.println("**********************************************************************");
        System.out.println("* Results:");
        System.out.println("*");
        System.out.println("* Number of flights departing from " + airportCode.toUpperCase() + " airport : " + numberOfFlightsDepartingFromThisAirport + ".");
        System.out.println("* Number of flights arriving to " + airportCode.toUpperCase() + " airport : " + numberOfFlightsArrivingToThisAirport + ".");
        System.out.println("* Total number (pieces) of baggage arriving to " + airportCode.toUpperCase() + " airport : " + numberOfBaggageArrivingToThisAirport + ".");
        System.out.println("* Total number (pieces) of baggage departing from " + airportCode.toUpperCase() + " airport : " + numberOfBaggageDepartingFromThisAirport + ".");
        System.out.println("**********************************************************************");
        System.out.println();
    }
}
