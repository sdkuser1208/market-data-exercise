package demo;

import demo.domain.MarketData;
import demo.messaging.MarketDataQueue;
import demo.repo.MarketDataRepo;
import demo.service.MarketDataListenerService;
import demo.service.MarketDataService;
import demo.service.SingleThreadedMarketDataProducerService;
import demo.util.MarketDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 *  Runs 3 different consumers parallely for EUR/GBP and EUR/USD and invalid currency pair
 */
public class MarketDataServiceIntegrationTest {

    private MarketDataService marketDataService;

    @BeforeEach
    void setup() {
        MarketDataUtil marketDataUtil = new MarketDataUtil();
        MarketDataRepo marketDataRepo = new MarketDataRepo();
        MarketDataQueue marketDataQueue = new MarketDataQueue();

        MarketDataListenerService marketDataListenerService = new MarketDataListenerService(marketDataRepo, marketDataQueue);
        marketDataListenerService.startListener();
        SingleThreadedMarketDataProducerService producerService = new SingleThreadedMarketDataProducerService(marketDataQueue, marketDataUtil);
        producerService.startProducer();
        marketDataService = new MarketDataService(marketDataRepo);
    }

    @Test
    void testService() throws Exception {

        var consumer1 = Flux.interval(Duration.ofMillis(10))
                .map(j -> "EUR/GBP")
                .log()
                .flatMap(marketDataService::getMarketDataByInstrument)
                .log("Received response from service")
                .take(6);

        StepVerifier.create(consumer1)
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/GBP"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/GBP"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/GBP"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/GBP"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/GBP"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/GBP"))
                .thenCancel()
                .verify();

        Flux<MarketData> consumer2 = Flux.interval(Duration.ofMillis(10))
                .map(j -> "EUR/USD")
                .log()
                .flatMap(marketDataService::getMarketDataByInstrument)
                .log("Received response from service")
                .take(6);

        Flux<MarketData> consumer3 = Flux.interval(Duration.ofMillis(10))
                .map(j -> "UBS/XYZ")
                .flatMap(instrument -> marketDataService.getMarketDataByInstrument(instrument)
                        .switchIfEmpty(Mono.just(new MarketData("NoInstrument", 0.0D, 0.0D, null))))
                .log("Received response from service for xyz")
                .take(6);

        StepVerifier.create(consumer2)
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/USD"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/USD"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/USD"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/USD"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/USD"))
                .expectNextMatches(marketData -> marketData.instrument().equals("EUR/USD"))
                .thenCancel()
                .verify();

        StepVerifier.create(consumer3)
                .expectNextMatches(marketData -> marketData.instrument().equals("NoInstrument"))
                .expectNextMatches(marketData -> marketData.instrument().equals("NoInstrument"))
                .expectNextMatches(marketData -> marketData.instrument().equals("NoInstrument"))
                .expectNextMatches(marketData -> marketData.instrument().equals("NoInstrument"))
                .expectNextMatches(marketData -> marketData.instrument().equals("NoInstrument"))
                .expectNextMatches(marketData -> marketData.instrument().equals("NoInstrument"))
                .thenCancel()
                .verify();
        Thread.sleep(1000);
    }
}
