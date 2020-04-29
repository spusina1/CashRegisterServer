package ba.unsa.etf.si.local_server.configurations;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("ba.unsa.etf.si.local_server.controllers"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Cash register server")
                .description("Spring Boot REST API\n\nThe cash register server is a local server with which the cash register and the seller mobile application establish communication. This server is online with its own database. If the main server goes down, the cash registers should be able to work using the information from the local server. The cash register server gets all the necessary information from the main server (about products and users). When the user logs in to the desktop or the mobile application, the local server forwards the data to the main server, from which it receives an answer(affirmative if the user data is correct) after which the user-login is either allowed or denied. When the employee logs in, he has the ability to open a new fiscal bill and add/delete bill items. All the information about currently open bills which weren't payed for, and the bills that were generated with the help of the seller mobile application, are temporarily kept on the server until they are confirmed(payed for) or rejected. If the cash register employee closes the account after previously being payed, the information about the payment are saved in the database. The bills that are being payed with the help of the Pay App should immediately be sent to the main server, others - at the end of the day. During the work, when selling prodcuts, the information about their quantity is edited on the server. All successful transactions during the day and new data are sent to the main server.")
                .version("1.0.0")
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }
}
