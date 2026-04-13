# Security Policy

## Supported Versions

Only the latest release receives security fixes.

| Version | Supported |
|---------|-----------|
| 0.1.x   | Yes       |

## Scope

Levitas Validators is a **pure offline validation library**. It performs no network calls, stores no data, and holds no credentials or secrets. The attack surface is limited to:

- Malformed or adversarial input strings passed to validator methods
- Bundled JSON resources (`ifsc-banks.json`, `upi-handles.json`) loaded at class-init time

It is **out of scope** to report that a validator returns `true` for an identifier that is not registered with GSTN, NPCI, UIDAI, or any other authority. The library validates format and checksum only — live registration checks require official APIs.

## Reporting a Vulnerability

If you discover a security vulnerability (e.g., a ReDoS via crafted regex input, a path-traversal in resource loading, or incorrect Aadhaar/GSTIN checksum logic that leaks information), please **do not open a public GitHub issue**.

Report privately via GitHub's Security Advisory feature:

1. Go to the repository on GitHub
2. Click **Security** → **Advisories** → **Report a vulnerability**
3. Describe the issue, steps to reproduce, and impact

You can expect an acknowledgement within **5 business days** and a fix or mitigation plan within **30 days** for confirmed vulnerabilities.

## PII Handling Note

This library processes sensitive Indian identifiers (Aadhaar, PAN, GSTIN). If you find a code path that **logs, stores, or exposes raw identifier values** inside the library itself, that is a privacy bug and should also be reported privately above.
