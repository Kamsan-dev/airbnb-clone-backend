package fr.kamsan.airbnb_clone_backend.booking.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.kamsan.airbnb_clone_backend.booking.domain.Booking;
import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	/*
	 * checks whether any existing booking for the given listing overlaps with the
	 * given time interval (startDate to endDate).
	 */
	@Query("""
			SELECT CASE WHEN COUNT(booking) > 0 THEN true ELSE false END
			FROM Booking booking
			WHERE NOT (
			    booking.startDate >= :endDate OR booking.endDate <= :startDate
			)
			AND booking.fkListing = :fkListing
			""")
	boolean bookingExistsAtInterval(OffsetDateTime startDate, OffsetDateTime endDate, UUID fkListing);

	List<Booking> findAllByFkListing(UUID fkListing);

	List<Booking> findAllByFkTenant(UUID fkTenant);

	int deleteBookingByFkTenantAndPublicId(UUID tenantPublicId, UUID bookingPublicId);

	int deleteBookingByPublicIdAndFkListing(UUID bookingPublicId, UUID listingPublicId);

	List<Booking> findAllByFkListingIn(List<UUID> allPropertiesByPublicIds);

	/* Gathering uuid of bookings that overlaps user search dates and match user listing criteria */
	@Query("SELECT booking FROM Booking booking WHERE NOT "
			+ "(booking.startDate >= :endDate OR booking.endDate <= :startDate) "
			+ "AND booking.fkListing IN :fkListings")
	List<Booking> findAllMatchWithDate(List<UUID> fkListings, OffsetDateTime startDate, OffsetDateTime endDate);
}
