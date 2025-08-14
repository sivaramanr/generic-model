package io.github.sivaramanr.genericmodel.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Configuration
public class SwaggerConfig {

    private static final String OPENAPI_SWAGGER_PREFIX = "openapi.swagger.apiInfo.genericModel.";
    private static final String EXAMPLE = "example";
    private static final String OPENAPI_EXAMPLE_SUFFIX = "." + EXAMPLE;

    @Autowired
    private Environment environment;

    @Value("${openapi.swagger.apiInfo.title}")
    private String title;

    @Value("${openapi.swagger.apiInfo.title}")
    private String description;

    @Value("${openapi.swagger.apiInfo.version}")
    private String version;

    @Value("${openapi.swagger.apiInfo.contactName}")
    private String contactName;

    @Value("${openapi.swagger.apiInfo.contactEmail}")
    private String contactEmail;

    @Value("${openapi.swagger.server.localhost.url}")
    private String localhostUrl;

    @Value("${openapi.swagger.server.localhost.description}")
    private String localhostDescription;

    @Value("${openapi.swagger.server.dev.url}")
    private String devUrl;

    @Value("${openapi.swagger.server.dev.description}")
    private String devDescription;

    @Value("${openapi.swagger.oauth2.token.url}")
    private String oauth2TokenUrl;

//    @Value("${openapi.swagger.apiInfo.genericModel.create.example}")
    private String createSummary;

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer")
                .addList(String.valueOf(new Scopes().addString("global", "access all APIs")));
        return new OpenAPI(SpecVersion.V30).info(apiInfo()).externalDocs(new ExternalDocumentation()).servers(servers())
                .security(Collections.singletonList(securityRequirement))
                .components(new Components().addSecuritySchemes("Bearer",
                        new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .flows(new OAuthFlows().password(new OAuthFlow().tokenUrl(oauth2TokenUrl)))));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi ->
            openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperations().forEach(operation ->
                    ofNullable(operation)
                    .map(Operation::getOperationId)
                    .map(key -> OPENAPI_SWAGGER_PREFIX + key + OPENAPI_EXAMPLE_SUFFIX)
                    .map(environment::getProperty)
                    .ifPresent(parts -> addExamples(parts, operation))
                )
            );
    }

    private void addExamples(String parts, Operation operation)
    {
        for (String part : parts.split(","))
        {
            if ("request".equalsIgnoreCase(part))
            {
                String examples = environment.getProperty(OPENAPI_SWAGGER_PREFIX + operation.getOperationId() + "."+ EXAMPLE + "." + part);
                ofNullable(operation.getRequestBody())
                        .map(RequestBody::getContent)
                        .ifPresent(bodyContent -> addRequestExamples(bodyContent, examples, operation.getOperationId()));
            }
            if ("response".equalsIgnoreCase(part))
            {
                Content bodyContent = new Content();
                String examples = environment.getProperty(OPENAPI_SWAGGER_PREFIX + operation.getOperationId() + "."+ EXAMPLE + "." + part);
                ApiResponse response = new ApiResponse();
                response.description("Example").content(bodyContent);
                addResponseExamples(bodyContent, examples, operation.getOperationId());
                operation.getResponses().addApiResponse("200", response);
            }
        }
    }

    private void addRequestExamples(Content bodyContent, String examples, String operationId)
    {
        bodyContent.forEach((mediaType, content) -> {
            for (String example : examples.split(","))
            {
                String value = environment.getProperty(OPENAPI_SWAGGER_PREFIX + operationId + "." + EXAMPLE + ".request." + example);
                content.addExamples(example, new Example().value(value));
            }
        });
    }

    private void addResponseExamples(Content bodyContent, String examples, String operationId)
    {
        MediaType mediaType = new MediaType();
        for (String example : examples.split(","))
        {
            String value = environment.getProperty(OPENAPI_SWAGGER_PREFIX + operationId + "." + EXAMPLE + ".response." + example);
            mediaType.addExamples(example, new Example().value(value));
        }
        bodyContent.addMediaType("application/json", mediaType);
    }

    private Info apiInfo() {
        return new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(getContact());
    }

    private Contact getContact() {
        return new Contact()
                .name(contactName)
                .email(contactEmail);
    }

    private List<Server> servers() {
        final Server localhostServer = new Server();
        localhostServer.url(localhostUrl);
        localhostServer.description(localhostDescription);
        final Server devServer = new Server();
        devServer.url(devUrl);
        devServer.description(devDescription);
        return List.of(localhostServer, devServer);
    }

}
