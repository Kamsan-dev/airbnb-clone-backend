package fr.kamsan.airbnb_clone_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class CorsProperties {

	@Bean
	public CorsConfiguration corsConfiguration(CorsProps props) {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(props.getAllowedOrigins());
		config.setAllowedMethods(props.getAllowedMethods());
		config.setAllowedHeaders(props.getAllowedHeaders());
		config.setExposedHeaders(props.getExposedHeaders());
		config.setAllowCredentials(props.getAllowCredentials());
		config.setMaxAge(props.getMaxAge());
		return config;
	}
}