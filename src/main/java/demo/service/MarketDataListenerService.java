package demo.service;

import demo.messaging.MarketDataQueue;
import demo.repo.MarketDataRepo;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Log4j2
public class MarketDataListenerService {

    private final MarketDataRepo marketDataRepo;
    private final MarketDataQueue marketDataQueue;

    public MarketDataListenerService(MarketDataRepo marketDataRepo, MarketDataQueue marketDataQueue) {
        this.marketDataRepo = marketDataRepo;
        this.marketDataQueue = marketDataQueue;
    }

    private Mono<Void> listen() {
        return marketDataQueue.pollFromQueue()
                .flatMap(marketDataRepo::update);
    }

    @PostConstruct
    public void startListener() {
        Flux.interval(Duration.ZERO, Duration.ofMillis(10))//for every 10 mills
                .flatMap(timer -> listen())
                .doOnSubscribe(subscription -> log.info("MarketDataListener service started."))
                .subscribe();
    }
}
