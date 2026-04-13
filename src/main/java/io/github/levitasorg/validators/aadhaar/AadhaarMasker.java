package io.github.levitasorg.validators.aadhaar;

/**
 * DPDP-compliant masking utility for Aadhaar numbers.
 *
 * <p>Provides methods to mask Aadhaar numbers before logging or displaying,
 * in compliance with the Digital Personal Data Protection (DPDP) Act and
 * UIDAI guidelines.
 *
 * <p><strong>Always mask Aadhaar numbers before logging or including in
 * audit trails.</strong> Never log raw Aadhaar numbers.
 *
 * <p>Example usage:
 * <pre>{@code
 * AadhaarMasker.mask("234123412346");        // "XXXX-XXXX-2346"
 * AadhaarMasker.maskCompact("234123412346"); // "XXXXXXXX2346"
 * }</pre>
 */
public final class AadhaarMasker {

    private AadhaarMasker() {
        // utility class
    }

    private static final String MASK_CHAR = "X";

    /**
     * Masks an Aadhaar number, showing only the last 4 digits in hyphen-separated groups.
     *
     * <p>Returns {@code "XXXX-XXXX-NNNN"} where {@code NNNN} are the last 4 digits.
     * Separators in the input (spaces or hyphens) are stripped before masking.
     *
     * @param aadhaar the 12-digit Aadhaar number (with or without separators); must not be null
     * @return the masked Aadhaar in {@code "XXXX-XXXX-NNNN"} format
     * @throws IllegalArgumentException if the input is null or not 12 digits (after stripping separators)
     */
    public static String mask(String aadhaar) {
        String digits = requireDigits(aadhaar);
        return "XXXX-XXXX-" + digits.substring(8);
    }

    /**
     * Masks an Aadhaar number without separators, showing only the last 4 digits.
     *
     * <p>Returns {@code "XXXXXXXXNNNN"} where {@code NNNN} are the last 4 digits.
     * Separators in the input (spaces or hyphens) are stripped before masking.
     *
     * @param aadhaar the 12-digit Aadhaar number (with or without separators); must not be null
     * @return the masked Aadhaar in {@code "XXXXXXXXNNNN"} format (no separators)
     * @throws IllegalArgumentException if the input is null or not 12 digits (after stripping separators)
     */
    public static String maskCompact(String aadhaar) {
        String digits = requireDigits(aadhaar);
        return "XXXXXXXX" + digits.substring(8);
    }

    /**
     * Validates and normalises the Aadhaar input, returning the raw 12-digit string.
     *
     * @throws IllegalArgumentException if null or not exactly 12 digits after stripping separators
     */
    private static String requireDigits(String aadhaar) {
        if (aadhaar == null) {
            throw new IllegalArgumentException("Aadhaar must not be null");
        }
        String normalised = AadhaarValidator.normalise(aadhaar);
        if (normalised == null) {
            throw new IllegalArgumentException(
                    "Aadhaar must be exactly 12 digits (after stripping spaces/hyphens)");
        }
        // Verify all characters are digits
        for (char c : normalised.toCharArray()) {
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException(
                        "Aadhaar must contain only digits (and optional space/hyphen separators)");
            }
        }
        return normalised;
    }
}
