package io.github.levitasorg.validators.gstin;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validator for Indian GST Identification Numbers (GSTINs).
 *
 * <p>A GSTIN is a 15-character alphanumeric identifier assigned to every GST-registered
 * taxpayer in India. It encodes the state code, PAN, registration count, and a checksum.
 *
 * <p>This class is a stateless utility — all methods are static and thread-safe.
 *
 * <p>Format: {@code ^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$}
 *
 * <p>Example usage:
 * <pre>{@code
 * boolean valid = GstinValidator.isValid("29AAACB1234C1ZX");
 * GstinInfo info = GstinValidator.parse("29AAACB1234C1ZX");
 * info.getStateCode().getDisplayName(); // "Karnataka"
 * }</pre>
 */
public final class GstinValidator {

    private GstinValidator() {
        // utility class
    }

    /** The length of a valid GSTIN. */
    private static final int GSTIN_LENGTH = 15;

    /** Base-36 alphabet used for GSTIN checksum computation. */
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** Compiled GSTIN format pattern (does not verify checksum). */
    private static final Pattern GSTIN_PATTERN =
            Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$");

    /**
     * Returns {@code true} if the given GSTIN has a valid format and a correct checksum.
     *
     * <p>This method is null-safe — passing {@code null} returns {@code false}.
     *
     * @param gstin the GSTIN to validate; may be null
     * @return {@code true} if the GSTIN is valid, {@code false} otherwise
     */
    public static boolean isValid(String gstin) {
        if (gstin == null || gstin.length() != GSTIN_LENGTH) {
            return false;
        }
        if (!GSTIN_PATTERN.matcher(gstin).matches()) {
            return false;
        }
        // Validate state code
        String stateCodeStr = gstin.substring(0, 2);
        if (StateCode.fromCode(stateCodeStr).isEmpty()) {
            return false;
        }
        // Validate checksum
        char expectedChecksum = computeChecksum(gstin.substring(0, 14));
        return gstin.charAt(14) == expectedChecksum;
    }

    /**
     * Parses a valid GSTIN into its component parts.
     *
     * <p>Validates format and checksum before parsing. Throws {@link IllegalArgumentException}
     * if the input is invalid.
     *
     * @param gstin the GSTIN to parse; must be non-null and valid
     * @return a {@link GstinInfo} with all parsed fields
     * @throws IllegalArgumentException if {@code gstin} is null or invalid
     */
    public static GstinInfo parse(String gstin) {
        if (gstin == null) {
            throw new IllegalArgumentException("GSTIN must not be null");
        }
        if (!isValid(gstin)) {
            throw new IllegalArgumentException("Invalid GSTIN: '" + gstin + "'");
        }
        StateCode stateCode = StateCode.fromCode(gstin.substring(0, 2))
                .orElseThrow(() -> new IllegalArgumentException("Invalid state code in GSTIN: '" + gstin + "'"));
        String pan = gstin.substring(2, 12);
        String entityCode = gstin.substring(12, 13);
        EntityType entityType = EntityType.REGULAR;
        char checksumChar = gstin.charAt(14);
        return new GstinInfo(gstin, stateCode, pan, entityCode, entityType, checksumChar);
    }

    /**
     * Extracts the state code from a GSTIN without performing full validation.
     *
     * <p>This method only checks that the string is long enough (at least 2 characters)
     * and that the first 2 characters match a known state code.
     *
     * @param gstin the GSTIN string; may be null
     * @return an {@link Optional} containing the {@link StateCode}, or empty if extraction fails
     */
    public static Optional<StateCode> extractStateCode(String gstin) {
        if (gstin == null || gstin.length() < 2) {
            return Optional.empty();
        }
        return StateCode.fromCode(gstin.substring(0, 2));
    }

    /**
     * Extracts the embedded PAN from a GSTIN without performing full validation.
     *
     * <p>This method only checks that the string is long enough (at least 12 characters).
     * No format or checksum validation is performed.
     *
     * @param gstin the GSTIN string; may be null
     * @return an {@link Optional} containing the 10-character PAN, or empty if extraction fails
     */
    public static Optional<String> extractPan(String gstin) {
        if (gstin == null || gstin.length() < 12) {
            return Optional.empty();
        }
        return Optional.of(gstin.substring(2, 12));
    }

    /**
     * Validates only the format of a GSTIN using the regex pattern, skipping checksum verification.
     *
     * <p>This is faster than {@link #isValid(String)} when processing data from a trusted source
     * where checksums are guaranteed correct.
     *
     * @param gstin the GSTIN to check; may be null
     * @return {@code true} if the format matches the GSTIN regex pattern, {@code false} otherwise
     */
    public static boolean isValidFormat(String gstin) {
        if (gstin == null || gstin.length() != GSTIN_LENGTH) {
            return false;
        }
        if (!GSTIN_PATTERN.matcher(gstin).matches()) {
            return false;
        }
        return StateCode.fromCode(gstin.substring(0, 2)).isPresent();
    }

    /**
     * Computes the expected GSTIN checksum character for the first 14 characters.
     *
     * <p>The checksum algorithm uses base-36 arithmetic over the alphabet
     * {@code 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ}. Odd-indexed positions (0-based)
     * are weighted by 2; even-indexed positions by 1.
     *
     * @param first14Chars the first 14 characters of the GSTIN; must be non-null and exactly 14 chars
     * @return the computed checksum character
     * @throws IllegalArgumentException if input is null or not exactly 14 characters
     */
    public static char computeChecksum(String first14Chars) {
        if (first14Chars == null || first14Chars.length() != 14) {
            throw new IllegalArgumentException(
                    "Input must be exactly 14 characters, got: "
                    + (first14Chars == null ? "null" : first14Chars.length()));
        }
        int sum = 0;
        for (int i = 0; i < 14; i++) {
            int digit = ALPHABET.indexOf(first14Chars.charAt(i));
            if (digit == -1) {
                throw new IllegalArgumentException(
                        "Character '" + first14Chars.charAt(i) + "' at index " + i
                        + " is not in the GSTIN alphabet");
            }
            int factor = (i % 2 == 0) ? 1 : 2;
            int product = digit * factor;
            sum += (product / 36) + (product % 36);
        }
        int checksumValue = (36 - (sum % 36)) % 36;
        return ALPHABET.charAt(checksumValue);
    }
}
