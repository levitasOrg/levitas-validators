package io.github.levitasorg.validators.ifsc;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validator for Indian Financial System Codes (IFSCs).
 *
 * <p>An IFSC is an 11-character alphanumeric code used to identify bank branches
 * in the National Electronic Funds Transfer (NEFT) and Real Time Gross Settlement
 * (RTGS) systems.
 *
 * <p>Format: {@code ^[A-Z]{4}0[A-Z0-9]{6}$}
 * <ul>
 *   <li>Characters 1–4: Bank code (alphabetic, e.g. HDFC, SBIN)</li>
 *   <li>Character 5: Always '0' (reserved for future use)</li>
 *   <li>Characters 6–11: Branch code (alphanumeric)</li>
 * </ul>
 *
 * <p>There is no public checksum for IFSC codes.
 *
 * <p>All methods are null-safe and thread-safe.
 *
 * <p>Example usage:
 * <pre>{@code
 * IfscValidator.isValid("HDFC0001234");           // true
 * IfscValidator.getBankName("HDFC0001234");       // Optional["HDFC Bank"]
 * IfscValidator.getBranchCode("HDFC0001234");     // Optional["001234"]
 * }</pre>
 */
public final class IfscValidator {

    private IfscValidator() {
        // utility class
    }

    /** Expected length of a valid IFSC. */
    private static final int IFSC_LENGTH = 11;

    /** IFSC format pattern. */
    private static final Pattern IFSC_PATTERN =
            Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

    /**
     * Returns {@code true} if the given string is a valid IFSC code.
     *
     * <p>Validation checks format only; there is no public checksum for IFSC codes.
     * This method is null-safe.
     *
     * @param ifsc the IFSC to validate; may be null
     * @return {@code true} if the IFSC matches the expected format
     */
    public static boolean isValid(String ifsc) {
        if (ifsc == null || ifsc.length() != IFSC_LENGTH) {
            return false;
        }
        return IFSC_PATTERN.matcher(ifsc).matches();
    }

    /**
     * Extracts the 4-character bank code from an IFSC.
     *
     * <p>This method does not perform full IFSC validation; it only checks length.
     *
     * @param ifsc the IFSC string; may be null
     * @return an {@link Optional} containing the bank code, or empty if the IFSC is too short
     */
    public static Optional<String> getBankCode(String ifsc) {
        if (ifsc == null || ifsc.length() < 4) {
            return Optional.empty();
        }
        return Optional.of(ifsc.substring(0, 4));
    }

    /**
     * Returns the display name of the bank identified by the first 4 characters of the IFSC.
     *
     * <p>The name is looked up in the bundled bank registry. If the bank code is not in the
     * registry, an empty Optional is returned (the IFSC may still be valid).
     *
     * @param ifsc the IFSC string; may be null
     * @return an {@link Optional} containing the bank name, or empty if not found
     */
    public static Optional<String> getBankName(String ifsc) {
        return getBankCode(ifsc).flatMap(BankRegistry::getBankName);
    }

    /**
     * Extracts the 6-character branch code from an IFSC (characters 6–11).
     *
     * <p>This method does not perform full IFSC validation; it only checks length.
     *
     * @param ifsc the IFSC string; may be null
     * @return an {@link Optional} containing the branch code, or empty if the IFSC is too short
     */
    public static Optional<String> getBranchCode(String ifsc) {
        if (ifsc == null || ifsc.length() < 11) {
            return Optional.empty();
        }
        return Optional.of(ifsc.substring(5, 11));
    }
}
