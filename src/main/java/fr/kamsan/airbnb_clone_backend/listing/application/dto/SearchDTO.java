package fr.kamsan.airbnb_clone_backend.listing.application.dto;

import fr.kamsan.airbnb_clone_backend.booking.application.dto.BookedDateDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.ListingInfoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SearchDTO(@Valid BookedDateDTO dates, @Valid ListingInfoDTO infos, @NotEmpty String location) {

}
