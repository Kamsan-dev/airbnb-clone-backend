package fr.kamsan.airbnb_clone_backend.listing.application.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.PictureDTO;
import fr.kamsan.airbnb_clone_backend.listing.domain.Listing;
import fr.kamsan.airbnb_clone_backend.listing.domain.ListingPicture;
import fr.kamsan.airbnb_clone_backend.listing.mapper.ListingPictureMapper;
import fr.kamsan.airbnb_clone_backend.listing.repository.ListingPictureRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PictureService {
	
	private final ListingPictureRepository listingPictureRepository;
	private final ListingPictureMapper listingPictureMapper;
	
	
	public List<PictureDTO> saveAll(List<PictureDTO> pictures, Listing listing){
		Set<ListingPicture> newPictures = listingPictureMapper.pictureDTOtoListingPicture(pictures);
		System.out.println("Number of picture from DTO list : " + pictures.size());
		System.out.println("Number of picture after mapping : " + newPictures.size());
		
		boolean isFirst = true;
		for(ListingPicture picture : newPictures) {
			picture.setCover(isFirst);
			picture.setListing(listing);
			isFirst = false;
		}
		listingPictureRepository.saveAll(newPictures);
		return listingPictureMapper.listingPicturetoPictureDTO(newPictures);		
	}

}
