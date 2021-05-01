/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * Java config for Springfox swagger documentation plugin
 *
 * @author Vitaliy Fedoriv
 *
 */

@Configuration
@ComponentScan(basePackages="org.springframework.samples.petclinic.rest")
public class ApplicationSwaggerConfig implements WebMvcConfigurer {

    private final String baseUrl;

    public ApplicationSwaggerConfig(
        @Value("${springfox.documentation.swagger-ui.base-url:}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

   @Bean
   public Docket customDocket(){
      return new Docket(DocumentationType.SWAGGER_2)
    		  .select()
              .apis(RequestHandlerSelectors.any())
              .paths(PathSelectors.any())
              .build()
              .apiInfo(getApiInfo());
   }

   private ApiInfo getApiInfo() {
	   return new ApiInfo(
		"REST Petclinic backend Api Documentation",
		"This is REST API documentation of the Spring Petclinic backend. If authentication is enabled, when calling the APIs use admin/admin",
		"1.0",
		"Petclinic backend terms of service",
		new Contact(
				"Vitaliy Fedoriv",
				"https://github.com/spring-petclinic/spring-petclinic-rest",
				"vitaliy.fedoriv@gmail.com"),
		"Apache 2.0",
		"http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
   }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.
            addResourceHandler(baseUrl + "/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(baseUrl + "/swagger-ui/")
            .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }
}
