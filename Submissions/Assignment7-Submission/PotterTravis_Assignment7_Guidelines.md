# Assignment 7 Submission

`LicenseService.getLicensesByOrganization()` is the only method protected by Resilience4j:

```java
@CircuitBreaker(name = "licenseService", fallbackMethod = "customFallbackLicenseList")
public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
    ...
    randomlyRunLong();
    return licenseRepository.findByOrganizationId(organizationId);
}
```

- `randomlyRunLong()` allows for about 1 call in 3 to sleep for 5 seconds and throws a `TimeoutException`, which simulates a slow or failing endpoint.
- The breaker is configured in `licensing-service/src/main/resources/bootstrap.yml`: it remembers the last 5 calls and opens when 50% or more fail. It then stays open (blocking) for 10 seconds, then goes half-open and allows 3 trial calls.
- `customFallbackLicenseList` is the custom fallback. It logs a warning saying why it ran, and it returns a placeholder license whose `comment` says whether the circuit was open or the call failed / timed out.

## Prerequisites

- Apache Maven (http://maven.apache.org)
- JDK 17
- Docker Desktop (includes Docker Engine, CLI, and Compose)
- Postman

## Run the stack

Unzip `PotterTravis_Assignment7_Code.zip`, then open a terminal in the extracted `PotterTravis_Assignment7_Code` directory.

```powershell
mvn clean package dockerfile:build
docker-compose -f docker/docker-compose.yml up
```

> **Docker must expose port 2375.**

## Using the Postman test the Circuit Breaker and Fallback

For a clean test, restart the licensing service first so the breaker starts closed with no failure history:

```powershell
docker-compose -f docker/docker-compose.yml restart licensingservice
```

Wait about 30 seconds for it to re-register, then:

1. In Postman, build a RUN with **Get Licenses by Organization (circuit breaker + fallback)**.
2. Set **Iterations** to `20` and **Delay** to `0 ms`, then click **Run**.
3. Open **View > Show Postman Console** for the per-call result. The request's test script logs one outcome per call:
   - `REAL DATA` is a normal response, around 15 ms.
   - `FALLBACK` 5,000 ms response time representing a slow call that timed out while the **breaker was still closed**.
   - `FALLBACK` 10 ms response time is a rejected call because the **breaker is open**.
   - `CIRCUIT OPEN (fail fast)` and `ERROR / TIMEOUT` appear only if the fallback is removed.

Expected pattern: a mix of `REAL DATA` and slow `FALLBACK` results, then once at least 3/5 calls have failed, the breaker opens and following calls are quickly marked `FALLBACK`.

View the breaker's state transitions after the run. The easiest way is the **Circuit Breaker Events** request in the Postman collection, which pretty-prints the JSON. Or, from PowerShell, as a table with one row per event:

```powershell
(curl.exe -s http://localhost:8080/actuator/circuitbreakerevents/licenseService | ConvertFrom-Json).circuitBreakerEvents | Format-Table creationTime,type,durationInMs,stateTransition
```

Look for `ERROR` rows (the 5000 ms timeouts), then `FAILURE_RATE_EXCEEDED` and `CLOSED_TO_OPEN`, followed by a run of `NOT_PERMITTED` events (calls rejected while the breaker is open).

To view the custom fallback's logging, one row per fallback call (`CALL FAILED` is a real timeout, `CIRCUIT OPEN` is a call rejected by the open breaker):

```powershell
docker logs docker-licensingservice-1 2>&1 | Select-String 'customFallbackLicenseList' | ForEach-Object {
  if ($_.Line -match '^(\S+ \S+)\s+WARN.*customFallbackLicenseList: (circuit breaker is OPEN)') { [pscustomobject]@{Time=$matches[1]; Cause='CIRCUIT OPEN (call rejected)'} }
  elseif ($_.Line -match '^(\S+ \S+)\s+WARN.*customFallbackLicenseList: call failed.*\((\w+)') { [pscustomobject]@{Time=$matches[1]; Cause="CALL FAILED ($($matches[2]))"} }
} | Format-Table -AutoSize
```

These rows line up in time with the `ERROR` and `NOT_PERMITTED` rows in the circuit breaker events table above. Each log line is written by the `logger.warn` call inside `customFallbackLicenseList`, so seeing them proves the custom fallback ran.

## Stopping the stack

```powershell
docker-compose -f docker/docker-compose.yml down -v
```

This stops all five containers and removes the Postgres data volume, so the database starts empty (and is re-seeded by `init.sql` and `data.sql`) next time.
