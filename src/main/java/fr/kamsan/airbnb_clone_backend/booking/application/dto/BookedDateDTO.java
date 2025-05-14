package fr.kamsan.airbnb_clone_backend.booking.application.dto;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;

public record BookedDateDTO(@NotNull OffsetDateTime startDate, @NotNull OffsetDateTime endDate) {

}
