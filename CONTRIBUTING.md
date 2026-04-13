# Contributing to Levitas Validators

Thank you for considering a contribution! This document covers how to build the project, add a new validator, our code style, and the PR checklist.

## Building

```bash
# Full build with tests and coverage check (requires Java 17+, Maven 3.9+)
mvn verify

# Skip tests for a quick compile check
mvn verify -DskipTests

# Run only tests
mvn test
```

The JaCoCo coverage check is enforced at 85% line coverage. New code should aim for 90%+.

## Adding a new validator

1. Create a new package under `src/main/java/io/github/levitasorg/validators/` (e.g., `tan/`).
2. Implement the validator class as a final utility class (private constructor, static methods).
3. Add Javadoc on every public method. At minimum: what it validates, what it returns, and null-safety contract.
4. Add a corresponding test class under `src/test/java/...` mirroring the main package.
5. Add tests for: null input, empty input, wrong length, invalid format, valid examples, and edge cases.
6. If your validator needs bundled data (e.g., a lookup table), add a JSON resource under `src/main/resources/levitas/` and load it via Jackson in a registry class.
7. Run `mvn verify` — all tests must pass and coverage must stay above 85%.

## Code style

- Java 17, no Lombok
- Immutable where possible; no mutable static state after initialization
- All public classes and methods must have Javadoc
- Static factory methods preferred over constructors for utility-like output types
- No hardcoded real Aadhaar, PAN, GSTIN, or other PII in tests — use generated or obviously-fake test data
- No network calls anywhere in the library
- All validators must be null-safe: `isValid(null)` returns `false`, `parse(null)` throws `IllegalArgumentException` with a clear message, `extractX(null)` returns `Optional.empty()`

## Test requirements

- 90%+ line coverage for new code
- Each new public method needs at least one positive and one negative test
- Null-safety tests for all public methods
- Thread-safety tested for validators that will be called in concurrent contexts

## PR checklist

Before opening a pull request, verify:

- [ ] `mvn verify` passes locally (tests + coverage)
- [ ] All new public methods have Javadoc
- [ ] No real PII in test data
- [ ] No network calls introduced
- [ ] Code follows existing style (immutability, null safety, static utility pattern)
- [ ] CHANGELOG.md updated with the change under `[Unreleased]`

## Issue tracker

Issues and feature requests: [GitHub Issues](https://github.com/levitasOrg/levitas-validators/issues)
