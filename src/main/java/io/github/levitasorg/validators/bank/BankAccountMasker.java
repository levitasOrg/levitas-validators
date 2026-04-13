package io.github.levitasorg.validators.bank;

/**
 * Log-safe masking utility for Indian bank account numbers.
 *
 * <p>Bank account numbers vary in length (9–18 digits typically) and are PII.
 * This utility provides masking helpers for use in logs and audit trails.
 *
 * <p>This is a formatting utility, not a validator — it makes no assumption about
 * whether the input contains only digits. Non-numeric characters are included
 * in the length calculation and masked like any other character.
 *
 * <p>All methods are null-safe and thread-safe.
 *
 * <p>Example usage:
 * <pre>{@code
 * BankAccountMasker.mask("123456789012");        // "XXXXXXXX9012"
 * BankAccountMasker.maskPartial("123456789012"); // "12XXXXXX9012"
 * }</pre>
 */
public final class BankAccountMasker {

    private BankAccountMasker() {
        // utility class
    }

    private static final int VISIBLE_SUFFIX_LENGTH = 4;
    private static final int VISIBLE_PREFIX_LENGTH = 2;
    private static final char MASK_CHAR = 'X';

    /**
     * Masks all characters of the account number except the last 4.
     *
     * <p>Examples:
     * <ul>
     *   <li>{@code "123456789012"} → {@code "XXXXXXXX9012"}</li>
     *   <li>{@code "ABC"} → {@code "XXX"} (fewer than 4 characters → all X's)</li>
     *   <li>{@code null} → {@code null}</li>
     * </ul>
     *
     * @param accountNumber the bank account number; may be null
     * @return the masked string, or {@code null} if input is null
     */
    public static String mask(String accountNumber) {
        if (accountNumber == null) {
            return null;
        }
        int len = accountNumber.length();
        if (len <= VISIBLE_SUFFIX_LENGTH) {
            return repeatX(len);
        }
        int maskLen = len - VISIBLE_SUFFIX_LENGTH;
        return repeatX(maskLen) + accountNumber.substring(maskLen);
    }

    /**
     * Masks all characters of the account number except the first 2 and the last 4.
     *
     * <p>Examples:
     * <ul>
     *   <li>{@code "123456789012"} → {@code "12XXXXXX9012"} (12 chars: 2 prefix + 6 mask + 4 suffix)</li>
     *   <li>{@code "1234567"} → {@code "12X4567"} (7 chars: 2 prefix + 1 mask + 4 suffix)</li>
     *   <li>{@code "123456"} → {@code "XX3456"} (≤ 6 chars: not enough for prefix+mask+suffix)</li>
     *   <li>{@code "1234"} → {@code "XXXX"} (≤ 4 chars: all X)</li>
     *   <li>{@code null} → {@code null}</li>
     * </ul>
     *
     * @param accountNumber the bank account number; may be null
     * @return the partially masked string, or {@code null} if input is null
     */
    public static String maskPartial(String accountNumber) {
        if (accountNumber == null) {
            return null;
        }
        int len = accountNumber.length();
        // Need at least prefix + 1 masked char + suffix to do partial masking usefully
        int minRequiredForPartial = VISIBLE_PREFIX_LENGTH + VISIBLE_SUFFIX_LENGTH + 1;
        if (len < minRequiredForPartial) {
            // Not enough characters to show both prefix and suffix without overlap — fully mask
            if (len <= VISIBLE_SUFFIX_LENGTH) {
                return repeatX(len);
            }
            // Show suffix only if we can't show full prefix
            int maskLen = len - VISIBLE_SUFFIX_LENGTH;
            return repeatX(maskLen) + accountNumber.substring(maskLen);
        }
        String prefix = accountNumber.substring(0, VISIBLE_PREFIX_LENGTH);
        String suffix = accountNumber.substring(len - VISIBLE_SUFFIX_LENGTH);
        int maskLen = len - VISIBLE_PREFIX_LENGTH - VISIBLE_SUFFIX_LENGTH;
        return prefix + repeatX(maskLen) + suffix;
    }

    private static String repeatX(int count) {
        return String.valueOf(MASK_CHAR).repeat(count);
    }
}
