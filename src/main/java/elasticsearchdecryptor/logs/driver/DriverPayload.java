package elasticsearchdecryptor.logs.driver;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import lombok.Value;

@Value
public class DriverPayload {
  String Status_Message;

  public static String decrypt(final String strToDecrypt, final String secret) {
    String textToDecrypt = strToDecrypt;
    String secretKey = "m4nd1r1sy4r14hku";
    String hasilDecrypt = null;
    try {
      SecretKeySpec sskey2 = new SecretKeySpec(secretKey.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, sskey2);
      byte[] decrypted = cipher.doFinal(Hex.decodeHex(textToDecrypt));
      hasilDecrypt = new String(decrypted);
      // hasil2 = new String(cipher.doFinal(Base64.getDecoder().decode(text)));
    } catch (Exception e) {
      System.out.println("Error while decrypting: " + e.toString());
    }
    return hasilDecrypt;
  }

}
