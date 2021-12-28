package health.data.java.app.config;

import health.data.java.app.service.CheckupCategoryService;
import health.data.java.app.service.CheckupComponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    private final CacheManager cacheManager;
    private final CheckupCategoryService checkupCategoryService;
    private final CheckupComponentService checkupComponentService;

    public CacheConfig(CacheManager cacheManager,
                       CheckupCategoryService checkupCategoryService,
                       CheckupComponentService checkupComponentService) {
        this.cacheManager = cacheManager;
        this.checkupCategoryService = checkupCategoryService;
        this.checkupComponentService = checkupComponentService;
    }
    @Scheduled(cron = "0 0 0 * * *")
    protected void putAllCache() {
        log.info("Firing Cache Evict!!!");
        cacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());

        log.info("Firing All Cache!!!");
        CompletableFuture.supplyAsync(checkupCategoryService::findCheckupCategories);
        CompletableFuture.supplyAsync(checkupComponentService::findCheckupComponents);
    }
}
