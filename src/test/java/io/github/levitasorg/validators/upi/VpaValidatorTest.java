package io.github.levitasorg.validators.upi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link VpaValidator} and {@link PspRegistry}.
 */
class VpaValidatorTest {

    // --- isValid() ---

    @Test
    void isValid_null_returnsFalse() {
        assertThat(VpaValidator.isValid(null)).isFalse();
    }

    @Test
    void isValid_empty_returnsFalse() {
        assertThat(VpaValidator.isValid("")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user@okhdfcbank",
        "user.name@paytm",
        "user-name@ybl",
        "user_name@upi",
        "ab@ic",
        "test123@axisbank"
    })
    void isValid_validFormats_returnsTrue(String vpa) {
        assertThat(VpaValidator.isValid(vpa)).isTrue();
    }

    @Test
    void isValid_noAtSign_returnsFalse() {
        assertThat(VpaValidator.isValid("userokhdfcbank")).isFalse();
    }

    @Test
    void isValid_multipleAtSigns_returnsFalse() {
        assertThat(VpaValidator.isValid("user@ok@hdfcbank")).isFalse();
    }

    @Test
    void isValid_emptyUsername_returnsFalse() {
        assertThat(VpaValidator.isValid("@okhdfcbank")).isFalse();
    }

    @Test
    void isValid_singleCharUsername_returnsFalse() {
        assertThat(VpaValidator.isValid("u@okhdfcbank")).isFalse();
    }

    @Test
    void isValid_emptyHandle_returnsFalse() {
        assertThat(VpaValidator.isValid("username@")).isFalse();
    }

    @Test
    void isValid_handleStartsWithDigit_returnsFalse() {
        assertThat(VpaValidator.isValid("user@1handle")).isFalse();
    }

    @Test
    void isValid_specialCharsInHandle_returnsFalse() {
        assertThat(VpaValidator.isValid("user@handle!")).isFalse();
    }

    @Test
    void isValid_specialCharsInUsername_returnsFalse() {
        // '!' not allowed in username
        assertThat(VpaValidator.isValid("user!@handle")).isFalse();
    }

    @Test
    void isValid_usernameExactly2Chars_returnsTrue() {
        assertThat(VpaValidator.isValid("ab@sbi")).isTrue();
    }

    @Test
    void isValid_singleCharUsernameBeforeAt_returnsFalse() {
        // AntiLabs: 1-char username should fail (minimum is 2)
        assertThat(VpaValidator.isValid("a@sbi")).isFalse();
    }

    @Test
    void isValid_multipleAtSigns_isInvalidButGetHandleReturnsEverythingAfterFirstAt() {
        // AntiLabs: document the contract — getHandle on invalid VPA returns raw suffix
        String multiAt = "user@extra@sbi";
        assertThat(VpaValidator.isValid(multiAt)).isFalse();
        // getHandle still works (returns everything after first @) — callers should validate first
        assertThat(VpaValidator.getHandle(multiAt)).contains("extra@sbi");
        // getPspName correctly returns empty because "extra@sbi" is not a known handle
        assertThat(VpaValidator.getPspName(multiAt)).isEmpty();
    }

    @Test
    void isValid_handleExactly2Chars_returnsTrue() {
        assertThat(VpaValidator.isValid("user@ab")).isTrue();
    }

    // --- getUsername() ---

    @Test
    void getUsername_null_returnsEmpty() {
        assertThat(VpaValidator.getUsername(null)).isEmpty();
    }

    @Test
    void getUsername_noAt_returnsEmpty() {
        assertThat(VpaValidator.getUsername("userokhdfcbank")).isEmpty();
    }

    @Test
    void getUsername_validVpa_returnsUsername() {
        assertThat(VpaValidator.getUsername("user@okhdfcbank")).contains("user");
    }

    // --- getHandle() ---

    @Test
    void getHandle_null_returnsEmpty() {
        assertThat(VpaValidator.getHandle(null)).isEmpty();
    }

    @Test
    void getHandle_noAt_returnsEmpty() {
        assertThat(VpaValidator.getHandle("userokhdfcbank")).isEmpty();
    }

    @Test
    void getHandle_validVpa_returnsHandle() {
        assertThat(VpaValidator.getHandle("user@okhdfcbank")).contains("okhdfcbank");
    }

    @Test
    void getHandle_preservesCase() {
        assertThat(VpaValidator.getHandle("user@OkHdfcBank")).contains("OkHdfcBank");
    }

    // --- getPspName() ---

    @Test
    void getPspName_null_returnsEmpty() {
        assertThat(VpaValidator.getPspName(null)).isEmpty();
    }

    @Test
    void getPspName_knownHandle_returnsPspName() {
        assertThat(VpaValidator.getPspName("user@okhdfcbank")).contains("Google Pay (HDFC)");
        assertThat(VpaValidator.getPspName("user@paytm")).contains("Paytm");
        assertThat(VpaValidator.getPspName("user@ybl")).contains("PhonePe (Yes Bank)");
        assertThat(VpaValidator.getPspName("user@upi")).contains("BHIM");
    }

    @Test
    void getPspName_unknownHandle_returnsEmpty() {
        assertThat(VpaValidator.getPspName("user@unknownbank")).isEmpty();
    }

    @Test
    void getPspName_caseInsensitiveHandle() {
        assertThat(VpaValidator.getPspName("user@OkHdfcBank")).contains("Google Pay (HDFC)");
        assertThat(VpaValidator.getPspName("user@PAYTM")).contains("Paytm");
    }

    // --- PspRegistry ---

    @Test
    void pspRegistry_getAllHandles_notEmpty() {
        assertThat(PspRegistry.getAllHandles()).isNotEmpty();
    }

    @Test
    void pspRegistry_getAllHandles_containsKnownHandles() {
        assertThat(PspRegistry.getAllHandles()).contains("okhdfcbank", "paytm", "ybl", "upi");
    }

    @Test
    void pspRegistry_getPspName_null_returnsEmpty() {
        assertThat(PspRegistry.getPspName(null)).isEmpty();
    }

    @Test
    void pspRegistry_getPspName_knownHandle_returnsName() {
        assertThat(PspRegistry.getPspName("apl")).contains("Amazon Pay");
        assertThat(PspRegistry.getPspName("cred")).contains("CRED");
    }
}
