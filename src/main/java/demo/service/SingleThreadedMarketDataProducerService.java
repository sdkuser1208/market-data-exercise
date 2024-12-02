package demo.service;

import demo.messaging.MarketDataQueue;
import demo.util.MarketDataUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *  This service produces market based on reactor single-threaded implementation.
 */
@Component
@Log4j2
public class SingleThreadedMarketDataProducerService {

    private final MarketDataQueue marketDataQueue;
    private final MarketDataUtil marketDataUtil;

    public SingleThreadedMarketDataProducerService(MarketDataQueue marketDataQueue, MarketDataUtil marketDataUtil) {
        this.marketDataQueue = marketDataQueue;
        this.marketDataUtil = marketDataUtil;
    }

    private Mono<Void> produce() {
        return marketDataQueue.addToQueue(marketDataUtil.getMarketData());
    }

    @PostConstruct
    public void startProducer() {
        Flux.interval(Duration.ZERO, Duration.ofMillis(50))//for every 50 mills produces market data
                .flatMap(timer -> produce())//TODO: error handling needs to be added
                .doOnSubscribe(subscription -> log.info("MarketDataProducer service started."))
                .subscribe();//TODO: error handling needs to be added
    }
}
