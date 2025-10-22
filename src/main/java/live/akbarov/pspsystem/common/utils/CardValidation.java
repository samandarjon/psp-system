package live.akbarov.pspsystem.common.utils;


import lombok.experimental.UtilityClass;

@UtilityClass
public class CardValidation {

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
