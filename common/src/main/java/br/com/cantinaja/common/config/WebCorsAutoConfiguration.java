package br.com.cantinaja.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS compartilhado por todos os módulos, para o frontend consumir as APIs.
 * Antes cada módulo tinha um {@code WebConfig} idêntico — agora a regra vive
 * num lugar só e é herdada via auto-configuration da common.
 */
@AutoConfiguration
@ConditionalOnClass(WebMvcConfigurer.class)
public class WebCorsAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
