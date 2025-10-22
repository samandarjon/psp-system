package live.akbarov.pspsystem.common.utils;


import lombok.experimental.UtilityClass;

@UtilityClass
public class CardValidation {

    /**
     * Validates a card number using the Luhn algorithm.
     * <p>
     * This method first checks if the card number is not null and matches the pattern
     * of 12-19 digits. If these basic validations pass, it applies the Luhn algorithm
     * to check if the card number is valid.
     * <p>
     * The Luhn algorithm works as follows:
     * <li>1. Starting from the rightmost digit, double the value of every second digit</li>
     * <li>2. If doubling results in a two-digit number, subtract 9 from it</li>
     * <li>3. Sum all the digits</li>
     * <li>4. If the sum is divisible by 10, the card number is valid</li>
     *
     * @param number the card number to validate
     * @return true if the card number is valid, false otherwise
     */
    public static boolean isValidCardNumber(String number) {
        if (number == null || !number.matches("\\d{12,19}")) return false;
        int sum = 0;
        boolean alt = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';
            if (alt) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alt = !alt;
        }
        return sum % 10 == 0;
    }

}
