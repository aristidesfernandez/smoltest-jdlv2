package co.com.ies.smol.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, co.com.ies.smol.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, co.com.ies.smol.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, co.com.ies.smol.domain.User.class.getName());
            createCache(cm, co.com.ies.smol.domain.Authority.class.getName());
            createCache(cm, co.com.ies.smol.domain.User.class.getName() + ".authorities");
            createCache(cm, co.com.ies.smol.domain.EventType.class.getName());
            createCache(cm, co.com.ies.smol.domain.Establishment.class.getName());
            createCache(cm, co.com.ies.smol.domain.EventDevice.class.getName());
            createCache(cm, co.com.ies.smol.domain.DeviceEstablishment.class.getName());
            createCache(cm, co.com.ies.smol.domain.CounterType.class.getName());
            createCache(cm, co.com.ies.smol.domain.CounterEvent.class.getName());
            createCache(cm, co.com.ies.smol.domain.DeviceType.class.getName());
            createCache(cm, co.com.ies.smol.domain.DeviceCategory.class.getName());
            createCache(cm, co.com.ies.smol.domain.Isle.class.getName());
            createCache(cm, co.com.ies.smol.domain.CurrencyType.class.getName());
            createCache(cm, co.com.ies.smol.domain.Device.class.getName());
            createCache(cm, co.com.ies.smol.domain.CounterDevice.class.getName());
            createCache(cm, co.com.ies.smol.domain.Manufacturer.class.getName());
            createCache(cm, co.com.ies.smol.domain.Formula.class.getName());
            createCache(cm, co.com.ies.smol.domain.Model.class.getName());
            createCache(cm, co.com.ies.smol.domain.InterfaceBoard.class.getName());
            createCache(cm, co.com.ies.smol.domain.DeviceInterface.class.getName());
            createCache(cm, co.com.ies.smol.domain.Operator.class.getName());
            createCache(cm, co.com.ies.smol.domain.OperationalPropertiesEstablishment.class.getName());
            createCache(cm, co.com.ies.smol.domain.UserAccess.class.getName());
            createCache(cm, co.com.ies.smol.domain.Municipality.class.getName());
            createCache(cm, co.com.ies.smol.domain.Province.class.getName());
            createCache(cm, co.com.ies.smol.domain.Country.class.getName());
            createCache(cm, co.com.ies.smol.domain.KeyOperatingProperty.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
