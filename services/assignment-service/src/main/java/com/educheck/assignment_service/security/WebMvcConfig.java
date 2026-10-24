package com.educheck.assignment_service.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AssignmentJwtInterceptor jwtInterceptor;
    private final AssignmentRoleInterceptor roleInterceptor;

    public WebMvcConfig(
            AssignmentJwtInterceptor jwtInterceptor,
            AssignmentRoleInterceptor roleInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.roleInterceptor = roleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // First: validate the JWT
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/actuator/health",
                        "/actuator/health/**"
                );

        // Second: authorize the user's role
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/assignments/**");
    }
}