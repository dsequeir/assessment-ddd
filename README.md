# Price API – Assessment

## Overview

This project implements a REST API to retrieve the applicable price for a given product, brand, and application date.
If there are more than one prices applicable, the selected price is
the one with bigger priority.

The solution focuses on:

* clean architecture
* performance optimization
* observability
* simplicity

## Technology Stack
* 
* Java 25
* Spring Boot 4.0.5
* Spring Boot – application framework
* Spring Web – REST API
* Spring Cache – caching abstraction
* Hazelcast – in-memory distributed cache
* H2 Database – in-memory persistence
* SpringDoc OpenAPI – API documentation
* SLF4J + Logback – logging
* JUnit + MockMvc – integration testing

## Installation & Testing

The application can be run via:

```bash
    ./mvnw spring-boot:run
```

The tests can be run via: 

```bash
./mvnw test
```

The API can be tested performing calls to the endpoint. 

* Valid request with price found: 

```bash 
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

* Valid request with no price found:

```bash
* curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-09T10:00:00&productId=35455&brandId=1"
```

* Invalid request:

```bash 
curl "http://localhost:8080/api/v1/prices?productId=35455&brandId=1"
```

## API Documentation

 Available via Swagger:

http://localhost:8080/swagger-ui/index.html

## Architectural Decisions

* Layered / Hexagonal design
* The application separates concerns into:
- Controller: exposes REST endpoints
- UseCase: orchestrates application logic
- Domain:
    - Model: Price
    - Policy: PriceSelector (business rule)
    - Port: PricePort
- Infrastructure:
    - Persistence: JPA / H2
    - Web: REST controllers
* The main packages are:
- Application (Use case)
- Domain Pricing (Core domain & business rules)
- Infrastructure (Web & Data access)

### Pricing Rule

The system selects the applicable price based on:

- Valid date range
- Product and brand
- Highest priority (business rule)

This rule is encapsulated in a domain policy (PriceSelector),
keeping business logic isolated from application orchestration and
the implementation/technical details of the data layer.
The policy is applied in a Strategy pattern, applying max priority in this case,
but adaptable to other alternative strategies without affecting the use case.

## Observability

Logging Strategy:
* INFO → request tracing (controller)
* DEBUG → internal processing and cache miss 
* WARN → business issues
* ERROR -> Unexpected error

## Error Handling

Centralized exception handling using @RestControllerAdvice ensures:

* consistent API responses
* proper HTTP status mapping

## Performance

For this assessment, there are two levels of performance
improvements, to show some possibilities. 

* Tested via Integration tests using MockMvc
* Warm-up phase included
* Measured 100 iterations per scenario
* Measured using System.nanoTime()

### Caching Strategy

Caching is implemented using Spring Cache:

`c@Cacheable(...)`

and configured in Hazelcast:

* TTL: 300 seconds
* Max entries: 1000 per node
* Eviction policy: LRU

### Database Optimization

A composite index was introduced:

`(BRAND_ID, PRODUCT_ID, START_DATE, END_DATE)`

### Results

| Scenario	           | Avg Response Time |
|:--------------------|:------------------|
| No index / No cache | 	 1.41 ms       |
| Index / No cache	   | 1.17 ms           |
| Index + Cache       | 	 0.76 ms       |

### Analysis

- Indexing reduced query time by ~17%
- Caching reduced response time by ~35%
- Total improvement ~46%

Note: The results are not scientific, due to the very limited 
number of records in the database, and the influences of the local execution environment.
Consider that the test is considering the same request with 100% of cache hits.
Additionally, a real use case would include write operations, which
could be affected for the index. 

## Possible Improvements

* Cache hit/miss metrics
- External Hazelcast cluster or Redis
- TTL tuning
- Monitor cache hit ratio
- External Database
- Validate indexing with real data
- Local/Test/Live environment configurations
* Load testing
* Error resilience
* Environment-based configuration (profiles)

### Security

The security has been discarded due to simplicity, but 
it is recommended for production, including at least:

* HTTPS
* mTLS for service-to-service
* API Gateway + rate limiting

## Conclusion

This solution prioritizes:

* Simplicity over production readiness
* Inclusion of measurable performance improvements