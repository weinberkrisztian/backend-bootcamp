package com.eazybytes.config;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * /myAccount - Secured /myBalance - Secured /myLoans - Secured /myCards -
	 * Secured /notices - Not Secured /contact - Not Secured
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

		http.headers().frameOptions().sameOrigin()
				.and()
		.cors().configurationSource(new CorsConfigurationSource() {
			@Override public CorsConfiguration getCorsConfiguration(HttpServletRequest
			request) { CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
			config.setAllowedMethods(Collections.singletonList("*"));
			config.setAllowCredentials(true);
			config.setAllowedHeaders(Collections.singletonList("*"));
			config.setMaxAge(3600L); return config;
			}
		}).and()
			.authorizeRequests()
			.antMatchers("/myAccount").hasAnyRole("USER")
			.antMatchers("/myBalance").hasAnyRole("ADMIN")
			.antMatchers("/myLoans").authenticated()
			.antMatchers("/myCards").hasAnyRole("USER", "ADMIN")
			.antMatchers("/notices").permitAll()
			.antMatchers("/contact").permitAll()
			.and().csrf().disable()
			.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter);

	}

}
