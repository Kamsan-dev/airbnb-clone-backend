package fr.kamsan.airbnb_clone_backend.listing.application.dto;

import java.util.List;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.DescriptionDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.ListingInfoDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.PictureDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.PriceVO;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ListingDTO {

	@NotNull
	BookingCategory category;
	
	@NotNull String location;
	
	@NotNull @Valid ListingInfoDTO infos;
	
	@NotNull @Valid DescriptionDTO description;
	
	@NotNull @Valid PriceVO price;
	
	@NotNull @Valid List<PictureDTO> pictures;
	
}
