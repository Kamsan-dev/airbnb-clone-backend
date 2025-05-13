package fr.kamsan.airbnb_clone_backend.listing.application.dto;

import java.util.List;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.DescriptionDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.LandlordListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.ListingInfoDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.PictureDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.PriceVO;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DisplayListingDTO {
	
	private DescriptionDTO description;
	private List<PictureDTO> pictures;
	private ListingInfoDTO infos;
	private BookingCategory category;
	private String location;
	private LandlordListingDTO landlord;
	private PriceVO price;
	

}
