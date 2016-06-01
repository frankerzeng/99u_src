package nd.im.im;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mir
 */
public final class MD5Util {
    final static char[] HEX = "0123456789abcdef".toCharArray();
    static MessageDigest md5 = null;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5Util init error.");
        }
    }

    public static String toHexString(byte[] b) {
        if (b == null || b.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX[(b[i] >>> 4) & 0x0F]);
            sb.append(HEX[b[i] & 0x0F]);
        }
        return sb.toString();
    }

    public static byte[] encode(byte[] b) {
        if (b == null) {
            return null;
        }
        return md5.digest(b);
    }

    public static String encode(String s) {
        if (s == null) {
            return null;
        }
        return toHexString(encode(s.getBytes()));
    }

    public static String encode(char s) {
        byte[] b = new byte[2];
        b[0] = (byte) ((s & 0xFF00) >> 8);
        b[1] = (byte) (s & 0xFF);
        return toHexString(encode(b));
    }
}
