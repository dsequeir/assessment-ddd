# Price API – Assessment

## Overview

This project implements a REST API to retrieve the applicable price for a given product, brand, and application date.
The applicable price is the top priority, if several are applicable. 

The solution focuses on:

* clean architecture
* performance optimization
* observability
* simplicity

## Technology Stack
* 
* Java 25
* Spring Boot 4.0.4
* Spring Boot – application framework
* Spring Web – REST API
* Spring Cache – caching abstraction
* Hazelcast – in-memory distributed cache
* H2 Database – in-memory persistence
* SpringDoc OpenAPI – API documentation
* SLF4J + Logback – logging
* JUnit + MockMvc – integration testing

## Instalation & Testing

Tha application can be run via:

```bash
    ./mvnw spring-boot:run
```

The application will start on localhost, port 8080

To run the tests:

```bash
./mvnw test
```

To Verify the application:

Once the application is running, you can test the API using:

* Valid request with price found: 
* 
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
 
http://localhost:8080/swagger-ui.html

## Architectural Decisions

* Layered / Hexagonal-inspired design
* The application separates concerns into:
- Controller (API layer)
- Service (application layer)
- Port/Adapter (data access abstraction)
* The main packages are:
- Application (Orchestration)
- Doamin (Use case)
- Infrastructure (Web & Data access)

This improves:

* testability
* maintainability
* flexibility of infrastructure changes

## Observability
Logging Strategy:
* INFO → request tracing (controller)
* DEBUG → internal processing and cache miss (service)
* WARN → business issues

* Principles
- minimal and meaningful logging
- contextual data included
- separation by layer

## Error Handling

Centralized exception handling using @RestControllerAdvice ensures:

* consistent API responses
* proper HTTP status mapping

## Performance

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

Note: The results are not accurate or cientifical, due to the very limited 
number of records in the database, and the influences of the local execution environment.

### Trade-offs

* Caching vs Data Freshness
* Cached results may become stale
* Mitigated using TTL
* Indexing vs Write Cost
* Improves read performance
* Adds slight overhead on writes
* Simplicity vs Production Readiness
* Embedded H2 and Hazelcast used for simplicity
* Production would require external systems

## Possible Improvements

* Cache hit/miss metrics
* Load testing
* Error resilience/Circuit breaker
* Contract testing
* Validation improvements
* Production readiness:
- External Hazelcast cluster or Redis
- TTL tuning
- Monitor cache hit ratio
- External Database
- Validate indexing with real data
- Local/Test/Live environment configurations

### Security

Recommended:

* OAuth2 (client credentials / auth code)
* OIDC for identity
* HTTPS mandatory
* mTLS for service-to-service
* API Gateway + rate limiting
* Observability (advanced)
* Micrometer + Prometheus
* OpenTelemetry tracing
* Centralized logging (ELK/Grafana)

## Conclusion

**This solution prioritizes:**

* simplicity over production readiness
* performance through measurable improvements
* maintainable architecture