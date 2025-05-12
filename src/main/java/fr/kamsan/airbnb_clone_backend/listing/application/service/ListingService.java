package fr.kamsan.airbnb_clone_backend.listing.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.CreatedListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayCardListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.ListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;
import fr.kamsan.airbnb_clone_backend.listing.mapper.ListingMapper;
import fr.kamsan.airbnb_clone_backend.listing.repository.ListingRepository;
import fr.kamsan.airbnb_clone_backend.sharedkernel.service.State;
import fr.kamsan.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.airbnb_clone_backend.user.application.service.Auth0Service;
import fr.kamsan.airbnb_clone_backend.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
	
	@Transactional(readOnly = true)
	public List<DisplayCardListingDTO> getAllPropertiesByPublicId(ReadUserDTO landlord){
		List<Listing> properties = listingRepository.findAllByLandLordPublicIdFetchCoverPicture(landlord.publicId());
		return listingMapper.listingToDisplayCardListingDTOs(properties);
	}
	
	@Transactional
	public State<UUID, String> delete(UUID publicId, ReadUserDTO landlord){
		long deletedSuccessfully = listingRepository.deleteByPublicIdAndLandlordPublicId(publicId, landlord.publicId());
		/* number of rows deleted */
		if (deletedSuccessfully > 0) {
			return State.<UUID, String>builder().forSuccess(publicId);
		} else {
			return State.<UUID, String>builder().forUnauthorized("User not authorized to delete this listing");
		}
	}

}
