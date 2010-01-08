import java.security.MessageDigest;

/* Class written by Chris Mair for Distributed Systems Labs */

public class MD5Demo {

    public static void main(String[] args) {

        String key = "MARCO";
        byte a[] = key.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte hash[] = md.digest(a);
            for (byte b: hash) {
                System.out.printf("%02x ", b);
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            System.exit(1);
        }

    }
}