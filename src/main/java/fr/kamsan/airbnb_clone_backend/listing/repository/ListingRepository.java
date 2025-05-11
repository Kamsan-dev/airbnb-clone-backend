package fr.kamsan.airbnb_clone_backend.listing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;

public interface ListingRepository extends JpaRepository<Listing, Long> {
	
	@Query("SELECT listing FROM Listing listing JOIN listing.pictures picture "
		     + "WHERE listing.landlordPublicId = :landlordPublicId AND picture.isCover = true")
	List<Listing> findAllByLandLordPublicIdFetchCoverPicture(UUID landlordPublicId);

	Long deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

}
