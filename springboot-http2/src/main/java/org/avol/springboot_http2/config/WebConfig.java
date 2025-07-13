package org.avol.springboot_http2.config;

import org.springframework.lang.NonNull;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

public class WebConfig implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Configure static resources with HTTP/2 push hints
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(org.springframework.http.CacheControl.maxAge(3600, java.util.concurrent.TimeUnit.SECONDS));

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCacheControl(org.springframework.http.CacheControl.maxAge(3600, java.util.concurrent.TimeUnit.SECONDS));

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCacheControl(org.springframework.http.CacheControl.maxAge(3600, java.util.concurrent.TimeUnit.SECONDS));
    }
}
