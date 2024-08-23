package com.algaworks.algafood.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    //.httpBasic()
		    //.and()
		    //.authorizeRequests()	
		    //.antMatchers(HttpMethod.POST,"/v1/cozinhas/**").hasAnyAuthority("EDITAR_COZINHAS")
		    //.antMatchers(HttpMethod.PUT,"/v1/cozinhas/**").hasAnyAuthority("EDITAR_COZINHAS")
		    //.antMatchers(HttpMethod.GET,"/v1/cozinhas/**").authenticated()
		    //	.anyRequest().denyAll()
		    	//.anyRequest().authenticated()
		    //.and()
			.formLogin().loginPage("/login")
			.and()
		    .authorizeRequests()
		    	.antMatchers("/oauth/**").authenticated()
		    .and()	
		    .csrf().disable()
		    .cors().and()
		    //.oauth2ResourceServer().opaqueToken();
		    .oauth2ResourceServer()
		    .jwt()
		    .jwtAuthenticationConverter(jwtAuthenticationConverter());	    	
	}
	
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			var authorities = jwt.getClaimAsStringList("authorities");
			
			if (authorities == null) {
				authorities = Collections.emptyList();
			}
			
			var scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);
			
			grantedAuthorities.addAll(authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));
			
			return grantedAuthorities;
		});
		
		return jwtAuthenticationConverter;
	}
	
	/*@Bean
	public JwtDecoder jwtDecoder() {
		var secretKey = new SecretKeySpec("89a7sd89f7as98f7dsa98fds7fd89sasd9898asdf98s".getBytes(), "HmacSHA256");
		
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}*/

	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {	
		return super.authenticationManager();
	}
}
