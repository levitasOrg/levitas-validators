# Levitas Validators — Spring Boot Demo

A minimal Spring Boot 3.x REST API demonstrating all levitas-validators features.

## Prerequisites

- Java 17+
- Maven 3.9+

## Setup

First, install the levitas-validators library into your local Maven repository:

```bash
cd ../..
mvn install -DskipTests
cd examples/spring-boot-demo
```

## Run

```bash
mvn spring-boot:run
```

> **If you get `No plugin found for prefix 'spring-boot'`**, Maven is not searching the
> `org.springframework.boot` plugin group by default. Use the fully qualified form instead:
>
> ```bash
> mvn org.springframework.boot:spring-boot-maven-plugin:run
> ```
>
> To make `mvn spring-boot:run` work permanently, add this to your `~/.m2/settings.xml`:
>
> ```xml
> <settings>
>   <pluginGroups>
>     <pluginGroup>org.springframework.boot</pluginGroup>
>   </pluginGroups>
> </settings>
> ```

The server starts on port 8080.

## Endpoints

### Validate GSTIN

```bash
curl http://localhost:8080/validate/gstin/29AAACB1234C1ZB
```

Response:
```json
{"valid":true,"info":{"entityCode":"1","stateName":"Karnataka","entityType":"REGULAR","stateCode":"29","pan":"AAACB1234C"}}
```

### Validate PAN

```bash
curl http://localhost:8080/validate/pan/AAAPA1234A
```

Response:
```json
{
  "valid": true,
  "entityType": "INDIVIDUAL",
  "entityDescription": "Individual"
}
```

### Validate IFSC

```bash
curl http://localhost:8080/validate/ifsc/HDFC0001234
```

Response:
```json
{
  "valid": true,
  "bankCode": "HDFC",
  "bankName": "HDFC Bank",
  "branchCode": "001234"
}
```

### Validate UPI VPA

```bash
curl "http://localhost:8080/validate/vpa/user@okhdfcbank"
```

Response:
```json
{
  "valid": true,
  "username": "user",
  "handle": "okhdfcbank",
  "pspName": "Google Pay (HDFC)"
}
```

### Mask Aadhaar

```bash
curl -X POST http://localhost:8080/mask/aadhaar \ -H "Content-Type: application/json" \ -d '{"aadhaar": "234123412346"}'
```
tion/json" \ -d '{"aadhaar": "234123412346"}'
{"timestamp":"2026-04-12T22:14:43.789+00:00","status":400,"error":"Bad Request","path":"/mask/aadhaar"}curl: (3) URL rejected: Bad hostname
curl: (3) URL rejected: Bad hostname
curl: (3) unmatched close brace/bracket in URL position 13:
234123412346}'

Response:
```json
{
  "masked": "XXXX-XXXX-2346",
  "valid": true
}
```
