package fr.kamsan.airbnb_clone_backend.listing.application.dto;

import java.util.UUID;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.PriceVO;

public record ListingCreateBookingDTO(
		UUID listingPublicId, PriceVO price) {

}
