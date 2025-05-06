package fr.kamsan.airbnb_clone_backend.user.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import fr.kamsan.airbnb_clone_backend.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "airbnb_user")
public class User extends AbstractAuditingEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
	@SequenceGenerator(name = "userSequenceGenerator", sequenceName = "user_generator", allocationSize = 1)
	private Long id;

	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "email")
	private String email;
	@Column(name = "image_url")
	private String imageUrl;

    @UuidGenerator
	@Column(name = "public_id", nullable = false)
	private UUID publicId;
	
	@ManyToMany
	@JoinTable(
	    name = "user_authority",
	    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
	    inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
	)
	private Set<Authority> authorities = new HashSet<>();
}
