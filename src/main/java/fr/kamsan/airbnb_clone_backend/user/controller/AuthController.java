package fr.kamsan.airbnb_clone_backend.user.controller;

import java.text.MessageFormat;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.airbnb_clone_backend.user.application.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final UserService userService;
	private final ClientRegistration registration;

	public AuthController(UserService userService,
	                      ClientRegistrationRepository registrationRepository,
	                      OAuth2AuthorizedClientService authorizedClientService) {
	    this.userService = userService;
	    this.registration = registrationRepository.findByRegistrationId("okta");
	}


	@GetMapping("/get-authenticated-user")
	public ResponseEntity<ReadUserDTO> getAuthenticatedUser(@RequestParam boolean forceResync, OAuth2AuthenticationToken authentication){
		OAuth2User user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			userService.syncWithIdp(user, forceResync);
			ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
			return new ResponseEntity<ReadUserDTO>(connectedUser, HttpStatus.OK);
		}
	}
	

	/*Construct a redirect URL to the Identity Provider's logout endpoint */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
    	// Identity Provider's Logout URL Info
        String issuerUri = registration.getProviderDetails().getIssuerUri();
        // Frontend URL
        String originUrl = request.getHeader(HttpHeaders.ORIGIN);
        Object[] params = {issuerUri, registration.getClientId(), originUrl};
        String logoutUrl = MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}", params);
        request.getSession().invalidate();
        return ResponseEntity.ok().body(Map.of("logoutUrl", logoutUrl));
    }

}
