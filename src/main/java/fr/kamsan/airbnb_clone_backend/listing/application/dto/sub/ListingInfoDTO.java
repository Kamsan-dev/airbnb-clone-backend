package fr.kamsan.airbnb_clone_backend.listing.application.dto.sub;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.BathsVO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.BedroomsVO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.BedsVO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.GuestsVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ListingInfoDTO(@NotNull @Valid GuestsVO guests, @NotNull @Valid BedroomsVO bedrooms,
		@NotNull @Valid BedsVO beds, @NotNull @Valid BathsVO baths) {

}
