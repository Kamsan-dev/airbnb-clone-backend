package fr.kamsan.airbnb_clone_backend.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;

public record GuestsVO(@NotNull(message = "Guest value must be present") int value) {

}
