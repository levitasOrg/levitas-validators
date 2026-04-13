package io.github.levitasorg.validators.gstin;

/**
 * Test helper that generates format-valid GSTINs with correct checksums.
 *
 * <p>IMPORTANT: All GSTINs generated here use obviously-fake PAN patterns
 * (e.g., {@code AAAAA0000A}) and must never be used in production or for
 * any real business purpose.
 */
public final class TestGstinGenerator {

    private TestGstinGenerator() {}

    /**
     * Generates a GSTIN-format string with a valid checksum for the given components.
     *
     * @param stateCode   2-digit state code (e.g., "29")
     * @param pan         10-character PAN in valid format (e.g., "AAAAA0000A")
     * @param entityCode  entity code character ('1'–'9' or 'A'–'Z')
     * @return a 15-character GSTIN with correct checksum
     */
    public static String generate(String stateCode, String pan, char entityCode) {
        String first14 = stateCode + pan + entityCode + "Z";
        char checksum = GstinValidator.computeChecksum(first14);
        return first14 + checksum;
    }

    /** Pre-computed test GSTINs using fake PAN patterns. */
    public static final String GSTIN_KA_1 = generate("29", "AAAAA0000A", '1');
    public static final String GSTIN_MH_1 = generate("27", "BBBBB1111B", '2');
    public static final String GSTIN_DL_1 = generate("07", "CCCCC2222C", '3');
    public static final String GSTIN_TN_1 = generate("33", "DDDDD3333D", '4');
    public static final String GSTIN_GJ_1 = generate("24", "EEEEE4444E", '5');
    public static final String GSTIN_UP_1 = generate("09", "FFFFF5555F", '6');
    public static final String GSTIN_WB_1 = generate("19", "GGGGG6666G", '7');
    public static final String GSTIN_RJ_1 = generate("08", "HHHHH7777H", '8');
    public static final String GSTIN_AP_1 = generate("37", "IIIII8888I", '9');
    public static final String GSTIN_TS_1 = generate("36", "JJJJJ9999J", 'A');
}
