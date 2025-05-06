package fr.kamsan.airbnb_clone_backend.infrastructure.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
		requestHandler.setCsrfRequestAttributeName(null);
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
				.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
						.csrfTokenRequestHandler(requestHandler))
				.oauth2Login(Customizer.withDefaults())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
				.oauth2Client(Customizer.withDefaults());

		return http.build();
	}

	/* map authorities from idToken to grantedAuthorities to be used inside authentication Object */
	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapper() {
		return authorities -> {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

			authorities.forEach(grantedAuthority -> {
				if (grantedAuthority instanceof OidcUserAuthority oidcUserAuthority) {
					Map<String, Object> userInfoClaims = oidcUserAuthority.getUserInfo().getClaims();
                    System.out.println("==== UserInfo Claims ====");
                    userInfoClaims.forEach((k, v) -> System.out.println(k + ": " + v));
					grantedAuthorities.addAll(
							SecurityUtils.extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
				}
			});
			return grantedAuthorities;

		};
	}

}
