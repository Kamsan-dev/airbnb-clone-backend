package fr.kamsan.airbnb_clone_backend.infrastructure.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.cors")
public class CorsProps {
	private List<String> allowedOrigins;
	private List<String> allowedMethods;
	private List<String> allowedHeaders;
	private List<String> exposedHeaders;
	private Boolean allowCredentials;
	private Long maxAge;
}