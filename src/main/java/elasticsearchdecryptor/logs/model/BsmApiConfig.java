package elasticsearchdecryptor.logs.model;

import java.io.Serializable;

public class BsmApiConfig implements Serializable {

    private String keyname;
    private String value;

    public BsmApiConfig() {
    }

    public BsmApiConfig(String keyname, String value) {
        this.keyname = keyname;
        this.value = value;

    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
