package fr.kamsan.airbnb_clone_backend.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.airbnb_clone_backend.booking.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
