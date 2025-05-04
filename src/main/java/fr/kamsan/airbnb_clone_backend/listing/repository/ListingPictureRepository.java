package fr.kamsan.airbnb_clone_backend.listing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.airbnb_clone_backend.listing.domain.ListingPicture;

public interface ListingPictureRepository extends JpaRepository<ListingPicture, Long>{

}
