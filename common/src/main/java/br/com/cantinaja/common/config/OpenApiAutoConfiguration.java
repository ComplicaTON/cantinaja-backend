package br.com.cantinaja.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * OpenAPI base compartilhado por todos os módulos. Gera o documento a partir do
 * código (via springdoc) usando o nome da aplicação, sem YAML escrito à mão.
 *
 * <p>Cada módulo herda um {@link OpenAPI} com título = {@code spring.application.name}.
 * Para customizar (descrição, contato, etc.), o módulo pode declarar o próprio
 * bean {@code OpenAPI} — este só entra quando nenhum outro existe
 * ({@link ConditionalOnMissingBean}).
 */
@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
public class OpenApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    OpenAPI defaultOpenAPI(@Value("${spring.application.name:API}") String appName) {
        return new OpenAPI()
                .info(new Info()
                        .title("CantinaJá · " + appName)
                        .version("1.0.0")
                        .description("Documentação do módulo " + appName + " da API CantinaJá"));
    }
}
