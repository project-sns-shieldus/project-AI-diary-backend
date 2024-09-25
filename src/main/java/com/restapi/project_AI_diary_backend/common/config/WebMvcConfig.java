package com.restapi.project_AI_diary_backend.common.config;

import com.restapi.project_AI_diary_backend.common.interceptor.AuthorizationInterceptor;
import com.restapi.project_AI_diary_backend.common.interceptor.LoggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    private List<String> OPEN_API = List.of(
            "/open-api/**"
    );

    private List<String> DEFAULT_EXCLUDE = List.of(
            "/",
            "favicon.ico",
            "/error"
    );

    private List<String> SWAGGER = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // LoggerInterceptor 등록
        registry.addInterceptor(new LoggerInterceptor());

        // AuthorizationInterceptor 등록
        registry.addInterceptor(authorizationInterceptor)
                .excludePathPatterns("/public/**", "/open-api/**")
                .excludePathPatterns("/api/v1/register", "/api/v1/login")
                .excludePathPatterns("/static/**", "/css/**", "/js/**")
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true);
    }
}