package fr.kamsan.airbnb_clone_backend.listing.application.dto.sub;

import java.util.Objects;

import jakarta.validation.constraints.NotNull;

public record PictureDTO(@NotNull byte[] file, @NotNull String fileContentType, @NotNull boolean isCover) {

	@Override
	public int hashCode() {
		return Objects.hash(fileContentType, isCover);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PictureDTO other = (PictureDTO) obj;
		return Objects.equals(fileContentType, other.fileContentType) && isCover == other.isCover;
	}

}
