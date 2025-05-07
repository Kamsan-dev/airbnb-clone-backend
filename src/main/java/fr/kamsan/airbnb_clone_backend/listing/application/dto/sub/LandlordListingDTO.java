package fr.kamsan.airbnb_clone_backend.listing.application.dto.sub;

import jakarta.validation.constraints.NotNull;

public record LandlordListingDTO(@NotNull String firstName, @NotNull String imageUrl) {

}
