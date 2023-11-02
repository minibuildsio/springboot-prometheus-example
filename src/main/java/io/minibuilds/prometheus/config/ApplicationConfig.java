package io.minibuilds.prometheus.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new CompositeMeterRegistry();
    }
}
