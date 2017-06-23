package com.absurd.rick.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @author Absurd.
 */
public class UUIDUtil {
    private static char hexDigits[] = { '0', '1', '2', '3', '4',
                                        '5', '6', '7', '8', '9',
                                        'A', 'B', 'C', 'D', 'E', 'F' };

    private static char charDigits[] = {
            '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'};
    public static String getID(){
        UUID uuid = UUID.randomUUID();
        return convertToHashStr(uuid.getMostSignificantBits(),5)
                + convertToHashStr(uuid.getLeastSignificantBits(),5)+ ThreadLocalRandom.current().nextInt(10);
    }

    public static String convertToHashStr(long hid, int len) {
        StringBuffer sb = new StringBuffer();

        for(int i=0; i<len; i++) {
            char c = charDigits[(int) ((hid&0xff) % charDigits.length)];
            sb.append(c);
            hid = hid >> 6;
        }

        return sb.toString();
    }
}
