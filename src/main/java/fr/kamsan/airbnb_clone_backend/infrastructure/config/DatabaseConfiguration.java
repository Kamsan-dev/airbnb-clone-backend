package fr.kamsan.airbnb_clone_backend.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "fr.kamsan.airbnb_clone_backend.user.repository",
		"fr.kamsan.airbnb_clone_backend.listing.repository", "fr.kamsan.airbnb_clone_backend.booking.repository" })
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {

}
