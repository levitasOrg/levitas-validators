package io.github.levitasorg.validators.ifsc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link IfscValidator} and {@link BankRegistry}.
 */
class IfscValidatorTest {

    // --- isValid() ---

    @Test
    void isValid_null_returnsFalse() {
        assertThat(IfscValidator.isValid(null)).isFalse();
    }

    @Test
    void isValid_empty_returnsFalse() {
        assertThat(IfscValidator.isValid("")).isFalse();
    }

    @Test
    void isValid_tooShort_returnsFalse() {
        assertThat(IfscValidator.isValid("HDFC001234")).isFalse();
    }

    @Test
    void isValid_tooLong_returnsFalse() {
        assertThat(IfscValidator.isValid("HDFC00012345")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"HDFC0001234", "SBIN0001234", "ICIC0001234", "AXIS0123456"})
    void isValid_validFormats_returnsTrue(String ifsc) {
        assertThat(IfscValidator.isValid(ifsc)).isTrue();
    }

    @Test
    void isValid_lowercase_returnsFalse() {
        assertThat(IfscValidator.isValid("hdfc0001234")).isFalse();
    }

    @Test
    void isValid_fifthCharNotZero_returnsFalse() {
        assertThat(IfscValidator.isValid("HDFC1001234")).isFalse();
        assertThat(IfscValidator.isValid("HDFC9001234")).isFalse();
    }

    @Test
    void isValid_numericBankCode_returnsFalse() {
        assertThat(IfscValidator.isValid("1234001234A")).isFalse();
    }

    @Test
    void isValid_digitInBankCode_returnsFalse() {
        assertThat(IfscValidator.isValid("HD1C0001234")).isFalse();
    }

    @Test
    void isValid_withAlphanumericBranchCode_returnsTrue() {
        // Branch code may contain alphanumeric
        assertThat(IfscValidator.isValid("HDFC0AB1234")).isTrue();
    }

    // --- getBankCode() ---

    @Test
    void getBankCode_null_returnsEmpty() {
        assertThat(IfscValidator.getBankCode(null)).isEmpty();
    }

    @Test
    void getBankCode_tooShort_returnsEmpty() {
        assertThat(IfscValidator.getBankCode("HDF")).isEmpty();
    }

    @Test
    void getBankCode_validIfsc_returnsFirst4Chars() {
        assertThat(IfscValidator.getBankCode("HDFC0001234")).contains("HDFC");
    }

    // --- getBankName() ---

    @Test
    void getBankName_null_returnsEmpty() {
        assertThat(IfscValidator.getBankName(null)).isEmpty();
    }

    @Test
    void getBankName_knownBank_returnsName() {
        assertThat(IfscValidator.getBankName("HDFC0001234")).contains("HDFC Bank");
        assertThat(IfscValidator.getBankName("SBIN0001234")).contains("State Bank of India");
        assertThat(IfscValidator.getBankName("ICIC0001234")).contains("ICICI Bank");
    }

    @Test
    void getBankName_unknownBankCode_returnsEmpty() {
        assertThat(IfscValidator.getBankName("ZZZZ0001234")).isEmpty();
    }

    // --- getBranchCode() ---

    @Test
    void getBranchCode_null_returnsEmpty() {
        assertThat(IfscValidator.getBranchCode(null)).isEmpty();
    }

    @Test
    void getBranchCode_tooShort_returnsEmpty() {
        assertThat(IfscValidator.getBranchCode("HDFC000")).isEmpty();
    }

    @Test
    void getBranchCode_validIfsc_returnsLast6Chars() {
        assertThat(IfscValidator.getBranchCode("HDFC0001234")).contains("001234");
    }

    // --- BankRegistry ---

    @Test
    void bankRegistry_getAllBankCodes_notEmpty() {
        assertThat(BankRegistry.getAllBankCodes()).isNotEmpty();
    }

    @Test
    void bankRegistry_getAllBankCodes_containsKnownBanks() {
        assertThat(BankRegistry.getAllBankCodes()).contains("HDFC", "SBIN", "ICIC", "AXIS", "KKBK");
    }

    @Test
    void bankRegistry_getBankName_null_returnsEmpty() {
        assertThat(BankRegistry.getBankName(null)).isEmpty();
    }

    @Test
    void bankRegistry_getBankName_knownCode_returnsName() {
        assertThat(BankRegistry.getBankName("HDFC")).contains("HDFC Bank");
        assertThat(BankRegistry.getBankName("CNRB")).contains("Canara Bank");
    }

    @Test
    void bankRegistry_getBankName_unknownCode_returnsEmpty() {
        assertThat(BankRegistry.getBankName("XXXX")).isEmpty();
    }

    @Test
    void bankRegistry_getBankName_lowercaseInput_returnsBankName() {
        // Registry normalizes to uppercase
        assertThat(BankRegistry.getBankName("hdfc")).contains("HDFC Bank");
    }
}
