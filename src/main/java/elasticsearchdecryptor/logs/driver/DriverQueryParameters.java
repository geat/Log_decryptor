package elasticsearchdecryptor.logs.driver;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import lombok.Value;

@Value
public class DriverQueryParameters {
    String Account_No;
    String Corp_ID;
    String DepositoNumber;
    String Prefix;
    String Tanggal_Akhir;
    String Tanggal_Awal;
    String Deposito_Number;
    String companyCode;
    String VA_Number;
    String billerId;
    String corpId;
    String currencyCode;
    String billtx_amterId;
    String Status_Message;
    String TransactionID;
    String Transaction_ID;
    String transactionId;

    public String getCorp_ID() {
        return Corp_ID;
        // return decrypt(Corp_ID, null;
    }

    public String getcorpId() {
        // return decrypt(corpId, null);
        return corpId;
    }

    public String getTanggal_Awal() {
        // return decrypt(Tanggal_Awal, null);
        return Tanggal_Awal;
    }

    public String getTanggal_Akhir() {
        // return decrypt(Tanggal_Akhir, null);
        return Tanggal_Akhir;
    }

    public String getAccount_No() {
        // return decrypt(Account_No, null);
        return Account_No;
    }

    public String getStatus_Message() {

        // return decrypt(Status_Message, null);
        return Status_Message;
    }

    // hardcode not used
    public static String decrypt(final String strToDecrypt, final String secret) {
        String textToDecrypt = strToDecrypt;
        String secretKey = "m4nd1r1sy4r14hku";
        String hasilDecrypt = null;

        if (textToDecrypt != null) {
            if (textToDecrypt.length() > 16) {
                try {
                    SecretKeySpec sskey2 = new SecretKeySpec(secretKey.getBytes(), "AES");
                    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                    cipher.init(Cipher.DECRYPT_MODE, sskey2);
                    byte[] decrypted = cipher.doFinal(Hex.decodeHex(textToDecrypt));
                    hasilDecrypt = new String(decrypted);
                    return hasilDecrypt;
                    // hasil2 = new String(cipher.doFinal(Base64.getDecoder().decode(text)));
                } catch (Exception e) {
                    System.out.println("Error while decrypting: " + e.toString());
                }
            }
        }

        return hasilDecrypt;
    }

}
