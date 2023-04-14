package elasticsearchdecryptor.logs.driver;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DriverRequestHeaders {
    String Accept;
    String Connection;
    String Host;
    String Signature;
    String TimeStamp;
    String EndpointUrl;

    public String getAccept() {
        return Accept;
    }

    public String getEndpointUrl() {
        if (EndpointUrl != null) {
            System.out.println(EndpointUrl + "kosong");
            return this.EndpointUrl;

        } else {
            return null;
        }

    }

}
