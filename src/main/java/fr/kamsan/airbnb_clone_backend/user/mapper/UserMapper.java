package fr.kamsan.airbnb_clone_backend.user.mapper;

import org.mapstruct.Mapper;

import fr.kamsan.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.airbnb_clone_backend.user.domain.Authority;
import fr.kamsan.airbnb_clone_backend.user.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	ReadUserDTO readUserDTOFromUser(User user);
	
	default String mapAuthoritiesToString(Authority authority) {
		return authority.getName();
	}
}
