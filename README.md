# Levitas Validators

[![Maven Central](https://img.shields.io/maven-central/v/io.github.levitasorg/levitas-validators.svg)](https://central.sonatype.com/artifact/io.github.levitasorg/levitas-validators)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CI](https://github.com/levitasOrg/levitas-validators/actions/workflows/ci.yml/badge.svg)](https://github.com/levitasOrg/levitas-validators/actions)

Fast, zero-dependency Java validators for Indian identifiers — GSTIN, PAN, HSN, IFSC, UPI VPA, Aadhaar, and more. Pure offline validation with checksum verification, entity type extraction, and DPDP-compliant masking.

Built for every backend team that's tired of copy-pasting regex from Stack Overflow.

## Why Levitas?

Every Indian backend developer has written (badly) some version of:

```java
if (!gstin.matches("[0-9]{2}[A-Z]{5}...")) throw ...
```

That regex doesn't verify the checksum. It doesn't tell you the state. It doesn't extract the PAN. It doesn't decode the entity type. And you've written the same thing for PAN, IFSC, HSN codes, and UPI VPAs.

Levitas gives you all of that in one small, well-tested library.

## Features

- **GSTIN** — format, checksum, state extraction, PAN extraction, entity type
- **PAN** — format validation, entity type decoding (individual, company, HUF, etc.)
- **HSN codes** — 4/6/8 digit validation with turnover-based rules
- **IFSC** — format validation with bank name lookup
- **UPI VPA** — format validation with PSP detection (GPay, PhonePe, Paytm, etc.)
- **Aadhaar** — Verhoeff checksum + DPDP-compliant masking
- **Bank account masking** — log-safe PII helpers
- Pure offline — no network calls, no credentials, no API keys
- Minimal dependencies (just Jackson for bundled lookups)
- Thread-safe and immutable
- Full Javadoc

## Installation

**Maven:**

```xml
<dependency>
    <groupId>io.github.levitasorg</groupId>
    <artifactId>levitas-validators</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Gradle:**

```gradle
implementation 'io.github.levitasorg:levitas-validators:0.1.0'
```

Requires Java 17+.

## Quick start

### GSTIN

```java
import io.github.levitasorg.validators.gstin.GstinValidator;
import io.github.levitasorg.validators.gstin.GstinInfo;

boolean valid = GstinValidator.isValid("29AAACB1234C1ZB");

GstinInfo info = GstinValidator.parse("29AAACB1234C1ZB");
info.getStateCode();              // StateCode.KARNATAKA
info.getStateCode().getDisplayName(); // "Karnataka"
info.getPan();                    // "AAACB1234C"
info.getEntityCode();             // "1"
```

### PAN

```java
import io.github.levitasorg.validators.pan.PanValidator;

PanValidator.isValid("AAACB1234C");              // true
PanValidator.getEntityType("AAACB1234C");        // Optional[COMPANY]
PanValidator.isIndividual("ABCPD1234F");         // true
```

### HSN codes

```java
import io.github.levitasorg.validators.hsn.HsnValidator;
import java.math.BigDecimal;

HsnValidator.isValid("8471");                                          // true
HsnValidator.getRequiredDigits(new BigDecimal("10000000"));            // 4
HsnValidator.getRequiredDigits(new BigDecimal("100000000"));           // 6
HsnValidator.isValidForTurnover("8471", new BigDecimal("100000000"));  // false
```

### IFSC

```java
import io.github.levitasorg.validators.ifsc.IfscValidator;

IfscValidator.isValid("HDFC0001234");          // true
IfscValidator.getBankName("HDFC0001234");      // Optional["HDFC Bank"]
```

### UPI VPA

```java
import io.github.levitasorg.validators.upi.VpaValidator;

VpaValidator.isValid("user@okhdfcbank");       // true
VpaValidator.getPspName("user@okhdfcbank");    // Optional["Google Pay (HDFC)"]
```

### Aadhaar (DPDP-safe)

```java
import io.github.levitasorg.validators.aadhaar.AadhaarValidator;
import io.github.levitasorg.validators.aadhaar.AadhaarMasker;

AadhaarValidator.isValid("234123412346");   // checksum verified
AadhaarMasker.mask("234123412346");          // "XXXX-XXXX-2346"
```

**Never log raw Aadhaar numbers.** Always mask first. The DPDP Act makes storage and logging of unmasked Aadhaar a compliance risk.

## Why "Levitas"?

Latin for "lightness." Indian enterprise integration is heavy — compliance, validation, retries, more compliance. We lift that weight off your codebase so you can focus on what your product actually does.

## Roadmap

- [x] v0.1: Core validators (GSTIN, PAN, HSN, IFSC, UPI, Aadhaar)
- [ ] v0.2: Spring Boot starter + Bean Validation annotations (`@ValidGstin`, etc.)
- [ ] v0.3: Kotlin extensions
- [ ] v0.4: Expanded DPDP compliance toolkit
- [ ] v1.0: API stability guarantee
- [ ] Future: `levitas-gstn` (GSTN API client), `levitas-aa` (Account Aggregator), `levitas-digilocker`

## Contributing

Contributions welcome! Good first issues are tagged on GitHub. If you find an incorrect bank code, missing UPI PSP, or an edge case in any validator, please open an issue or PR. See CONTRIBUTING.md.

## License

Apache License 2.0 — use it anywhere, including commercial products.

## Disclaimer

This library performs format and checksum validation only. It does NOT verify whether an identifier is actually registered with GSTN, NPCI, UIDAI, or any other authority. For live verification, use the respective official APIs. Authors are not liable for any business decisions made based on validation results.
