# Price API – Assessment

## Overview

This project implements a REST API to retrieve the applicable price for a given product, brand, and application date.

The solution focuses on:

* clean architecture
* performance optimization
* observability
* simplicity

## Technology Stack

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

## Architectural Decisions

* Layered / Hexagonal-inspired design
* The application separates concerns into:
- Controller (API layer)
- Service (application logic)
- Port/Adapter (data access abstraction)

This improves:

* testability
* maintainability
* flexibility of infrastructure changes

## Caching Strategy

Caching is implemented using Spring Cache:

`c@Cacheable(...)`

and configured in Hazelcast:

* TTL: 300 seconds
* Max entries: 1000 per node
* Eviction policy: LRU

### Database Optimization

A composite index was introduced:

`(BRAND_ID, PRODUCT_ID, START_DATE, END_DATE)`

## Error Handling

Centralized exception handling using @RestControllerAdvice ensures:

* consistent API responses
* proper HTTP status mapping

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

## Performance
* Tested via Integration tests using MockMvc
* Warm-up phase included
* Measured 100 iterations per scenario
* Measured using System.nanoTime()

### Results

| Scenario	           | Avg Response Time  |
|:--------------------|:-------------------|
| No index / No cache | 	 2.27 ms        |
| Index / No cache	   | 1.62 ms            |
| Index + Cache       | 	 1.07 ms        |

### Analysis
- Indexing reduced query time by ~28%
- Caching reduced response time by ~34%
- Total improvement ~53%

## Observability
Logging Strategy: 
* INFO → request tracing (controller)
* DEBUG → internal processing and cache miss (service)
* WARN → business issues

* Principles
- minimal and meaningful logging
- contextual data included
- separation by layer

## Security

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

## High-Level Architecture

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

## Conclusion

This solution prioritizes:

* simplicity over production readiness
* performance through measurable improvements
* maintainable architecture