package com.educheck.api_gateway.config;

import com.educheck.api_gateway.security.GatewayJwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final GatewayJwtFilter gatewayJwtFilter;

    public WebMvcConfig(GatewayJwtFilter gatewayJwtFilter) {
        this.gatewayJwtFilter = gatewayJwtFilter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gatewayJwtFilter)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register"
                );
    }
}