package io.github.levitasorg.validators.pan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Entity types encoded in the 4th character of a Permanent Account Number (PAN).
 *
 * <p>The Income Tax Department of India assigns the 4th character to indicate the type of
 * taxpaying entity. For example, 'P' denotes an individual person and 'C' denotes a company.
 */
public enum PanEntityType {

    /** Individual person. */
    INDIVIDUAL('P', "Individual"),

    /** Company registered under the Companies Act. */
    COMPANY('C', "Company"),

    /** Hindu Undivided Family (HUF). */
    HINDU_UNDIVIDED_FAMILY('H', "Hindu Undivided Family"),

    /** Firm (partnership firm). */
    FIRM('F', "Firm"),

    /** Association of Persons (AOP). */
    ASSOCIATION_OF_PERSONS('A', "Association of Persons"),

    /** Trust. */
    TRUST('T', "Trust"),

    /** Body of Individuals (BOI). */
    BODY_OF_INDIVIDUALS('B', "Body of Individuals"),

    /** Local Authority (municipality, gram panchayat, etc.). */
    LOCAL_AUTHORITY('L', "Local Authority"),

    /** Artificial Juridical Person. */
    ARTIFICIAL_JURIDICAL_PERSON('J', "Artificial Juridical Person"),

    /** Government entity. */
    GOVERNMENT('G', "Government");

    private final char code;
    private final String description;

    private static final Map<Character, PanEntityType> CODE_MAP;

    static {
        Map<Character, PanEntityType> map = new HashMap<>();
        for (PanEntityType type : values()) {
            map.put(type.code, type);
        }
        CODE_MAP = Collections.unmodifiableMap(map);
    }

    PanEntityType(char code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Returns the single uppercase character that encodes this entity type in the PAN.
     *
     * @return the entity type character (e.g. 'P' for individual)
     */
    public char getCode() {
        return code;
    }

    /**
     * Returns a human-readable description of this entity type.
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Looks up the entity type for the given PAN character (4th character of the PAN).
     *
     * @param character the 4th character of a PAN
     * @return an {@link Optional} containing the matching {@link PanEntityType},
     *         or empty if the character does not correspond to a known entity type
     */
    public static Optional<PanEntityType> fromCharacter(char character) {
        return Optional.ofNullable(CODE_MAP.get(character));
    }
}
