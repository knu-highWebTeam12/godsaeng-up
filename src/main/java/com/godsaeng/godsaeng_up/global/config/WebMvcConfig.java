package com.godsaeng.godsaeng_up.global.config;

import com.godsaeng.godsaeng_up.global.interceptor.CsrfInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final CsrfInterceptor csrfInterceptor;
    private final String fileDir;

    public WebMvcConfig(CsrfInterceptor csrfInterceptor,
                        @Value("${file.dir}") String fileDir) {
        this.csrfInterceptor = csrfInterceptor;
        this.fileDir = fileDir;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(csrfInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/mission/**")
                .addResourceLocations(Path.of(fileDir).toUri().toString());
    }
}
