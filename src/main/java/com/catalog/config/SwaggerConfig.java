package com.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@PropertySource("classpath:swagger.properties")
public class SwaggerConfig {

    @Value("${swagger.app.title}")
    String SWAGGER_TITLE;

    @Value("${swagger.basepackage}")
    String basePackage;

    @Value("${app.version}")
    String API_VERSION;

    @Value("${swagger.app.description}")
    String SWAGGER_APP_DESCRIPTION;

    @Value("${swagger.contact.name}")
    String contactName;
    @Value("${swagger.contact.url}")
    String url;
    @Value("${swagger.contact.email}")
    String email;

    @Bean
    public Docket productApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title(SWAGGER_TITLE)
                        .description(SWAGGER_APP_DESCRIPTION)
                        .version(API_VERSION)
                        .contact(new Contact(contactName, url, email))
                        .build());
    }
}
