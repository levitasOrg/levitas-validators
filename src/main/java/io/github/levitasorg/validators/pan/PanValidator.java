package io.github.levitasorg.validators.pan;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validator for Indian Permanent Account Numbers (PANs).
 *
 * <p>A PAN is a 10-character alphanumeric identifier issued by the Income Tax Department.
 * The format is {@code ^[A-Z]{5}[0-9]{4}[A-Z]$}. Note that there is no publicly verifiable
 * checksum — the 10th character is assigned by the IT department via an undocumented algorithm.
 * Validation here is format-only plus entity type extraction.
 *
 * <p>All methods are null-safe and thread-safe.
 *
 * <p>Example usage:
 * <pre>{@code
 * PanValidator.isValid("AAAPA1234A");              // true (individual)
 * PanValidator.getEntityType("AAAPA1234A");         // Optional[INDIVIDUAL]
 * PanValidator.isIndividual("AAAPA1234A");          // true
 * }</pre>
 */
public final class PanValidator {

    private PanValidator() {
        // utility class
    }

    /** Expected length of a valid PAN. */
    private static final int PAN_LENGTH = 10;

    /** PAN format pattern. */
    private static final Pattern PAN_PATTERN =
            Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");

    /**
     * Returns {@code true} if the given PAN has a valid format.
     *
     * <p>Validation is format-only; the 10th character is not checksummed.
     * This method is null-safe.
     *
     * @param pan the PAN to validate; may be null
     * @return {@code true} if the PAN matches the expected format
     */
    public static boolean isValid(String pan) {
        if (pan == null || pan.length() != PAN_LENGTH) {
            return false;
        }
        if (!PAN_PATTERN.matcher(pan).matches()) {
            return false;
        }
        // Verify the 4th character is a known entity type
        return PanEntityType.fromCharacter(pan.charAt(3)).isPresent();
    }

    /**
     * Returns the entity type encoded in the 4th character of the PAN.
     *
     * @param pan the PAN string; may be null
     * @return an {@link Optional} containing the {@link PanEntityType},
     *         or empty if the PAN is null, too short, or the entity char is not recognised
     */
    public static Optional<PanEntityType> getEntityType(String pan) {
        if (pan == null || pan.length() < 4) {
            return Optional.empty();
        }
        return PanEntityType.fromCharacter(pan.charAt(3));
    }

    /**
     * Returns {@code true} if the PAN belongs to an individual ({@code P} entity type).
     *
     * @param pan the PAN to check; may be null
     * @return {@code true} if valid and entity type is {@link PanEntityType#INDIVIDUAL}
     */
    public static boolean isIndividual(String pan) {
        return isValid(pan) && getEntityType(pan)
                .map(t -> t == PanEntityType.INDIVIDUAL)
                .orElse(false);
    }

    /**
     * Returns {@code true} if the PAN belongs to a company ({@code C} entity type).
     *
     * @param pan the PAN to check; may be null
     * @return {@code true} if valid and entity type is {@link PanEntityType#COMPANY}
     */
    public static boolean isCompany(String pan) {
        return isValid(pan) && getEntityType(pan)
                .map(t -> t == PanEntityType.COMPANY)
                .orElse(false);
    }

    /**
     * Returns {@code true} if the PAN belongs to a Hindu Undivided Family ({@code H} entity type).
     *
     * @param pan the PAN to check; may be null
     * @return {@code true} if valid and entity type is {@link PanEntityType#HINDU_UNDIVIDED_FAMILY}
     */
    public static boolean isHuf(String pan) {
        return isValid(pan) && getEntityType(pan)
                .map(t -> t == PanEntityType.HINDU_UNDIVIDED_FAMILY)
                .orElse(false);
    }

    /**
     * Returns {@code true} if the PAN belongs to a firm ({@code F} entity type).
     *
     * @param pan the PAN to check; may be null
     * @return {@code true} if valid and entity type is {@link PanEntityType#FIRM}
     */
    public static boolean isFirm(String pan) {
        return isValid(pan) && getEntityType(pan)
                .map(t -> t == PanEntityType.FIRM)
                .orElse(false);
    }

    /**
     * Returns {@code true} if the PAN belongs to a trust ({@code T} entity type).
     *
     * @param pan the PAN to check; may be null
     * @return {@code true} if valid and entity type is {@link PanEntityType#TRUST}
     */
    public static boolean isTrust(String pan) {
        return isValid(pan) && getEntityType(pan)
                .map(t -> t == PanEntityType.TRUST)
                .orElse(false);
    }

    /**
     * Returns {@code true} if the PAN belongs to a government entity ({@code G} entity type).
     *
     * @param pan the PAN to check; may be null
     * @return {@code true} if valid and entity type is {@link PanEntityType#GOVERNMENT}
     */
    public static boolean isGovernment(String pan) {
        return isValid(pan) && getEntityType(pan)
                .map(t -> t == PanEntityType.GOVERNMENT)
                .orElse(false);
    }
}
