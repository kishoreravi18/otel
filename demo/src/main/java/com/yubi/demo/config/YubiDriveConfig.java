package com.yubi.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.BeanDefinition;
import com.yubi.yubidrive.sdk.latest.client.YubiDriveClient;
import com.yubi.yubidrive.sdk.latest.client.YubiDriveClientImpl;


@Configuration
public class YubiDriveConfig {

    @Value("${yubidrive.baseUrl}")
    private String baseUrl;

    @Value("${yubidrive.apiKey}")
    private String apiKey;

    @Value("${yubidrive.productKey}")
    private String productKey;

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public YubiDriveClient yubiDriveClient() {
        return YubiDriveClientImpl.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .productKey(productKey)
                .build();
    }
}
