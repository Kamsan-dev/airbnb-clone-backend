package fr.kamsan.airbnb_clone_backend.listing.application.dto;

import java.util.UUID;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.PictureDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.PriceVO;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;

public record DisplayCardListingDTO(PriceVO price, String location, PictureDTO cover, BookingCategory bookingCategory, UUID publicId ) {

}
