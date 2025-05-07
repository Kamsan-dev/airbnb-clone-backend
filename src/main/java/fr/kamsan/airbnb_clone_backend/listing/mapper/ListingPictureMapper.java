package fr.kamsan.airbnb_clone_backend.listing.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.PictureDTO;
import fr.kamsan.airbnb_clone_backend.listing.domain.ListingPicture;

@Mapper(componentModel = "spring")
public interface ListingPictureMapper {

	Set<ListingPicture> pictureDTOtoListingPicture(List<PictureDTO> pictureDTO);
	
	List<PictureDTO> listingPicturetoPictureDTO(Set<ListingPicture> listingPictures);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "listing", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "cover", source = "isCover")
    ListingPicture pictureDTOToListingPicture(PictureDTO pictureDTO);
    
    @Mapping(target = "isCover", source = "cover")
    PictureDTO convertToPictureDTO(ListingPicture listingPicture);

    @Named("extract-cover")
    default PictureDTO extractCover(Set<ListingPicture> pictures) {
        return pictures.stream().findFirst().map(this::convertToPictureDTO).orElseThrow();
    }

}
