package demo.rest;

import demo.service.MarketDataService;
import demo.domain.MarketData;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class MarketDataController {
    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/instrument/{currency1}/{currency2}")
    public Mono<ResponseEntity<MarketData>> processUrl(@PathVariable String currency1, @PathVariable String currency2) {
        if (isNotValid(currency1) && isNotValid(currency2)) {
            log.error("Invalid currencyPair url currency1{}/currency2{}", currency1, currency2);
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return marketDataService.getMarketDataByInstrument(currency1 + "/" + currency2)
                .flatMap(marketData -> Mono.just(ResponseEntity.ok(marketData)));
    }

    private boolean isNotValid(String str) {
        return str == null || str.isBlank();
    }
}
