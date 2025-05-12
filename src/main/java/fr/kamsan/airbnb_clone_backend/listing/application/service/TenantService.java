package fr.kamsan.airbnb_clone_backend.listing.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayCardListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;
import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;
import fr.kamsan.airbnb_clone_backend.listing.mapper.ListingMapper;
import fr.kamsan.airbnb_clone_backend.listing.repository.ListingRepository;
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

}
