package elasticsearchdecryptor.logs.model;

import java.io.Serializable;
import java.security.PublicKey;

public class CorpSecretKeyData implements Serializable {

    private String app_name;
    private String secret_key;

    public CorpSecretKeyData() {
    }

    public CorpSecretKeyData(String app_name, String secret_key) {
        this.secret_key = secret_key;
        this.app_name = app_name;

    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getSecret_key() {
        return secret_key;
    }

}
