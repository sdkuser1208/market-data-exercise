package demo.util;

import demo.domain.MarketData;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Component
public class MarketDataUtil {

    private final AtomicInteger counter = new AtomicInteger(0);

    public MarketData getMarketData() {
        var pair = getRandomCurrencyPair();
        var price = randomPrice();

        //log.info("generated at {} currency pair: {}, bid: {}, ask: {}",
          ///      LocalTime.now(), pair, price, price);
        return new MarketData(pair, price, price, LocalDateTime.now());
    }

    private final Random random = new Random();
    private final List<String> CURRENCY_PAIRS = List.of(
            "EUR/GBP",
            "EUR/USD",
            "EUR/INR",
            "INR/GBP",
            "USD/GBP"
    );

    private String getRandomCurrencyPair() {
        return CURRENCY_PAIRS.get(counter.getAndIncrement() % (CURRENCY_PAIRS.size()));
    }

    private double randomPrice() {
        return 1.0 + random.nextDouble();
    }
}
