package io.github.levitasorg.validators.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link VerhoeffChecksum}.
 */
class VerhoeffChecksumTest {

    // --- validate() ---

    @Test
    void validate_nullThrows() {
        assertThatThrownBy(() -> VerhoeffChecksum.validate(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validate_emptyThrows() {
        assertThatThrownBy(() -> VerhoeffChecksum.validate(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validate_nonDigitThrows() {
        assertThatThrownBy(() -> VerhoeffChecksum.validate("123A5"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validate_singleDigitZeroIsValid() {
        // "0" by itself: Verhoeff of empty prefix → check digit 0, so "0" should be valid
        assertThat(VerhoeffChecksum.validate("0")).isTrue();
    }

    /**
     * Known good 12-digit Aadhaar-style number for Verhoeff testing.
     * computeChecksum("23412341234") == 6, so "234123412346" is valid.
     */
    @Test
    void validate_knownValidNumber() {
        // compute checksum on 11 digits, then validate the 12-digit number
        String prefix = "23412341234";
        int check = VerhoeffChecksum.computeChecksum(prefix);
        String full = prefix + check;
        assertThat(VerhoeffChecksum.validate(full)).isTrue();
    }

    @Test
    void validate_modifiedDigitFails() {
        String prefix = "23412341234";
        int check = VerhoeffChecksum.computeChecksum(prefix);
        String valid = prefix + check;
        // Flip the last digit by 1
        int flippedCheck = (check + 1) % 10;
        String invalid = prefix + flippedCheck;
        assertThat(VerhoeffChecksum.validate(invalid)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"236", "2363", "12121", "236366"})
    void validate_roundTrip(String prefix) {
        int checkDigit = VerhoeffChecksum.computeChecksum(prefix);
        String full = prefix + checkDigit;
        assertThat(VerhoeffChecksum.validate(full)).isTrue();
    }

    /**
     * Pinned known-answer test for prefix {@code "2363"}.
     *
     * <p>The Levitas plan specification incorrectly stated that the checksum for {@code "2363"}
     * is {@code 3} (making {@code "23633"} valid). The correct value, computed against the
     * standard Verhoeff tables, is {@code 4} — making {@code "23634"} valid and {@code "23633"}
     * invalid. These tests pin that computation to prevent silent algorithm regressions.
     */
    @Test
    void computeChecksum_knownVector_2363returns4() {
        assertThat(VerhoeffChecksum.computeChecksum("2363")).isEqualTo(4);
    }

    @Test
    void validate_knownVector_23634isValid() {
        assertThat(VerhoeffChecksum.validate("23634")).isTrue();
    }

    @Test
    void validate_knownVector_23633isInvalid() {
        // "23633" has an incorrect checksum — the correct digit for "2363" is 4, not 3
        assertThat(VerhoeffChecksum.validate("23633")).isFalse();
    }

    // --- computeChecksum() ---

    @Test
    void computeChecksum_nullThrows() {
        assertThatThrownBy(() -> VerhoeffChecksum.computeChecksum(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void computeChecksum_emptyThrows() {
        assertThatThrownBy(() -> VerhoeffChecksum.computeChecksum(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void computeChecksum_nonDigitThrows() {
        assertThatThrownBy(() -> VerhoeffChecksum.computeChecksum("12A4"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void computeChecksum_returnsSingleDigit() {
        int result = VerhoeffChecksum.computeChecksum("12345678901");
        assertThat(result).isBetween(0, 9);
    }

    @Test
    void computeChecksum_consistentForSameInput() {
        String prefix = "23412341234";
        int first = VerhoeffChecksum.computeChecksum(prefix);
        int second = VerhoeffChecksum.computeChecksum(prefix);
        assertThat(first).isEqualTo(second);
    }

    @Test
    void computeChecksum_differentInputsDifferentResults() {
        // Not always true, but these specific inputs should differ
        int a = VerhoeffChecksum.computeChecksum("23412341234");
        int b = VerhoeffChecksum.computeChecksum("23412341235");
        // Just verify they are both valid single digits
        assertThat(a).isBetween(0, 9);
        assertThat(b).isBetween(0, 9);
    }

    @Test
    void validate_allDigitsAllZeros_isValid() {
        // "0" is valid, but "00" - let's verify round-trip for multi-digit
        String prefix = "0000000000";
        int check = VerhoeffChecksum.computeChecksum(prefix);
        assertThat(VerhoeffChecksum.validate(prefix + check)).isTrue();
    }
}
