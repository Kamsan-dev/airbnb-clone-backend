package fr.kamsan.airbnb_clone_backend.listing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;

public interface ListingRepository extends JpaRepository<Listing, Long> {
	
	

}
