package io.github.levitasorg.validators.aadhaar;

import io.github.levitasorg.validators.internal.VerhoeffChecksum;

import java.util.regex.Pattern;

/**
 * Validator for Indian Aadhaar numbers.
 *
 * <p>An Aadhaar number is a 12-digit unique identification number issued by the Unique
 * Identification Authority of India (UIDAI). The last digit is a Verhoeff checksum over
 * the first 11 digits.
 *
 * <p>Format rules:
 * <ul>
 *   <li>Exactly 12 digits</li>
 *   <li>First digit must be 2–9 (0 and 1 are never assigned)</li>
 *   <li>Last digit is a Verhoeff checksum</li>
 * </ul>
 *
 * <p>This validator also accepts Aadhaar numbers with space or hyphen separators
 * (e.g., {@code "2341 2341 2346"} or {@code "2341-2341-2346"}).
 * Separators are stripped before validation.
 *
 * <p><strong>WARNING:</strong> Never log raw Aadhaar numbers. Use
 * {@link AadhaarMasker} before logging. The DPDP Act (Digital Personal Data Protection Act)
 * makes storage and logging of unmasked Aadhaar a compliance risk.
 *
 * <p>All methods are null-safe and thread-safe.
 *
 * <p>Example usage:
 * <pre>{@code
 * AadhaarValidator.isValid("234123412346");   // checksum verified
 * AadhaarValidator.isValid("2341 2341 2346"); // separators accepted
 * }</pre>
 */
public final class AadhaarValidator {

    private AadhaarValidator() {
        // utility class
    }

    /** Expected length of a normalised (separator-stripped) Aadhaar number. */
    private static final int AADHAAR_LENGTH = 12;

    /** Pattern that matches valid first digit (2–9). */
    private static final Pattern AADHAAR_FORMAT_PATTERN =
            Pattern.compile("^[2-9][0-9]{11}$");

    /**
     * Pattern for stripping allowed separators: ASCII space (U+0020) and hyphen-minus (U+002D).
     * Intentionally NOT using \s — tabs and newlines are not valid Aadhaar separators.
     */
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile("[ -]");

    /**
     * Validates an Aadhaar number including the Verhoeff checksum.
     *
     * <p>Accepts numbers with or without space/hyphen separators.
     * This method is null-safe — {@code null} returns {@code false}.
     *
     * @param aadhaar the Aadhaar number to validate; may be null
     * @return {@code true} if the Aadhaar number has valid format and correct checksum
     */
    public static boolean isValid(String aadhaar) {
        String normalised = normalise(aadhaar);
        if (normalised == null) {
            return false;
        }
        if (!AADHAAR_FORMAT_PATTERN.matcher(normalised).matches()) {
            return false;
        }
        return VerhoeffChecksum.validate(normalised);
    }

    /**
     * Validates only the format of an Aadhaar number without verifying the checksum.
     *
     * <p>Checks that the number is 12 digits and the first digit is 2–9.
     * Accepts numbers with or without space/hyphen separators.
     *
     * @param aadhaar the Aadhaar number to check; may be null
     * @return {@code true} if the format is valid (12 digits, first digit 2–9)
     */
    public static boolean isValidFormat(String aadhaar) {
        String normalised = normalise(aadhaar);
        if (normalised == null) {
            return false;
        }
        return AADHAAR_FORMAT_PATTERN.matcher(normalised).matches();
    }

    /**
     * Strips allowed separators (spaces and hyphens) and returns the raw digit string.
     * Returns {@code null} if input is null or the normalised result is not exactly 12 digits.
     */
    static String normalise(String aadhaar) {
        if (aadhaar == null) {
            return null;
        }
        String stripped = SEPARATOR_PATTERN.matcher(aadhaar).replaceAll("");
        if (stripped.length() != AADHAAR_LENGTH) {
            return null;
        }
        return stripped;
    }
}
