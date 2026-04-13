package io.github.levitasorg.validators.gstin;

import java.util.Objects;

/**
 * Immutable value object holding the parsed components of a valid GSTIN.
 *
 * <p>A GSTIN is 15 characters long and encodes:
 * <ul>
 *   <li>Characters 1–2: State code</li>
 *   <li>Characters 3–12: PAN (10 characters)</li>
 *   <li>Character 13: Entity / registration count code</li>
 *   <li>Character 14: Always 'Z' (reserved)</li>
 *   <li>Character 15: Checksum character</li>
 * </ul>
 *
 * <p>Instances are created exclusively by {@link GstinValidator#parse(String)}.
 */
public final class GstinInfo {

    private final String gstin;
    private final StateCode stateCode;
    private final String pan;
    private final String entityCode;
    private final EntityType entityType;
    private final char checksumChar;

    /**
     * Package-private constructor. Use {@link GstinValidator#parse(String)} to obtain instances.
     *
     * @param gstin        the full 15-character GSTIN
     * @param stateCode    the state/UT code extracted from characters 1–2
     * @param pan          the PAN extracted from characters 3–12
     * @param entityCode   the entity/registration count code (character 13)
     * @param entityType   the entity type (currently always {@link EntityType#REGULAR})
     * @param checksumChar the checksum character (character 15)
     */
    GstinInfo(String gstin, StateCode stateCode, String pan,
              String entityCode, EntityType entityType, char checksumChar) {
        this.gstin = gstin;
        this.stateCode = stateCode;
        this.pan = pan;
        this.entityCode = entityCode;
        this.entityType = entityType;
        this.checksumChar = checksumChar;
    }

    /**
     * Returns the full 15-character GSTIN.
     *
     * @return the GSTIN string
     */
    public String getGstin() {
        return gstin;
    }

    /**
     * Returns the state or union territory associated with this GSTIN.
     *
     * @return the {@link StateCode}
     */
    public StateCode getStateCode() {
        return stateCode;
    }

    /**
     * Returns the 10-character PAN embedded in the GSTIN (characters 3–12).
     *
     * @return the PAN string
     */
    public String getPan() {
        return pan;
    }

    /**
     * Returns the entity/registration count code — the 13th character of the GSTIN.
     *
     * <p>Values '1'–'9' represent the 1st through 9th GST registration for the same
     * PAN in the same state; 'A'–'Z' represent the 10th onwards.
     *
     * @return single-character string representing the entity code
     */
    public String getEntityCode() {
        return entityCode;
    }

    /**
     * Returns the entity type for this GSTIN.
     *
     * <p>Currently always {@link EntityType#REGULAR}.
     *
     * @return the {@link EntityType}
     */
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * Returns the checksum character (15th character of the GSTIN).
     *
     * @return the checksum character
     */
    public char getChecksumChar() {
        return checksumChar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GstinInfo)) return false;
        GstinInfo other = (GstinInfo) o;
        return checksumChar == other.checksumChar
                && Objects.equals(gstin, other.gstin)
                && stateCode == other.stateCode
                && Objects.equals(pan, other.pan)
                && Objects.equals(entityCode, other.entityCode)
                && entityType == other.entityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gstin, stateCode, pan, entityCode, entityType, checksumChar);
    }

    @Override
    public String toString() {
        return "GstinInfo{"
                + "gstin='" + gstin + '\''
                + ", stateCode=" + stateCode
                + ", pan='" + pan + '\''
                + ", entityCode='" + entityCode + '\''
                + ", entityType=" + entityType
                + ", checksumChar=" + checksumChar
                + '}';
    }
}
