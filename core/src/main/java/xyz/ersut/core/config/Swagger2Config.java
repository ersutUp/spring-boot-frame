package xyz.ersut.core.config;

import xyz.ersut.core.util.verify.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;


/**
 * api可视化工具配置
 * @author 王二飞
 */
//@Configuration
//@EnableSwagger2
public class Swagger2Config{

    @Value("${project.legal.referrer:}")
    private String[] legalReferrer;
    @Bean
    public Docket createRestApi() {

        ParameterBuilder ticketPar = new ParameterBuilder();
        ParameterBuilder payuserauth = new ParameterBuilder();
        ParameterBuilder timestamp = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("Referrer")
                .description("referrer值")
                .modelRef(new ModelRef("string"))
                .defaultValue(ObjectUtils.isNull(legalReferrer) ? "127.0.0.1" : legalReferrer[0])
                .parameterType("header")
                .required(true).build();
        payuserauth.name("payuserauth")
                .modelRef(new ModelRef("string"))
                .defaultValue("7a4e2fe710b669f7506eb2552cd3b4be")
                .parameterType("header")
                .required(true).build();
        timestamp.name("timestamp")
                .modelRef(new ModelRef("string"))
                .defaultValue(String.valueOf(System.currentTimeMillis()))
                .parameterType("header")
                .required(true).build();
        pars.add(ticketPar.build());
        pars.add(payuserauth.build());
        pars.add(timestamp.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("xyz.ersut"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("frame API")
                .description("frame-api")
                .version("1.0")
                .build();
    }


}
