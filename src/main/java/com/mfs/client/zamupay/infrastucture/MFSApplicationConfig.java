package com.mfs.client.zamupay.infrastucture;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for configuring bean and other details for Adopter REST
 * API
 */
@Configuration
@EnableSwagger2
public class MFSApplicationConfig {
    /**
     * Configuring the Swagger2 Docket Api for the application
     *
     * @return Docket object
     */
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .apiInfo(apiInfo()).select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.mfs.client.zamupay")).build();
    }

    /**
     * Configuring the Swagger2 Api info for the application
     *
     * @return ApiInfo object
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("MFS ZamuPay Adopter")
                .description("Provide integration between MFS Africa and ZamuPay (KCB Bank)")
                .version("1.0").build();
    }


}
