Design:
Producer(SingleThreadedMarketDataProducerService) and Listener(MarketDataListenerService) starts during the start-up of application.

Lock-free implementation, used reactor based and Gradlle(spring boot for REST, IOC and package management).

Producer produces market data model and writes to MarketData Queue every 50 milli seconds.
Listener listens to MarketData Queue every 10 milli seconds and writes to Repo.

Market Data Service reads from Repo whenever a request for currency pair,
if there's no currency pair exist in repo it just returns Mono empty which is like Optional empty.

Tested parallel consumers running at the same time, please refer to integration test MarketDataServiceIntegrationTest.

Technology:
Why Reactor was used over Vert.x: Have been using reactor currently at work for past 5 years and 
used Vert.X only in one project so purely based on familiarity and quickness to deliver the exercise were the reason to choose this technology.

And referred Spring docs for spring boot test while implementation.

Assumptions:
API MarketDataService, returns empty when currency pair doesn't exist in MarketDataRepo.

Improvements:
Interface extraction, to make it more interface based coupling.
Queue implementation, depends on requirements
Test coverage
Error handling 
Logging
Extend current solution to support more producers
Springbok - replacement


