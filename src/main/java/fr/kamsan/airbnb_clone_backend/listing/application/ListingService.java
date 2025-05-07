package fr.kamsan.airbnb_clone_backend.listing.application;

import org.springframework.stereotype.Service;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.CreatedListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.ListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;
import fr.kamsan.airbnb_clone_backend.listing.mapper.ListingMapper;
import fr.kamsan.airbnb_clone_backend.listing.repository.ListingRepository;
import fr.kamsan.airbnb_clone_backend.user.application.Auth0Service;
import fr.kamsan.airbnb_clone_backend.user.application.UserService;
import fr.kamsan.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListingService {
	
	
	private final ListingRepository listingRepository;
	private final UserService userService;
	private final Auth0Service auth0Service;
	private final ListingMapper listingMapper;
	private final PictureService pictureService;
	
	
	public CreatedListingDTO create(ListingDTO listingDTO) {
		/* set publicId of landlord to the new listing */
		Listing newListing = listingMapper.listingDTOToListing(listingDTO);
		ReadUserDTO userConnected = userService.getAuthenticatedUserFromSecurityContext();
		newListing.setLandlordPublicId(userConnected.publicId());
		
		/* Save new listing and his pictures */
		Listing savedListing = listingRepository.saveAndFlush(newListing);
		pictureService.saveAll(listingDTO.getPictures(), savedListing);
		
		/* Set user to landlord */
		auth0Service.addLandlordRoleToUser(userConnected);
		
		return listingMapper.listingToCreatedListingDTO(savedListing);
	}

}
