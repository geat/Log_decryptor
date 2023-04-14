package elasticsearchdecryptor.logs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import elasticsearchdecryptor.logs.model.BsmApiConfig;
import elasticsearchdecryptor.logs.model.CorpSecretKeyData;

@Service
@Configurable
public class ACacheService {

    @Autowired
    private CacheService cacheService;

    public BsmApiConfig getBsmApiConfigFromCahce(String KEYNAME) throws Exception {
        return cacheService.getBsmApiConfig(KEYNAME);

    }

    public CorpSecretKeyData getCorpSecretKeyData(String app_name) throws Exception {
        return cacheService.getCorpSecretKeyData(app_name);
    }

}
