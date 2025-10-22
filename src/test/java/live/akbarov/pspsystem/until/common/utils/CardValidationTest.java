package live.akbarov.pspsystem.until.common.utils;

import live.akbarov.pspsystem.common.utils.CardValidation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CardValidationTest {
    @Test
    void shouldReturnTrue_forValidCardNumbers() {
        assertThat(CardValidation.isValidCardNumber("4539578763621486")).isTrue();
        assertThat(CardValidation.isValidCardNumber("4111111111111111")).isTrue();
        assertThat(CardValidation.isValidCardNumber("5105105105105100")).isTrue();
    }

    @Test
    void shouldReturnFalse_forInvalidCardNumbers() {
        assertThat(CardValidation.isValidCardNumber("4539578763621487")).isFalse();
        assertThat(CardValidation.isValidCardNumber("12345678901")).isFalse();
        assertThat(CardValidation.isValidCardNumber("12345678901234567890")).isFalse();
        assertThat(CardValidation.isValidCardNumber("4111-1111-1111-1111")).isFalse();
        assertThat(CardValidation.isValidCardNumber(null)).isFalse();
        assertThat(CardValidation.isValidCardNumber("")).isFalse();
    }
}
