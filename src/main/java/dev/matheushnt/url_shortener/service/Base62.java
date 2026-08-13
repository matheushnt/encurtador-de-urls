package dev.matheushnt.url_shortener.service;

import org.springframework.stereotype.Component;

@Component
/**
 * Provides encoding and decoding operations using the Base62 numeral system.
 *
 * <p>Base62 uses 62 characters to represent numeric values: {@code 0-9}, {@code a-z}, and {@code A-Z}.
 *
 * <p>This implementation uses the following alphabet:
 * {@code 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ}.
 *
 * <p>The same alphabet must be used for both encoding and decoding.
 */
public class Base62 {

    /**
     * The alphabet used by the Base62 encoding.
     */
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Encodes a non-negative number into its Base62 representation.
     *
     * <p>For example:
     * <pre>encode(123456) -> "w7e"</pre>
     *
     * @param number the non-negative number to encode
     * @return the Base62 representation of the given number
     * @throws IllegalArgumentException if {@code number} is negative
     */
    public String encode(long number) {
        if (number == 0) {
            return "0";
        }

        if (number < 0) {
            throw new IllegalArgumentException("Number cannot be negative");
        }

        StringBuilder sb = new StringBuilder();

        while (number > 0) {
            int remainder = (int) (number % 62);
            sb.append(ALPHABET.charAt(remainder));
            number /= 62;
        }

        return sb.reverse().toString();
    }

    /**
     * Decodes a Base62 string into its numeric representation.
     *
     * <p>For example:
     *
     * <pre>
     * decode("w7e") → 123456
     * </pre>
     *
     * @param value the Base62 string to decode
     * @return the numeric value represented by the given Base62 string
     * @throws IllegalArgumentException if {@code value} is null, empty, or contains an invalid Base62 character
     */
    public long decode(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }

        long result = 0;

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            int indexOfChar = ALPHABET.indexOf(ch);

            if (indexOfChar == -1) {
                throw new IllegalArgumentException("Invalid Base62 character: " + ch);
            }

            result = result * 62 + indexOfChar;
        }

        return result;
    }

}
