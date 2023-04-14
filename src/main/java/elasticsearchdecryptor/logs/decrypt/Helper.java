package elasticsearchdecryptor.logs.decrypt;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import elasticsearchdecryptor.logs.cache.BsmSecretKeyCache;

@Service
public class Helper {

    @Autowired
    BsmSecretKeyCache bsmSecretKeyCache;

    public String getGetGet(String bsm_key_name) throws Exception {
        return bsmSecretKeyCache.getBsmApiConfig(bsm_key_name).getValue();
    }

}
