package com.dj.p2p.config;

import com.dj.p2p.interceptor.LogCostInterceptor;
import com.dj.p2p.service.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config extends WebMvcConfigurerAdapter {


    //是否开启swagger，正式环境一般是需要关闭的，从配置文件中读取
    @Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled;

    @Bean
    public Docket createRestApi() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
       /* List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("ticket").description("user ticket")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());  */  //根据每个方法名也知道当前方法在设置什么参数

       aParameterBuilder
                .parameterType("header") //参数类型支持header, cookie, body, query etc
                .name("token")  //参数名
                .description("header中字段username测试")
                .modelRef(new ModelRef("string"))//指定参数值的类型
                .required(true).build();

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.basePackage("com.dj.p2p.controller"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any()).build().pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("点金教育Swaggerdemo")
                .description("用户")
                // 作者信息
                .contact(new Contact("yyw", "https://www.dianit.cn", "17631336417@163.com"))
                .version("1.0.0")
                .build();
    }


    @Bean
    public LogCostInterceptor addLogin() {

        return new LogCostInterceptor();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(addLogin()).addPathPatterns("/**")
                .excludePathPatterns("/login","/updateAccById","/findAccByUserId", "/user/login", "/findBaseDataRegisterData", "/addUser", "/findBorrowerData","/getUserData","/locking","/unlocked")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
        super.addInterceptors(registry);
    }

    @Bean
    public RedisServiceImpl addRedis() {

        return new RedisServiceImpl();
    }
}