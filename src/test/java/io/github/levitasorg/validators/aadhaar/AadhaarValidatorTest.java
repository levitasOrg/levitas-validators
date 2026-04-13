package io.github.levitasorg.validators.aadhaar;

import io.github.levitasorg.validators.internal.VerhoeffChecksum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link AadhaarValidator} and {@link AadhaarMasker}.
 */
class AadhaarValidatorTest {

    /**
     * Generates a valid 12-digit Aadhaar with Verhoeff checksum.
     * First digit is forced to 2–9.
     */
    private static String generateValidAadhaar(String prefix11Digits) {
        int check = VerhoeffChecksum.computeChecksum(prefix11Digits);
        return prefix11Digits + check;
    }

    private static final String VALID_AADHAAR = generateValidAadhaar("23412341234");

    // --- isValid() ---

    @Test
    void isValid_null_returnsFalse() {
        assertThat(AadhaarValidator.isValid(null)).isFalse();
    }

    @Test
    void isValid_empty_returnsFalse() {
        assertThat(AadhaarValidator.isValid("")).isFalse();
    }

    @Test
    void isValid_tooShort_returnsFalse() {
        assertThat(AadhaarValidator.isValid("23412341")).isFalse();
    }

    @Test
    void isValid_tooLong_returnsFalse() {
        assertThat(AadhaarValidator.isValid("2341234123456")).isFalse();
    }

    @Test
    void isValid_startsWithZero_returnsFalse() {
        String prefix = "03412341234";
        int check = VerhoeffChecksum.computeChecksum(prefix);
        assertThat(AadhaarValidator.isValid(prefix + check)).isFalse();
    }

    @Test
    void isValid_startsWithOne_returnsFalse() {
        String prefix = "13412341234";
        int check = VerhoeffChecksum.computeChecksum(prefix);
        assertThat(AadhaarValidator.isValid(prefix + check)).isFalse();
    }

    @Test
    void isValid_validAadhaar_returnsTrue() {
        assertThat(AadhaarValidator.isValid(VALID_AADHAAR)).isTrue();
    }

    @Test
    void isValid_wrongChecksum_returnsFalse() {
        String wrong = VALID_AADHAAR.substring(0, 11)
                + ((VALID_AADHAAR.charAt(11) - '0' + 1) % 10);
        assertThat(AadhaarValidator.isValid(wrong)).isFalse();
    }

    @Test
    void isValid_nonNumeric_returnsFalse() {
        assertThat(AadhaarValidator.isValid("ABCDEFGHIJKL")).isFalse();
    }

    // --- Separator handling ---

    @Test
    void isValid_withSpaceSeparators_returnsTrue() {
        String withSpaces = VALID_AADHAAR.substring(0, 4) + " "
                + VALID_AADHAAR.substring(4, 8) + " "
                + VALID_AADHAAR.substring(8, 12);
        assertThat(AadhaarValidator.isValid(withSpaces)).isTrue();
    }

    @Test
    void isValid_withHyphenSeparators_returnsTrue() {
        String withHyphens = VALID_AADHAAR.substring(0, 4) + "-"
                + VALID_AADHAAR.substring(4, 8) + "-"
                + VALID_AADHAAR.substring(8, 12);
        assertThat(AadhaarValidator.isValid(withHyphens)).isTrue();
    }

    // --- isValidFormat() ---

    @Test
    void isValidFormat_null_returnsFalse() {
        assertThat(AadhaarValidator.isValidFormat(null)).isFalse();
    }

    @Test
    void isValidFormat_wrongChecksum_returnsTrue() {
        // Format check passes even if checksum is wrong
        String wrongChecksum = VALID_AADHAAR.substring(0, 11)
                + ((VALID_AADHAAR.charAt(11) - '0' + 1) % 10);
        assertThat(AadhaarValidator.isValidFormat(wrongChecksum)).isTrue();
    }

    @Test
    void isValidFormat_startsWithZero_returnsFalse() {
        assertThat(AadhaarValidator.isValidFormat("012341234567")).isFalse();
    }

    @Test
    void isValidFormat_valid_returnsTrue() {
        assertThat(AadhaarValidator.isValidFormat(VALID_AADHAAR)).isTrue();
    }

    // --- Multiple valid Aadhaar numbers round-trip test ---

    @ParameterizedTest
    @ValueSource(strings = {"23412341234", "34512345678", "56789012345", "98765432109", "20000000000"})
    void isValid_generatedAadhaar_roundTrip(String prefix) {
        String valid = generateValidAadhaar(prefix);
        assertThat(AadhaarValidator.isValid(valid)).isTrue();
    }

    // --- Separator strictness (AntiLabs: only space and hyphen, not tab/newline) ---

    @Test
    void isValid_withTabSeparators_returnsFalse() {
        String withTabs = VALID_AADHAAR.substring(0, 4) + "\t"
                + VALID_AADHAAR.substring(4, 8) + "\t"
                + VALID_AADHAAR.substring(8, 12);
        // Tabs are not valid Aadhaar separators
        assertThat(AadhaarValidator.isValid(withTabs)).isFalse();
    }

    @Test
    void isValid_withNewlineSeparators_returnsFalse() {
        String withNewlines = VALID_AADHAAR.substring(0, 4) + "\n"
                + VALID_AADHAAR.substring(4, 8) + "\n"
                + VALID_AADHAAR.substring(8, 12);
        assertThat(AadhaarValidator.isValid(withNewlines)).isFalse();
    }

    @Test
    void isValidFormat_withTabSeparators_returnsFalse() {
        String withTabs = VALID_AADHAAR.substring(0, 4) + "\t"
                + VALID_AADHAAR.substring(4, 8) + "\t"
                + VALID_AADHAAR.substring(8, 12);
        assertThat(AadhaarValidator.isValidFormat(withTabs)).isFalse();
    }

    // --- AadhaarMasker ---

    @Test
    void mask_validAadhaar_returnsCorrectFormat() {
        String masked = AadhaarMasker.mask(VALID_AADHAAR);
        assertThat(masked).startsWith("XXXX-XXXX-");
        assertThat(masked).endsWith(VALID_AADHAAR.substring(8));
        assertThat(masked).hasSize(14); // "XXXX-XXXX-" (10) + 4 digits = 14
    }

    @Test
    void mask_withSeparators_worksCorrectly() {
        String withSpaces = VALID_AADHAAR.substring(0, 4) + " "
                + VALID_AADHAAR.substring(4, 8) + " "
                + VALID_AADHAAR.substring(8, 12);
        String masked = AadhaarMasker.mask(withSpaces);
        assertThat(masked).startsWith("XXXX-XXXX-");
        assertThat(masked).endsWith(VALID_AADHAAR.substring(8));
    }

    @Test
    void mask_null_throws() {
        assertThatThrownBy(() -> AadhaarMasker.mask(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void mask_tooShort_throws() {
        assertThatThrownBy(() -> AadhaarMasker.mask("12345678"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void maskCompact_validAadhaar_returnsCorrectFormat() {
        String masked = AadhaarMasker.maskCompact(VALID_AADHAAR);
        assertThat(masked).startsWith("XXXXXXXX");
        assertThat(masked).endsWith(VALID_AADHAAR.substring(8));
        assertThat(masked).hasSize(12);
    }

    @Test
    void maskCompact_null_throws() {
        assertThatThrownBy(() -> AadhaarMasker.maskCompact(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void maskCompact_showsOnlyLast4Digits() {
        String last4 = VALID_AADHAAR.substring(8);
        assertThat(AadhaarMasker.maskCompact(VALID_AADHAAR)).isEqualTo("XXXXXXXX" + last4);
    }
}
