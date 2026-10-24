package com.educheck.classroom.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ClassroomJwtInterceptor jwtInterceptor;
    private final ClassroomRoleInterceptor roleInterceptor;

    public WebMvcConfig(
            ClassroomJwtInterceptor jwtInterceptor,
            ClassroomRoleInterceptor roleInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.roleInterceptor = roleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/actuator/health",
                        "/actuator/health/**"
                );

        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/classrooms/**");
    }
}