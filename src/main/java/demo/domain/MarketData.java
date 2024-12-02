package demo.domain;

import java.time.LocalDateTime;

public record MarketData(String instrument, double bid, double ask, LocalDateTime localDateTime) {

    public String toString() {
        return instrument + " bid: " + bid + "ask: " + ask + "time: " + localDateTime;
    }
}
