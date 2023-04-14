package elasticsearchdecryptor.logs.decrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Decryptor {
    public String decryptAES128(final String strToDecrypt, final String secret) {
        String textToDecrypt = strToDecrypt;
        String hasilDecrypt = null;
        String secretKey = null;
        if (secret != null && strToDecrypt != null) {
            secretKey = secret;

            try {
                SecretKeySpec sskey2 = new SecretKeySpec(secretKey.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, sskey2);
                byte[] decrypted = cipher.doFinal(Hex.decodeHex(textToDecrypt));
                hasilDecrypt = new String(decrypted);
                // hasil2 = new String(cipher.doFinal(Base64.getDecoder().decode(text)));
            } catch (Exception e) {
                System.out.println("Error while decrypting: " + e.toString());
                return textToDecrypt;
            }
        } else {

        }

        return hasilDecrypt;
    }
}
