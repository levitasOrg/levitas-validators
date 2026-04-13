package io.github.levitasorg.validators.hsn;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Validator for Harmonised System of Nomenclature (HSN) codes used in Indian GST invoicing.
 *
 * <p>HSN codes classify goods under the Goods and Services Tax framework. The required
 * digit length depends on the taxpayer's aggregate annual turnover:
 * <ul>
 *   <li>&lt;= &#x20B9;5 crore: 4-digit HSN required on B2B invoices</li>
 *   <li>&gt; &#x20B9;5 crore: 6-digit HSN required</li>
 *   <li>Exports/imports: 8-digit HSN required</li>
 * </ul>
 *
 * <p>All methods are null-safe and thread-safe.
 *
 * <p>Example usage:
 * <pre>{@code
 * HsnValidator.isValid("8471");                                           // true
 * HsnValidator.getRequiredDigits(new BigDecimal("100000000"));            // 6
 * HsnValidator.isValidForTurnover("847130", new BigDecimal("100000000")); // true
 * }</pre>
 */
public final class HsnValidator {

    private HsnValidator() {
        // utility class
    }

    /** &#x20B9;5 crore threshold: 5,00,00,000 (50,000,000). */
    static final BigDecimal FIVE_CRORE = new BigDecimal("50000000");

    /** Pattern matching valid HSN codes: 2, 4, 6, or 8 numeric digits only. */
    private static final Pattern HSN_PATTERN = Pattern.compile("^[0-9]{2}([0-9]{2}([0-9]{2}([0-9]{2})?)?)?$");

    /**
     * Returns {@code true} if the given string is a syntactically valid HSN code.
     *
     * <p>A valid HSN code contains exactly 2, 4, 6, or 8 numeric digits.
     * This method is null-safe.
     *
     * @param hsn the HSN code to validate; may be null
     * @return {@code true} if the HSN matches the valid format
     */
    public static boolean isValid(String hsn) {
        if (hsn == null || hsn.isEmpty()) {
            return false;
        }
        int len = hsn.length();
        if (len != 2 && len != 4 && len != 6 && len != 8) {
            return false;
        }
        return HSN_PATTERN.matcher(hsn).matches();
    }

    /**
     * Returns the minimum number of HSN digits required for the given annual turnover.
     *
     * <ul>
     *   <li>Turnover &lt;= &#x20B9;5 crore &rarr; 4 digits</li>
     *   <li>Turnover &gt; &#x20B9;5 crore &rarr; 6 digits</li>
     *   <li>Null turnover &rarr; 4 digits (safe default)</li>
     * </ul>
     *
     * <p>Note: exports and imports always require 8 digits — use
     * {@link #isValidForExportImport(String)} for that check.
     *
     * @param annualTurnoverInRupees the aggregate annual turnover; may be null
     * @return the minimum required HSN digit count (4 or 6)
     */
    public static int getRequiredDigits(BigDecimal annualTurnoverInRupees) {
        if (annualTurnoverInRupees == null) {
            return 4;
        }
        if (annualTurnoverInRupees.compareTo(FIVE_CRORE) > 0) {
            return 6;
        }
        return 4;
    }

    /**
     * Returns {@code true} if the given HSN code meets the minimum digit requirement
     * for the given annual turnover.
     *
     * <p>The HSN must be valid (see {@link #isValid(String)}) and must have at least as many
     * digits as required by the turnover threshold.
     *
     * @param hsn                    the HSN code; may be null
     * @param annualTurnoverInRupees the aggregate annual turnover; may be null
     * @return {@code true} if the HSN is valid and satisfies the turnover-based length requirement
     */
    public static boolean isValidForTurnover(String hsn, BigDecimal annualTurnoverInRupees) {
        if (!isValid(hsn)) {
            return false;
        }
        int required = getRequiredDigits(annualTurnoverInRupees);
        return hsn.length() >= required;
    }

    /**
     * Returns {@code true} if the given HSN code is valid for export or import transactions.
     *
     * <p>Export/import transactions require an 8-digit HSN code.
     *
     * @param hsn the HSN code; may be null
     * @return {@code true} if the HSN is valid and is exactly 8 digits
     */
    public static boolean isValidForExportImport(String hsn) {
        return isValid(hsn) && hsn.length() == 8;
    }
}
