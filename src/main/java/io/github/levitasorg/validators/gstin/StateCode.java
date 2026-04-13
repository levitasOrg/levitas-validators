package io.github.levitasorg.validators.gstin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Enumeration of all Indian state and union territory codes used in GST registration.
 *
 * <p>Codes follow the official GST state code list as mandated by the Central Board of
 * Indirect Taxes and Customs (CBIC).
 *
 * <p>Usage:
 * <pre>{@code
 * Optional<StateCode> state = StateCode.fromCode("29");
 * state.map(StateCode::getDisplayName); // "Karnataka"
 * }</pre>
 */
public enum StateCode {

    /** Jammu and Kashmir (GST code 01). */
    JAMMU_AND_KASHMIR("01", "Jammu and Kashmir"),
    /** Himachal Pradesh (GST code 02). */
    HIMACHAL_PRADESH("02", "Himachal Pradesh"),
    /** Punjab (GST code 03). */
    PUNJAB("03", "Punjab"),
    /** Chandigarh (GST code 04). */
    CHANDIGARH("04", "Chandigarh"),
    /** Uttarakhand (GST code 05). */
    UTTARAKHAND("05", "Uttarakhand"),
    /** Haryana (GST code 06). */
    HARYANA("06", "Haryana"),
    /** Delhi (GST code 07). */
    DELHI("07", "Delhi"),
    /** Rajasthan (GST code 08). */
    RAJASTHAN("08", "Rajasthan"),
    /** Uttar Pradesh (GST code 09). */
    UTTAR_PRADESH("09", "Uttar Pradesh"),
    /** Bihar (GST code 10). */
    BIHAR("10", "Bihar"),
    /** Sikkim (GST code 11). */
    SIKKIM("11", "Sikkim"),
    /** Arunachal Pradesh (GST code 12). */
    ARUNACHAL_PRADESH("12", "Arunachal Pradesh"),
    /** Nagaland (GST code 13). */
    NAGALAND("13", "Nagaland"),
    /** Manipur (GST code 14). */
    MANIPUR("14", "Manipur"),
    /** Mizoram (GST code 15). */
    MIZORAM("15", "Mizoram"),
    /** Tripura (GST code 16). */
    TRIPURA("16", "Tripura"),
    /** Meghalaya (GST code 17). */
    MEGHALAYA("17", "Meghalaya"),
    /** Assam (GST code 18). */
    ASSAM("18", "Assam"),
    /** West Bengal (GST code 19). */
    WEST_BENGAL("19", "West Bengal"),
    /** Jharkhand (GST code 20). */
    JHARKHAND("20", "Jharkhand"),
    /** Odisha (GST code 21). */
    ODISHA("21", "Odisha"),
    /** Chhattisgarh (GST code 22). */
    CHHATTISGARH("22", "Chhattisgarh"),
    /** Madhya Pradesh (GST code 23). */
    MADHYA_PRADESH("23", "Madhya Pradesh"),
    /** Gujarat (GST code 24). */
    GUJARAT("24", "Gujarat"),
    /**
     * Dadra and Nagar Haveli and Daman and Diu (merged post-2020).
     * Code 25 is the current official code after merger.
     */
    DADRA_AND_NAGAR_HAVELI_AND_DAMAN_AND_DIU("25", "Dadra and Nagar Haveli and Daman and Diu"),
    /**
     * Legacy code 26 for Daman and Diu (before the 2020 merger).
     * Kept for backward compatibility with pre-merger GSTINs.
     */
    DADRA_AND_NAGAR_HAVELI_AND_DAMAN_AND_DIU_LEGACY("26", "Dadra and Nagar Haveli and Daman and Diu (legacy)"),
    /** Maharashtra (GST code 27). */
    MAHARASHTRA("27", "Maharashtra"),
    /**
     * Old Andhra Pradesh code (deprecated after bifurcation in 2014).
     * Kept for backward compatibility.
     */
    ANDHRA_PRADESH_OLD("28", "Andhra Pradesh (old, pre-bifurcation)"),
    /** Karnataka (GST code 29). */
    KARNATAKA("29", "Karnataka"),
    /** Goa (GST code 30). */
    GOA("30", "Goa"),
    /** Lakshadweep (GST code 31). */
    LAKSHADWEEP("31", "Lakshadweep"),
    /** Kerala (GST code 32). */
    KERALA("32", "Kerala"),
    /** Tamil Nadu (GST code 33). */
    TAMIL_NADU("33", "Tamil Nadu"),
    /** Puducherry (GST code 34). */
    PUDUCHERRY("34", "Puducherry"),
    /** Andaman and Nicobar Islands (GST code 35). */
    ANDAMAN_AND_NICOBAR_ISLANDS("35", "Andaman and Nicobar Islands"),
    /** Telangana (GST code 36). */
    TELANGANA("36", "Telangana"),
    /** Andhra Pradesh (GST code 37, post-bifurcation). */
    ANDHRA_PRADESH("37", "Andhra Pradesh"),
    /** Ladakh (GST code 38). */
    LADAKH("38", "Ladakh"),
    /** Other Territory (GST code 97). */
    OTHER_TERRITORY("97", "Other Territory"),
    /** Centre Jurisdiction (GST code 99). */
    CENTRE_JURISDICTION("99", "Centre Jurisdiction");

    private final String code;
    private final String displayName;

    private static final Map<String, StateCode> CODE_MAP;

    static {
        Map<String, StateCode> map = new HashMap<>();
        for (StateCode sc : values()) {
            map.put(sc.code, sc);
        }
        CODE_MAP = Collections.unmodifiableMap(map);
    }

    StateCode(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Returns the 2-digit numeric GST state code.
     *
     * @return the 2-digit state code string (e.g. "29")
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the human-readable display name for this state/UT.
     *
     * @return display name (e.g. "Karnataka")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Looks up a {@link StateCode} by its 2-digit code.
     *
     * @param code the 2-digit state code string; may be null
     * @return an {@link Optional} containing the matching {@link StateCode}, or empty if not found
     */
    public static Optional<StateCode> fromCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(CODE_MAP.get(code));
    }
}
