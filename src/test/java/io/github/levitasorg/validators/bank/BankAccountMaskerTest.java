package io.github.levitasorg.validators.bank;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BankAccountMasker}.
 */
class BankAccountMaskerTest {

    // --- mask() ---

    @Test
    void mask_null_returnsNull() {
        assertThat(BankAccountMasker.mask(null)).isNull();
    }

    @Test
    void mask_empty_returnsEmpty() {
        assertThat(BankAccountMasker.mask("")).isEqualTo("");
    }

    @Test
    void mask_12Digits_masksFirst8() {
        assertThat(BankAccountMasker.mask("123456789012")).isEqualTo("XXXXXXXX9012");
    }

    @Test
    void mask_exactlyFourChars_allMasked() {
        // ≤ 4 chars → all X
        assertThat(BankAccountMasker.mask("1234")).isEqualTo("XXXX");
    }

    @Test
    void mask_threeChars_allMasked() {
        assertThat(BankAccountMasker.mask("123")).isEqualTo("XXX");
    }

    @Test
    void mask_oneChar_allMasked() {
        assertThat(BankAccountMasker.mask("1")).isEqualTo("X");
    }

    @Test
    void mask_fiveChars_showsLast4() {
        assertThat(BankAccountMasker.mask("12345")).isEqualTo("X2345");
    }

    @Test
    void mask_18Digits_masksFirst14() {
        assertThat(BankAccountMasker.mask("123456789012345678"))
                .isEqualTo("XXXXXXXXXXXXXX5678");
    }

    @Test
    void mask_nonNumericInput_stillMasks() {
        assertThat(BankAccountMasker.mask("ABCD1234WXYZ")).isEqualTo("XXXXXXXXWXYZ");
    }

    @Test
    void mask_outputLengthMatchesInput() {
        String input = "123456789012";
        assertThat(BankAccountMasker.mask(input)).hasSameSizeAs(input);
    }

    // --- maskPartial() ---

    @Test
    void maskPartial_null_returnsNull() {
        assertThat(BankAccountMasker.maskPartial(null)).isNull();
    }

    @Test
    void maskPartial_empty_returnsEmpty() {
        assertThat(BankAccountMasker.maskPartial("")).isEqualTo("");
    }

    @Test
    void maskPartial_12Digits_showsPrefixAndSuffix() {
        assertThat(BankAccountMasker.maskPartial("123456789012")).isEqualTo("12XXXXXX9012");
    }

    @Test
    void maskPartial_exactlyFourChars_fullyMasked() {
        // ≤ 4 chars → all X
        assertThat(BankAccountMasker.maskPartial("1234")).isEqualTo("XXXX");
    }

    @Test
    void maskPartial_threeChars_fullyMasked() {
        assertThat(BankAccountMasker.maskPartial("123")).isEqualTo("XXX");
    }

    @Test
    void maskPartial_fiveChars_showsSuffix() {
        // 5 chars: 5 > 4 (suffix) but < 7 (min for partial) → show masked + last 4
        assertThat(BankAccountMasker.maskPartial("12345")).isEqualTo("X2345");
    }

    @Test
    void maskPartial_sixChars_showsSuffix() {
        // 6 chars is not enough for prefix+mask+suffix (need ≥7), so shows masked+suffix
        assertThat(BankAccountMasker.maskPartial("123456")).isEqualTo("XX3456");
    }

    @Test
    void maskPartial_sevenChars_showsPrefixAndSuffix() {
        // Exactly 1 char masked in the middle
        assertThat(BankAccountMasker.maskPartial("1234567")).isEqualTo("12X4567");
    }

    @Test
    void maskPartial_18Digits_showsPrefixAndSuffix() {
        assertThat(BankAccountMasker.maskPartial("123456789012345678"))
                .isEqualTo("12XXXXXXXXXXXX5678"); // 18 - 2 prefix - 4 suffix = 12 X's
    }

    @Test
    void maskPartial_nonNumericInput_stillMasks() {
        assertThat(BankAccountMasker.maskPartial("ABCDEFGHIJKL"))
                .isEqualTo("ABXXXXXX" + "IJKL");
    }

    @Test
    void maskPartial_outputLengthMatchesInput() {
        String input = "123456789012";
        assertThat(BankAccountMasker.maskPartial(input)).hasSameSizeAs(input);
    }
}
