package site.dqxfz.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author WENG Yang
 * @date 2020年04月14日
 **/
public class Md5Utils {
    /**
     * Encodes a string 2 MD5
     *
     * @param str String to encode
     * @return Encoded String
     * @throws NoSuchAlgorithmException
     */
    public static String crypt(String str) throws NoSuchAlgorithmException {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
        StringBuffer hexString = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] hash = md.digest();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }
}