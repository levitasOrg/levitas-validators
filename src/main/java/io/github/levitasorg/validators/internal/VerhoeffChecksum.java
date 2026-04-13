package io.github.levitasorg.validators.internal;

/**
 * Implementation of the Verhoeff checksum algorithm.
 *
 * <p>The Verhoeff algorithm is a checksum formula that uses a dihedral group
 * of order 10. It detects all single-digit errors and all adjacent transpositions.
 * It is used by UIDAI (Unique Identification Authority of India) for Aadhaar numbers.
 *
 * <p>This class is intended for internal use only.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Verhoeff_algorithm">Verhoeff algorithm</a>
 */
public final class VerhoeffChecksum {

    private VerhoeffChecksum() {
        // utility class
    }

    /**
     * Multiplication table d (Cayley table for D5).
     */
    private static final int[][] D = {
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
        {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
        {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
        {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
        {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
        {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
        {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
        {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
        {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
    };

    /**
     * Permutation table p.
     */
    private static final int[][] P = {
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
        {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
        {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
        {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
        {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
        {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
        {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
    };

    /**
     * Inverse table inv.
     */
    private static final int[] INV = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

    /**
     * Validates a string of digits using the Verhoeff algorithm.
     *
     * <p>The number is considered valid if the Verhoeff check digit (the last digit)
     * is correct with respect to the preceding digits.
     *
     * @param digits the full digit string including the check digit; must be non-null and non-empty
     * @return {@code true} if the Verhoeff checksum is valid, {@code false} otherwise
     * @throws IllegalArgumentException if {@code digits} is null, empty, or contains non-digit characters
     */
    public static boolean validate(String digits) {
        if (digits == null || digits.isEmpty()) {
            throw new IllegalArgumentException("Digits must not be null or empty");
        }
        int c = 0;
        int[] reversed = reverseDigits(digits);
        for (int i = 0; i < reversed.length; i++) {
            c = D[c][P[i % 8][reversed[i]]];
        }
        return c == 0;
    }

    /**
     * Computes the Verhoeff check digit for the given digit string (without check digit).
     *
     * <p>The returned digit, when appended to {@code digitsWithoutChecksum}, forms a
     * string that passes {@link #validate(String)}.
     *
     * @param digitsWithoutChecksum the digit string without the trailing check digit;
     *                              must be non-null, non-empty, and contain only digits
     * @return the computed check digit (0–9)
     * @throws IllegalArgumentException if input is null, empty, or contains non-digit characters
     */
    public static int computeChecksum(String digitsWithoutChecksum) {
        if (digitsWithoutChecksum == null || digitsWithoutChecksum.isEmpty()) {
            throw new IllegalArgumentException("Digits must not be null or empty");
        }
        // Append a 0 placeholder for the check digit position
        String withPlaceholder = digitsWithoutChecksum + "0";
        int c = 0;
        int[] reversed = reverseDigits(withPlaceholder);
        for (int i = 0; i < reversed.length; i++) {
            c = D[c][P[i % 8][reversed[i]]];
        }
        return INV[c];
    }

    /**
     * Reverses the digit string and returns an int array.
     */
    private static int[] reverseDigits(String digits) {
        int len = digits.length();
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            char ch = digits.charAt(len - 1 - i);
            if (ch < '0' || ch > '9') {
                throw new IllegalArgumentException("Non-digit character found: '" + ch + "'");
            }
            result[i] = ch - '0';
        }
        return result;
    }
}
