package fr.kamsan.airbnb_clone_backend.user.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "authority")
public class Authority implements Serializable {
	
	@NotNull
	@Size(max=50)
	@Id
	@Column(length = 50)
	private String name;

}
