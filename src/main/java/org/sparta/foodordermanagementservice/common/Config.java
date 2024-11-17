package org.sparta.foodordermanagementservice.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
