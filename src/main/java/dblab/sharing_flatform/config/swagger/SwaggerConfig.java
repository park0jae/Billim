package dblab.sharing_flatform.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
public class SwaggerConfig {
    private static final String TITLE = "STR BOOT SharingPlatform";
    private static final String DESCRIPTION = "SharingPlatform REST API Documentation";
    private static final String LICENSE_URL = "https://github.com/Dongwoongkim/Sharing_PlatForm";
    private static final String VERSION = "1.0";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TOKEN = "Bearer Token";
    private static final String HEADER = "header";
    private static final String BASE_PACKAGE = "dblab.sharing_flatform.controller";
    private static final String SCOPE = "global";
    private static final String SCOPE_DESCRIPTION = "global access";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo()) // 2
                .select() // 3
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(List.of(apiKey())) // 4
                .securityContexts(List.of(securityContext())); // 5
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .license("rlaehddnd0422@naver.com")
                .licenseUrl(LICENSE_URL)
                .license("okvv26@naver.com")
                .version(VERSION)
                .build();
    }

    private static ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION, BEARER_TOKEN, HEADER);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .operationSelector(oc -> oc.requestMappingPattern().startsWith("/api/")).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(SCOPE, SCOPE_DESCRIPTION);
        return List.of(new SecurityReference(AUTHORIZATION, new AuthorizationScope[] {authorizationScope}));
    }

}
