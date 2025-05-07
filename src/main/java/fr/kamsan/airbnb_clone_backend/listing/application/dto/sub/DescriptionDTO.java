package fr.kamsan.airbnb_clone_backend.listing.application.dto.sub;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.DescriptionVO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.TitleVO;
import jakarta.validation.constraints.NotNull;

public record DescriptionDTO(@NotNull TitleVO title, @NotNull DescriptionVO description) {

}
