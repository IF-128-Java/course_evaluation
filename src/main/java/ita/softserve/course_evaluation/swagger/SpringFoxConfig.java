package ita.softserve.course_evaluation.swagger;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ita.softserve.course_evaluation.constants.SwaggerConstants.*;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).forCodeGeneration(true)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("ita.softserve.course_evaluation.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(API_TITLE, API_DESCRIPTION, API_VERSION, TERM_OF_SERVICE,
                getContact(), LICENSE, LICENSE_URL, Collections.emptyList());
    }

    private Contact getContact() {
        return new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL);
    }

    private ApiKey apiKey() {
        return new ApiKey(SECURITY_REFERENCE, AUTHORIZATION, SecurityScheme.In.HEADER.name());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(AUTHORIZATION_SCOPE, AUTHORIZATION_DESCRIPTION);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(SECURITY_REFERENCE, authorizationScopes));
    }
}
