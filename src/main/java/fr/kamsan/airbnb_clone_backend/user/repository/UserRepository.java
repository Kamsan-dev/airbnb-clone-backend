package fr.kamsan.airbnb_clone_backend.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.airbnb_clone_backend.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findOneByEmail(String email);
	
	Optional<User> findOneByPublicId(UUID publicId);

}
