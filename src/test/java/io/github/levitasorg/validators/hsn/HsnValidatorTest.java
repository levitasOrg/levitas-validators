package io.github.levitasorg.validators.hsn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HsnValidator}.
 */
class HsnValidatorTest {

    // --- isValid() ---

    @Test
    void isValid_null_returnsFalse() {
        assertThat(HsnValidator.isValid(null)).isFalse();
    }

    @Test
    void isValid_empty_returnsFalse() {
        assertThat(HsnValidator.isValid("")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"84", "8471", "847130", "84713010"})
    void isValid_validLengths_returnsTrue(String hsn) {
        assertThat(HsnValidator.isValid(hsn)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"8", "847", "84713", "8471301", "847130100"})
    void isValid_invalidLengths_returnsFalse(String hsn) {
        assertThat(HsnValidator.isValid(hsn)).isFalse();
    }

    @Test
    void isValid_nonNumeric_returnsFalse() {
        assertThat(HsnValidator.isValid("84AB")).isFalse();
    }

    @Test
    void isValid_alphaPrefix_returnsFalse() {
        assertThat(HsnValidator.isValid("AB71")).isFalse();
    }

    @Test
    void isValid_withSpaces_returnsFalse() {
        assertThat(HsnValidator.isValid("84 71")).isFalse();
    }

    @Test
    void isValid_allZeros_2digits_isValid() {
        assertThat(HsnValidator.isValid("00")).isTrue();
    }

    @Test
    void isValid_allZeros_8digits_isValid() {
        assertThat(HsnValidator.isValid("00000000")).isTrue();
    }

    // --- getRequiredDigits() ---

    @Test
    void getRequiredDigits_null_returns4() {
        assertThat(HsnValidator.getRequiredDigits(null)).isEqualTo(4);
    }

    @Test
    void getRequiredDigits_belowFiveCrore_returns4() {
        assertThat(HsnValidator.getRequiredDigits(new BigDecimal("49900000"))).isEqualTo(4);
    }

    @Test
    void getRequiredDigits_exactlyFiveCrore_returns4() {
        assertThat(HsnValidator.getRequiredDigits(new BigDecimal("50000000"))).isEqualTo(4);
    }

    @Test
    void getRequiredDigits_justAboveFiveCrore_returns6() {
        assertThat(HsnValidator.getRequiredDigits(new BigDecimal("50000001"))).isEqualTo(6);
    }

    @Test
    void getRequiredDigits_hundredCrore_returns6() {
        assertThat(HsnValidator.getRequiredDigits(new BigDecimal("1000000000"))).isEqualTo(6);
    }

    @Test
    void getRequiredDigits_4_99crore_returns4() {
        // 4.99 crore = 49,900,000
        assertThat(HsnValidator.getRequiredDigits(new BigDecimal("49900000"))).isEqualTo(4);
    }

    @Test
    void getRequiredDigits_5_01crore_returns6() {
        // 5.01 crore = 50,100,000
        assertThat(HsnValidator.getRequiredDigits(new BigDecimal("50100000"))).isEqualTo(6);
    }

    // --- isValidForTurnover() ---

    @Test
    void isValidForTurnover_null_hsn_returnsFalse() {
        assertThat(HsnValidator.isValidForTurnover(null, new BigDecimal("10000000"))).isFalse();
    }

    @Test
    void isValidForTurnover_2digits_lowTurnover_returnsFalse() {
        // 2 digits is not enough even for low turnover (requires 4)
        assertThat(HsnValidator.isValidForTurnover("84", new BigDecimal("10000000"))).isFalse();
    }

    @Test
    void isValidForTurnover_4digits_lowTurnover_returnsTrue() {
        assertThat(HsnValidator.isValidForTurnover("8471", new BigDecimal("10000000"))).isTrue();
    }

    @Test
    void isValidForTurnover_4digits_highTurnover_returnsFalse() {
        // Above 5 crore requires 6 digits minimum
        assertThat(HsnValidator.isValidForTurnover("8471", new BigDecimal("100000000"))).isFalse();
    }

    @Test
    void isValidForTurnover_6digits_highTurnover_returnsTrue() {
        assertThat(HsnValidator.isValidForTurnover("847130", new BigDecimal("100000000"))).isTrue();
    }

    @Test
    void isValidForTurnover_8digits_highTurnover_returnsTrue() {
        assertThat(HsnValidator.isValidForTurnover("84713010", new BigDecimal("100000000"))).isTrue();
    }

    @Test
    void isValidForTurnover_nullTurnover_4digits_returnsTrue() {
        assertThat(HsnValidator.isValidForTurnover("8471", null)).isTrue();
    }

    // --- isValidForExportImport() ---

    @Test
    void isValidForExportImport_null_returnsFalse() {
        assertThat(HsnValidator.isValidForExportImport(null)).isFalse();
    }

    @Test
    void isValidForExportImport_4digits_returnsFalse() {
        assertThat(HsnValidator.isValidForExportImport("8471")).isFalse();
    }

    @Test
    void isValidForExportImport_6digits_returnsFalse() {
        assertThat(HsnValidator.isValidForExportImport("847130")).isFalse();
    }

    @Test
    void isValidForExportImport_8digits_returnsTrue() {
        assertThat(HsnValidator.isValidForExportImport("84713010")).isTrue();
    }

    @Test
    void isValidForExportImport_invalid8digits_returnsFalse() {
        assertThat(HsnValidator.isValidForExportImport("8471301X")).isFalse();
    }
}
