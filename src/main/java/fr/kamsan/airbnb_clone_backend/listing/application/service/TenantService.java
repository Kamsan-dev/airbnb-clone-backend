package fr.kamsan.airbnb_clone_backend.listing.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayCardListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.LandlordListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;
import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;
import fr.kamsan.airbnb_clone_backend.listing.mapper.ListingMapper;
import fr.kamsan.airbnb_clone_backend.listing.repository.ListingRepository;
import fr.kamsan.airbnb_clone_backend.sharedkernel.service.State;
import fr.kamsan.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.airbnb_clone_backend.user.application.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService {

	private final ListingRepository listingRepository;
	private final UserService userService;
	private final ListingMapper listingMapper;

	public Page<DisplayCardListingDTO> getAllListingByCategory(Pageable pageable, BookingCategory category) {
		Page<Listing> allOrBookingCategory;
		if (category == BookingCategory.ALL) {
			allOrBookingCategory = listingRepository.findAllWithCoverOnly(pageable);
		} else {
			allOrBookingCategory = listingRepository.findAllByBookingCategoryWithCoverOnly(category, pageable);
		}

		return allOrBookingCategory.map(listing -> listingMapper.listingToDisplayCardListingDTO(listing));

	}

	@Transactional(readOnly = true)
	public State<DisplayListingDTO, String> getListingByPublicId(UUID publicId) {
		Optional<Listing> listing = listingRepository.findByPublicId(publicId);

		if (listing.isEmpty()) {
			return State.<DisplayListingDTO, String>builder()
					.forError(String.format("Listing does not exist for publicId: %s", publicId));
		}

		DisplayListingDTO displayListingDTO = listingMapper.listingtoDisplayListingDTO(listing.get());
		ReadUserDTO readUserDTO = userService.getByPublicId(listing.get().getLandlordPublicId()).orElseThrow();
		LandlordListingDTO landlordListingDTO = new LandlordListingDTO(readUserDTO.firstName(), readUserDTO.imageUrl());
		displayListingDTO.setLandlord(landlordListingDTO);

		return State.<DisplayListingDTO, String>builder().forSuccess(displayListingDTO);

	}

}
