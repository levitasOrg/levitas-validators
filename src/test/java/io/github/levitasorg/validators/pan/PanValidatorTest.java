package io.github.levitasorg.validators.pan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PanValidator}.
 */
class PanValidatorTest {

    // --- Valid PANs by entity type (all use fake/obviously-test patterns) ---
    private static final String PAN_INDIVIDUAL = "AAAPA1234A";     // P = Individual
    private static final String PAN_COMPANY    = "AAACA1234A";     // C = Company
    private static final String PAN_HUF        = "AAAHA1234A";     // H = HUF
    private static final String PAN_FIRM       = "AAAFA1234A";     // F = Firm
    private static final String PAN_TRUST      = "AAATA1234A";     // T = Trust
    private static final String PAN_GOVT       = "AAAGA1234A";     // G = Government
    private static final String PAN_AOP        = "AAAAA1234A";     // A = AOP
    private static final String PAN_BOI        = "AAABA1234A";     // B = BOI
    private static final String PAN_LOCAL      = "AAALA1234A";     // L = Local Authority
    private static final String PAN_JURIDICAL  = "AAAJA1234A";     // J = Artificial Juridical

    // --- isValid() null/empty/wrong length ---

    @Test
    void isValid_null_returnsFalse() {
        assertThat(PanValidator.isValid(null)).isFalse();
    }

    @Test
    void isValid_empty_returnsFalse() {
        assertThat(PanValidator.isValid("")).isFalse();
    }

    @Test
    void isValid_tooShort_returnsFalse() {
        assertThat(PanValidator.isValid("AAAPA123")).isFalse();
    }

