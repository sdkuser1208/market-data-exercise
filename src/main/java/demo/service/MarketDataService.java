package demo.service;

import demo.domain.MarketData;
import demo.repo.MarketDataRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MarketDataService {

    private final MarketDataRepo marketDataRepo;

    public MarketDataService(MarketDataRepo marketDataRepo) {
        this.marketDataRepo = marketDataRepo;
    }

    public Mono<MarketData> getMarketDataByInstrument(final String instrument) {
        return this.marketDataRepo.getLatestMarketDataForInstrumentIfExist(instrument);
    }
}
