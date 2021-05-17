package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.CargoEntity.Cargo;
import org.example.CargoEntity.LuggageEntity;
import org.example.FlightEntity.Flight;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CargoRepository {
    private List<Cargo> cargoDB;
    private final Path PATH = Paths.get("src", "main", "resources", "CargoEntity.json");
    private final String KILOGRAMS = "kg";
    private final String POUNDS = "lb";

    public void readCargo() {
        try {
            FileReader reader = new FileReader(String.valueOf(PATH));
            Type listType = new TypeToken<ArrayList<Cargo>>(){}.getType();
            cargoDB = new Gson().fromJson(reader, listType);
        }
        catch (IOException e) {
            System.out.printf("File CargoEntity.json can not be read. Program will be terminated");
            System.exit(0);
        }
    }

    public Integer[] calculateCargoWeight(Integer flightId) {
        Integer[] luggageWeight = {0, 0};

        for (int i = 0; i < cargoDB.size(); i++) {
            if (cargoDB.get(i).getFlightId().equals(flightId)) {
                LuggageEntity[] luggageCargo = cargoDB.get(i).getCargo();

                for (int j = 0; j < luggageCargo.length; j++) {
                    if (luggageCargo[j].getWeightUnit().equals(KILOGRAMS)) {
                        luggageWeight[0] = luggageWeight[0] + luggageCargo[j].getWeight();
                    } else {
                        luggageWeight[1] = luggageWeight[1] + luggageCargo[j].getWeight();
                    }
                }
            }
        }
    return luggageWeight;
    }

    public Integer[] calculateBaggageWeight(Integer flightId) {
        Integer[] luggageWeight = {0, 0};

        for (int i = 0; i < cargoDB.size(); i++) {
            if (cargoDB.get(i).getFlightId().equals(flightId)) {
                LuggageEntity[] luggageBaggage = cargoDB.get(i).getBaggage();

                for (int j = 0; j < luggageBaggage.length; j++) {
                    if (luggageBaggage[j].getWeightUnit().equals(KILOGRAMS)) {
                        luggageWeight[0] = luggageWeight[0] + luggageBaggage[j].getWeight();
                    } else {
                        luggageWeight[1] = luggageWeight[1] + luggageBaggage[j].getWeight();
                    }
                }
            }
        }
        return luggageWeight;
    }

    public List<LuggageEntity> getBaggageList (List<Flight> sourceList) {
        List<LuggageEntity> resultList = new ArrayList<>();

        Integer flightNumber;
        for (Flight flight: sourceList) {
            flightNumber = flight.getFlightId();

            for (int i = 0; i < cargoDB.size(); i++) {
                if (cargoDB.get(i).getFlightId().equals(flightNumber)) {
                    LuggageEntity[] baggage = cargoDB.get(i).getBaggage();

                    resultList.addAll(Arrays.asList(baggage));
                }
            }
        }
        return resultList;
    }
}
