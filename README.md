# Northstar Platform – Core Framework
**Version:** 1.0.0

---

## 1. Overview

The **Northstar Platform – Core Framework** is a modular, extensible security, logging foundation designed for **enterprise-grade microservices**.

It provides a **consistent, opinionated, yet opt-in** approach to:

- Logging context propagation (tenant, user, correlation ID)
- Audit logging
- Performance measurement
- Observability integration (Micrometer / OpenTelemetry)
- Logging governance and enforcement

The framework is **non-intrusive**, **Spring Boot 3 compatible**, and supports both **web and non-web applications**.

> **Design mantra:**  
> _Logging context is produced once and consumed everywhere._

---

## 2. Design Principles

This framework is built on the following principles:

- **Separation of concerns**
- **Explicit opt-in** for advanced features
- **No forced dependencies**
- **Platform-neutral logging APIs**
- **Backward compatibility**
- **Production-safe defaults**

---

## 3. Module Overview

The framework is delivered as multiple Maven modules. Teams can consume **only what they need**.

### 3.1 Module Summary

| Module                          | Purpose |
|---------------------------------|--------|
| `commons-logging-core`          | Core contracts, constants, and shared abstractions |
| `commons-logging-context`       | MDC population (tenant, user, correlationId) |
| `commons-logging-audit`         | Declarative audit logging via annotations |
| `commons-logging-performance`   | Method-level performance logging |
| `commons-logging-observability` | Bridge to Micrometer / OpenTelemetry |
| `commons-logging-enforcement`   | Optional logging governance (Logback) |
| `northstar-core-bom`            | Centralized dependency management |

---

## 4. Dependency Management (Required)

All client applications **must import the BOM**.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.arch.northstar.platform</groupId>
            <artifactId>northstar-core-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 5. Logging Context (`commons-logging-context`)

### 5.1 What It Does

- Extracts tenant and user information from requests
- Generates or propagates correlation IDs
- Stores values in MDC
- Works for:
    - Spring MVC
    - Non-web / batch jobs
    - Async processing

### 5.2 Headers Used (Default)

| Header | MDC Key |
|------|--------|
| `X-Tenant-Id` | `tenantId` |
| `X-User-Id` | `userId` |
| `X-Correlation-Id` | `correlationId` |

### 5.3 Enablement

This module is **auto-enabled** for servlet applications when present on the classpath.

---

## 6. Audit Logging (`commons-logging-audit`)

### 6.1 Purpose

Audit logging records **business-relevant actions** for:

- Compliance
- Security
- Traceability
- Regulatory requirements

### 6.2 Usage

```java
@Audit(
    action = "CREATE_ORDER_API",
    service = "Order-Service",
    captureResult = true
)
public void createOrder(...) {
}
```

### 6.3 Configuration

```yaml
platform:
  audit:
    enabled: true
```

### 6.4 Automatic Enrichment

Audit events are automatically enriched with:

- `tenantId`
- `userId`
- `correlationId`
- Execution time
- Success / failure status

Developers **do not need** to pass context explicitly.

---

## 7. Performance Logging (`commons-logging-performance`)

### 7.1 Purpose

Provides **lightweight method execution timing** without requiring metrics backends.

### 7.2 Usage

```java
@Performance(
    operation = "CREATE_ORDER_API",
    thresholdMs = 50
)
public void createOrder(...) {
}
```

### 7.3 Behavior

| Condition | Result |
|--------|--------|
| `thresholdMs = 0` | Always logs |
| `duration < thresholdMs` | No log |
| `duration ≥ thresholdMs` | Logs |
| `enabled = false` | Skipped |

### 7.4 Configuration

```yaml
platform:
  performance:
    enabled: true
```

---

## 8. Observability Integration (`commons-logging-observability`)

### 8.1 Purpose

Acts as a **bridge** between audit/performance signals and observability systems.

- Does **not** provide exporters
- Does **not** force Micrometer or OTEL
- Fully optional

### 8.2 Enablement

```yaml
platform:
  observability:
    enabled: true
```

### 8.3 Client Responsibilities

Client applications must provide:

- Micrometer implementation
- Optional exporters (Prometheus / OTLP)

Example dependencies:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## 9. Logging Enforcement (`commons-logging-enforcement`)

### 9.1 Purpose

Provides **optional governance** at the logging backend level.

Examples:

- Enforce mandatory MDC fields
- Drop non-compliant logs
- Prevent anonymous production logs

### 9.2 How It Is Enabled

This module is **NOT auto-enabled**.

It must be explicitly referenced in `logback-spring.xml`.

```xml
<turboFilter
    class="com.arch.northstar.platform.logging.enforcement.MandatoryMdcFilter"/>
```

This ensures enforcement is:

- Explicit
- Environment-specific
- Client-controlled

---

## 10. Logback Configuration (Client Responsibility)

Example console configuration:

```xml
<pattern>
    %d{yyyy-MM-dd HH:mm:ss}
    [%X{correlationId}]
    [%X{tenantId}]
    [%X{userId}]
    %-5level %logger - %msg%n
</pattern>
```

The platform **does not ship a default logback file** to avoid overriding client preferences.

---

## 11. Supported Application Types

| Application Type | Supported |
|-----------------|-----------|
| Spring Boot Web (MVC) | ✅ Yes |
| Spring Boot Non-Web | ✅ Yes |
| Batch / CLI | ✅ Yes |
| Kafka Consumers | ✅ Yes |
| Async Executors | ✅ Yes |

---

## 12. What the Platform Does NOT Do (By Design)

- Does not enforce logging formats
- Does not require Spring Security
- Does not generate metrics exporters
- Does not override client logging configs
- Does not auto-enable governance

---

## 13. Recommended Usage Pattern

1. Import the BOM
2. Add required modules
3. Enable features via configuration
4. Configure logging backend
5. Use annotations where needed

---

## 14. Architectural Guarantees

- No circular dependencies between modules
- Context → Audit → Performance → Observability is one-way
- All advanced features are opt-in
- Safe defaults for production

---

## 15. Support & Extension

The framework is designed to be extended for:

- Kafka audit publishers
- Database audit persistence
- Custom MDC enrichment
- Additional observability backends

---

## 16. Summary

The **Northstar Platform – Core Framework** provides a **scalable, modular, and enterprise-ready** foundation for logging, auditing, performance measurement, and observability — without sacrificing flexibility or developer productivity.
