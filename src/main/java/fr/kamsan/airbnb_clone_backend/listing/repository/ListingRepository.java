package fr.kamsan.airbnb_clone_backend.listing.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayCardListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;
import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;

public interface ListingRepository extends JpaRepository<Listing, Long> {
	
	@Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture "
		     + "WHERE listing.landlordPublicId = :landlordPublicId AND picture.isCover = true")
	List<Listing> findAllByLandLordPublicIdFetchCoverPicture(UUID landlordPublicId);

	Long deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);
	
	@Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture "
		     + "WHERE listing.bookingCategory = :bookingCategory AND picture.isCover = true")
	Page<Listing> findAllByBookingCategoryWithCoverOnly(BookingCategory bookingCategory, Pageable pageable);
	
	@Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture "
		     + "WHERE picture.isCover = true")
	Page<Listing> findAllWithCoverOnly(Pageable pageable);
	
	Optional<Listing> findByPublicId(UUID publicId);

	List<Listing> findAllByPublicIdIn(List<UUID> allListingPublicId);

	Optional<Listing> findOneByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId);

}