    @Test
    void isValid_tooLong_returnsFalse() {
        assertThat(PanValidator.isValid("AAAPA1234AB")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaapa1234a", "AaApA1234A", "AAAPA1234a"})
    void isValid_lowercase_returnsFalse(String pan) {
        assertThat(PanValidator.isValid(pan)).isFalse();
    }

    @Test
    void isValid_invalidEntityChar_returnsFalse() {
        // 4th char 'X' is not a valid entity type
        assertThat(PanValidator.isValid("AAAXA1234A")).isFalse();
    }

    @Test
    void isValid_digitInFirstFiveChars_returnsFalse() {
        assertThat(PanValidator.isValid("A1APA1234A")).isFalse();
    }

    @Test
    void isValid_letterInNumericSection_returnsFalse() {
        assertThat(PanValidator.isValid("AAAPA123AA")).isFalse();
    }

    // --- isValid() valid PANs for each entity type ---

    @Test
    void isValid_individual_true() {
        assertThat(PanValidator.isValid(PAN_INDIVIDUAL)).isTrue();
    }

    @Test
    void isValid_company_true() {
        assertThat(PanValidator.isValid(PAN_COMPANY)).isTrue();
    }

    @Test
    void isValid_huf_true() {
        assertThat(PanValidator.isValid(PAN_HUF)).isTrue();
    }

    @Test
    void isValid_firm_true() {
        assertThat(PanValidator.isValid(PAN_FIRM)).isTrue();
    }

    @Test
    void isValid_trust_true() {
        assertThat(PanValidator.isValid(PAN_TRUST)).isTrue();
    }

    @Test
    void isValid_govt_true() {
        assertThat(PanValidator.isValid(PAN_GOVT)).isTrue();
    }

    @Test
    void isValid_allOtherEntityTypes_true() {
        assertThat(PanValidator.isValid(PAN_AOP)).isTrue();
        assertThat(PanValidator.isValid(PAN_BOI)).isTrue();
        assertThat(PanValidator.isValid(PAN_LOCAL)).isTrue();
        assertThat(PanValidator.isValid(PAN_JURIDICAL)).isTrue();
    }

    // --- getEntityType() ---

    @Test
    void getEntityType_null_returnsEmpty() {
        assertThat(PanValidator.getEntityType(null)).isEmpty();
    }

    @Test
    void getEntityType_tooShort_returnsEmpty() {
        assertThat(PanValidator.getEntityType("AAA")).isEmpty();
    }

    @Test
    void getEntityType_individual_returnsCorrect() {
        assertThat(PanValidator.getEntityType(PAN_INDIVIDUAL))
                .contains(PanEntityType.INDIVIDUAL);
    }

    @Test
    void getEntityType_company_returnsCorrect() {
        assertThat(PanValidator.getEntityType(PAN_COMPANY))
                .contains(PanEntityType.COMPANY);
    }

    @Test
    void getEntityType_huf_returnsCorrect() {
        assertThat(PanValidator.getEntityType(PAN_HUF))
                .contains(PanEntityType.HINDU_UNDIVIDED_FAMILY);
    }

    @Test
    void getEntityType_firm_returnsCorrect() {
        assertThat(PanValidator.getEntityType(PAN_FIRM))
                .contains(PanEntityType.FIRM);
    }

    @Test
    void getEntityType_trust_returnsCorrect() {
        assertThat(PanValidator.getEntityType(PAN_TRUST))
                .contains(PanEntityType.TRUST);
    }

    @Test
    void getEntityType_govt_returnsCorrect() {
        assertThat(PanValidator.getEntityType(PAN_GOVT))
                .contains(PanEntityType.GOVERNMENT);
    }

    // --- Convenience boolean methods ---

    @Test
    void isIndividual_individual_true() {
        assertThat(PanValidator.isIndividual(PAN_INDIVIDUAL)).isTrue();
        assertThat(PanValidator.isIndividual(PAN_COMPANY)).isFalse();
    }

    @Test
    void isCompany_company_true() {
        assertThat(PanValidator.isCompany(PAN_COMPANY)).isTrue();
        assertThat(PanValidator.isCompany(PAN_INDIVIDUAL)).isFalse();
    }

    @Test
    void isHuf_huf_true() {
        assertThat(PanValidator.isHuf(PAN_HUF)).isTrue();
        assertThat(PanValidator.isHuf(PAN_INDIVIDUAL)).isFalse();
    }

    @Test
    void isFirm_firm_true() {
        assertThat(PanValidator.isFirm(PAN_FIRM)).isTrue();
        assertThat(PanValidator.isFirm(PAN_COMPANY)).isFalse();
    }

    @Test
    void isTrust_trust_true() {
        assertThat(PanValidator.isTrust(PAN_TRUST)).isTrue();
        assertThat(PanValidator.isTrust(PAN_INDIVIDUAL)).isFalse();
    }

    @Test
    void isGovernment_govt_true() {
        assertThat(PanValidator.isGovernment(PAN_GOVT)).isTrue();
        assertThat(PanValidator.isGovernment(PAN_INDIVIDUAL)).isFalse();
    }

    @Test
    void nullSafety_allConvenienceMethods_returnFalse() {
        assertThat(PanValidator.isIndividual(null)).isFalse();
        assertThat(PanValidator.isCompany(null)).isFalse();
        assertThat(PanValidator.isHuf(null)).isFalse();
        assertThat(PanValidator.isFirm(null)).isFalse();
        assertThat(PanValidator.isTrust(null)).isFalse();
        assertThat(PanValidator.isGovernment(null)).isFalse();
    }

    // --- PanEntityType ---

    @Test
    void panEntityType_fromCharacter_knownChars() {
        assertThat(PanEntityType.fromCharacter('P')).contains(PanEntityType.INDIVIDUAL);
        assertThat(PanEntityType.fromCharacter('C')).contains(PanEntityType.COMPANY);
        assertThat(PanEntityType.fromCharacter('H')).contains(PanEntityType.HINDU_UNDIVIDED_FAMILY);
        assertThat(PanEntityType.fromCharacter('F')).contains(PanEntityType.FIRM);
        assertThat(PanEntityType.fromCharacter('A')).contains(PanEntityType.ASSOCIATION_OF_PERSONS);
        assertThat(PanEntityType.fromCharacter('T')).contains(PanEntityType.TRUST);
        assertThat(PanEntityType.fromCharacter('B')).contains(PanEntityType.BODY_OF_INDIVIDUALS);
        assertThat(PanEntityType.fromCharacter('L')).contains(PanEntityType.LOCAL_AUTHORITY);
        assertThat(PanEntityType.fromCharacter('J')).contains(PanEntityType.ARTIFICIAL_JURIDICAL_PERSON);
        assertThat(PanEntityType.fromCharacter('G')).contains(PanEntityType.GOVERNMENT);
    }

    @Test
    void panEntityType_fromCharacter_unknownChar_returnsEmpty() {
        assertThat(PanEntityType.fromCharacter('X')).isEmpty();
        assertThat(PanEntityType.fromCharacter('Z')).isEmpty();
        assertThat(PanEntityType.fromCharacter('0')).isEmpty();
    }

    @Test
    void panEntityType_getDescription_nonNull() {
        for (PanEntityType type : PanEntityType.values()) {
            assertThat(type.getDescription()).isNotBlank();
            assertThat(type.getCode()).isNotEqualTo('\0');
        }
    }
}
