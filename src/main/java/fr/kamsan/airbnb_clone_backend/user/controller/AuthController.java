package fr.kamsan.airbnb_clone_backend.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.airbnb_clone_backend.user.application.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final UserService userService;

	public AuthController(UserService userService) {
	    this.userService = userService;
	}


	@GetMapping("/get-authenticated-user")
	public ResponseEntity<ReadUserDTO> getAuthenticatedUser(@RequestParam boolean forceResync, OAuth2AuthenticationToken authentication){
		Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			userService.syncWithIdp(user, forceResync);
			ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
			return new ResponseEntity<ReadUserDTO>(connectedUser, HttpStatus.OK);
		}
	}

}
