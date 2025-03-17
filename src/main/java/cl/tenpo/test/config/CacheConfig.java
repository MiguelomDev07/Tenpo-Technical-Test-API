package cl.tenpo.test.config;

import cl.tenpo.test.constants.GeneralConstants;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean("cacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(GeneralConstants.CACHE_BASE);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(GeneralConstants.CACHE_DURATION_TIME, TimeUnit.MINUTES)
                .maximumSize(100)
        );
        return cacheManager;
    }

    @Bean("getCacheBean")
    public Cache getCacheBean() {
        return cacheManager().getCache(GeneralConstants.CACHE_BASE);
    }
}
