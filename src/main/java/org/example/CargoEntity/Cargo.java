package org.example.CargoEntity;

public class Cargo {
    private Integer flightId;
    private LuggageEntity[] baggage;
    private LuggageEntity[] cargo;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public LuggageEntity[] getBaggage() {
        return baggage;
    }

    public void setBaggage(LuggageEntity[] baggage) {
        this.baggage = baggage;
    }

    public LuggageEntity[] getCargo() {
        return cargo;
    }

    public void setCargo(LuggageEntity[] cargo) {
        this.cargo = cargo;
    }
}
