package demo.repo;

import demo.domain.MarketData;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketDataRepo {

    private final Map<String, MarketData> marketDataMap = new ConcurrentHashMap<>();

    public Mono<MarketData> getLatestMarketDataForInstrumentIfExist(String currencyPair) {
        return Mono.justOrEmpty(marketDataMap.get(currencyPair));
    }

    public Mono<Void> update(MarketData marketData) {
        return Mono.fromRunnable(() -> marketDataMap.put(marketData.instrument(), marketData));
    }
}
