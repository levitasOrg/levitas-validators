package io.github.levitasorg.validators.gstin;

/**
 * Represents the entity type implied by the 13th character of a GSTIN.
 *
 * <p>The 13th character (index 12) of a GSTIN indicates the registration count for the
 * same PAN in the same state. Characters '1'–'9' represent the 1st through 9th
 * registration, and 'A'–'Z' represent the 10th onwards.
 *
 * <p>At present, there is only one functional entity type — {@link #REGULAR}. This enum
 * is provided for future extensibility in case the GST specification introduces distinct
 * entity classifications (e.g., casual taxable person, non-resident taxable person, etc.)
 * using this position.
 *
 * <p>Character 14 (index 13) is always 'Z' and is reserved for future use by GSTN.
 */
public enum EntityType {

    /**
     * Regular GST registration. Currently the only type.
     * The 13th GSTIN character ('1'–'9' or 'A'–'Z') identifies the ordinal registration
     * for that PAN+state combination, not a distinct entity category.
     */
    REGULAR;
}
