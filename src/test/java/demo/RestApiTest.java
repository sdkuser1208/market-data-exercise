package demo;

import demo.domain.MarketData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void testApi() {
		webTestClient.get()
				.uri("/instrument/xyz/GBP")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.isEmpty();

		Flux.fromArray(new String[]{"/instrument/EUR/GBP", "/instrument/EUR/INR", "/instrument/USD/GBP"})
				.flatMap(instrumentPath -> webTestClient.get()
						.uri(instrumentPath)
						.exchange()
						.expectStatus().isOk()
						.returnResult(MarketData.class)
						.getResponseBody()
				)
				.collectList()
				.log()
				.block();
	}
}