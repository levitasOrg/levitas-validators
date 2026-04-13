package io.github.levitasorg.validators.upi;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validator for UPI Virtual Payment Addresses (VPAs), commonly called UPI IDs.
 *
 * <p>A VPA has the format {@code username@handle} where:
 * <ul>
 *   <li>Username: 2–256 characters; alphanumeric, dots, hyphens, and underscores</li>
 *   <li>Handle: starts with a letter, 2–65 characters total, alphanumeric only</li>
 * </ul>
 *
 * <p>Pattern: {@code ^[a-zA-Z0-9.\-_]{2,256}@[a-zA-Z][a-zA-Z0-9]{1,64}$}
 *
 * <p>UPI handles are case-insensitive for PSP lookup purposes — they are normalised to
 * lowercase before looking up in the {@link PspRegistry}.
 *
 * <p>All methods are null-safe and thread-safe.
 *
 * <p>Example usage:
 * <pre>{@code
 * VpaValidator.isValid("user@okhdfcbank");        // true
 * VpaValidator.getPspName("user@okhdfcbank");     // Optional["Google Pay (HDFC)"]
 * VpaValidator.getHandle("user@okhdfcbank");      // Optional["okhdfcbank"]
 * }</pre>
 */
public final class VpaValidator {

    private VpaValidator() {
        // utility class
    }

    /**
     * VPA format pattern.
     * Username: 2–256 chars of [a-zA-Z0-9.\-_].
     * Handle: starts with a letter, then 1–64 alphanumeric chars (total 2–65).
     */
    private static final Pattern VPA_PATTERN =
            Pattern.compile("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z][a-zA-Z0-9]{1,64}$");

    /**
     * Returns {@code true} if the given string is a syntactically valid UPI VPA.
     *
     * <p>This method validates the format only. It does not verify whether the VPA is
     * registered with NPCI. This method is null-safe.
     *
     * @param vpa the VPA to validate; may be null
     * @return {@code true} if the VPA matches the expected format
     */
    public static boolean isValid(String vpa) {
        if (vpa == null) {
            return false;
        }
        return VPA_PATTERN.matcher(vpa).matches();
    }

    /**
     * Extracts the username part (before the '@') from a VPA.
     *
     * @param vpa the VPA string; may be null
     * @return an {@link Optional} containing the username, or empty if the VPA is null
     *         or contains no '@' character
     */
    public static Optional<String> getUsername(String vpa) {
        if (vpa == null) {
            return Optional.empty();
        }
        int atIndex = vpa.indexOf('@');
        if (atIndex < 0) {
            return Optional.empty();
        }
        return Optional.of(vpa.substring(0, atIndex));
    }

    /**
     * Extracts the handle part (after the '@') from a VPA.
     *
     * @param vpa the VPA string; may be null
     * @return an {@link Optional} containing the handle in its original case,
     *         or empty if the VPA is null or contains no '@' character
     */
    public static Optional<String> getHandle(String vpa) {
        if (vpa == null) {
            return Optional.empty();
        }
        int atIndex = vpa.indexOf('@');
        if (atIndex < 0 || atIndex == vpa.length() - 1) {
            return Optional.empty();
        }
        return Optional.of(vpa.substring(atIndex + 1));
    }

    /**
     * Returns the PSP (Payment Service Provider) name for the given VPA.
     *
     * <p>The handle portion is extracted and looked up in the bundled {@link PspRegistry}.
     * The lookup is case-insensitive.
     *
     * @param vpa the VPA string; may be null
     * @return an {@link Optional} containing the PSP name, or empty if the handle is not
     *         recognised or the VPA is invalid
     */
    public static Optional<String> getPspName(String vpa) {
        return getHandle(vpa).flatMap(PspRegistry::getPspName);
    }
}
