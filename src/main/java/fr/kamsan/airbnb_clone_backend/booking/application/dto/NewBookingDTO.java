package fr.kamsan.airbnb_clone_backend.booking.application.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record NewBookingDTO(@NotNull OffsetDateTime startDate, @NotNull OffsetDateTime endDate, @NotNull UUID listingPublicId) {
}
