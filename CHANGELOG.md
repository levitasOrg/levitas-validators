# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] — 2024-04-13

### Added

- **GSTIN validator** (`GstinValidator`) — format validation, base-36 checksum verification,
  state code extraction, PAN extraction, full parse into `GstinInfo`
- **StateCode enum** — all 40 Indian state/UT GST codes with display names and
  `fromCode(String)` lookup
- **EntityType enum** — REGULAR entity type for GSTIN registration count character
- **PAN validator** (`PanValidator`) — format validation, entity type extraction
  (individual, company, HUF, firm, trust, government, AOP, BOI, local authority,
  artificial juridical person)
- **PanEntityType enum** — all 10 IT department entity type codes with descriptions
- **HSN validator** (`HsnValidator`) — 2/4/6/8 digit validation, turnover-based
  minimum digit requirements (4 digits ≤ ₹5 crore, 6 digits > ₹5 crore),
  export/import 8-digit check
- **IFSC validator** (`IfscValidator`) — format validation, bank code extraction,
  bank name lookup, branch code extraction
- **BankRegistry** — 46 major Indian banks bundled as JSON resource, loaded via Jackson
- **UPI VPA validator** (`VpaValidator`) — format validation, username/handle extraction,
  PSP name lookup
- **PspRegistry** — 36 known UPI PSP handles bundled as JSON resource
- **Aadhaar validator** (`AadhaarValidator`) — Verhoeff checksum validation, format check,
  space/hyphen separator support
- **AadhaarMasker** — DPDP-compliant masking (`XXXX-XXXX-NNNN` and compact formats)
- **VerhoeffChecksum** (internal) — full Verhoeff algorithm implementation with
  validate and computeChecksum methods
- **BankAccountMasker** — full-mask and partial-mask helpers for bank account numbers
- **Spring Boot demo** (`examples/spring-boot-demo/`) — REST API demo for all validators
- **GitHub Actions CI** — builds and tests on Java 17 and 21, uploads JaCoCo coverage

[Unreleased]: https://github.com/levitasOrg/levitas-validators/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/levitasOrg/levitas-validators/releases/tag/v0.1.0
