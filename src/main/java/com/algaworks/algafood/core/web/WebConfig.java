package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Autowired
    private ApiDeprecationHandler apiDeprecationHandler;
	
	/*@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")			
			.allowedMethods("*");
			//.allowedOrigins("*")
			//.maxAge(30)
	}*/
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiDeprecationHandler);
	}
	
	/*@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(AlgaMediaTypes.V2_APPLICATION_JSON);
	}*/

	
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
}
