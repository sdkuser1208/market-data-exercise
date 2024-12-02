package demo.messaging;

import demo.domain.MarketData;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Log4j2
public class MarketDataQueue {

    private final Queue<MarketData> marketDataQueue = new ConcurrentLinkedQueue<>();

    public Mono<Void> addToQueue(MarketData marketData) {
        return Mono.fromRunnable(() -> Optional.ofNullable(marketData)
                .ifPresentOrElse(
                        marketDataQueue::offer,
                        () -> log.info("Market data is null, can't add to queue"))
        );
    }

    /**
     *
     * @return market data object if exist in queue, otherwise returns null
     * Assumptions:
     * need to do agree on what to do when producer isn't ready or down.
     * possible scenarios, as we need to decouple producer and consumer
     * we can through exception however producer can be down in the middle of process yet unprocessed elements still
     * exist in queue
     * Approach implemented here is - return an Optional and log it for simplicity
     */
    public Mono<MarketData> pollFromQueue() {
        return Mono.justOrEmpty(marketDataQueue.poll());
    }

}
