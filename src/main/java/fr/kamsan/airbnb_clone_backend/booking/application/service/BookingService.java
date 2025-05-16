package fr.kamsan.airbnb_clone_backend.booking.application.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.kamsan.airbnb_clone_backend.booking.application.dto.BookedDateDTO;
import fr.kamsan.airbnb_clone_backend.booking.application.dto.BookedListingDTO;
import fr.kamsan.airbnb_clone_backend.booking.application.dto.NewBookingDTO;
import fr.kamsan.airbnb_clone_backend.booking.domain.Booking;
import fr.kamsan.airbnb_clone_backend.booking.mapper.BookingMapper;
import fr.kamsan.airbnb_clone_backend.booking.repository.BookingRepository;
import fr.kamsan.airbnb_clone_backend.infrastructure.config.SecurityUtils;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayCardListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.ListingCreateBookingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.vo.PriceVO;
import fr.kamsan.airbnb_clone_backend.listing.application.service.ListingService;
import fr.kamsan.airbnb_clone_backend.sharedkernel.service.State;
import fr.kamsan.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.airbnb_clone_backend.user.application.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;
	private final BookingMapper bookingMapper;
	private final UserService userService;
	private final ListingService listingService;

	@Transactional()
	public State<Void, String> create(NewBookingDTO newBookingDTO) {
		Booking booking = bookingMapper.newBookingDTOToBooking(newBookingDTO);
		Optional<ListingCreateBookingDTO> listingOpt = listingService
				.getByListingPublicId(newBookingDTO.listingPublicId());

		if (listingOpt.isEmpty()) {
			return State.<Void, String>builder().forError("Listing public id not found");
		}

		boolean alreadyBooked = bookingRepository.bookingExistsAtInterval(newBookingDTO.startDate(),
				newBookingDTO.endDate(), newBookingDTO.listingPublicId());
		if (alreadyBooked) {
			return State.<Void, String>builder().forError("One booking already exists");
		}

		ListingCreateBookingDTO listingCreateBookingDTO = listingOpt.get();
		booking.setFkListing(listingCreateBookingDTO.listingPublicId());
		ReadUserDTO userConnected = userService.getAuthenticatedUserFromSecurityContext();
		booking.setFkTenant(userConnected.publicId());
		booking.setNumberOfTravelers(1);

		/* Setting total price base on price / day && the number of nights booked */
		long numberOfNights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
		booking.setTotalPrice((int) (numberOfNights * listingCreateBookingDTO.price().value()));

		bookingRepository.save(booking);

		return State.<Void, String>builder().forSuccess();
	}

	@Transactional(readOnly = true)
	public List<BookedDateDTO> checkAvailability(UUID publicId) {
		return bookingRepository.findAllByFkListing(publicId).stream().map(bookingMapper::bookingRangeDates).toList();
	}

	@Transactional(readOnly = true)
	public List<BookedListingDTO> getAllBookedListing() {
		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		List<Booking> allBookings = bookingRepository.findAllByFkTenant(connectedUser.publicId());
		List<UUID> allListingPublicIDs = allBookings.stream().map(Booking::getFkListing).toList();
		List<DisplayCardListingDTO> allListings = listingService.getCardDisplayByListingPublicId(allListingPublicIDs);
		return mapBookingToBookedListing(allBookings, allListings);
	}

	private List<BookedListingDTO> mapBookingToBookedListing(List<Booking> allBookings,
			List<DisplayCardListingDTO> allListings) {

		return allBookings.stream().map(booking -> {
			DisplayCardListingDTO displayCardListingDTO = allListings.stream()
					.filter(listing -> listing.publicId().equals(booking.getFkListing())).findFirst().orElseThrow();
			BookedDateDTO dates = bookingMapper.bookingRangeDates(booking);
			return new BookedListingDTO(displayCardListingDTO.cover(), displayCardListingDTO.location(), dates,
					new PriceVO(booking.getTotalPrice()), booking.getPublicId(), displayCardListingDTO.publicId());
		}).toList();
	}

	@Transactional
	public State<UUID, String> cancel(UUID bookingPublicId, UUID listingPublicId, boolean byLandlord) {
		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		int deleteCount = 0;

		/* if the cancel is called by the landlord */
		if (SecurityUtils.hasCurrentUserAnyOfAuthorities(SecurityUtils.ROLE_LANDLORD) && byLandlord) {
			deleteCount = handleDeletionForLandlord(bookingPublicId, listingPublicId, connectedUser, deleteCount);
		} else {
			deleteCount = bookingRepository.deleteBookingByFkTenantAndPublicId(connectedUser.publicId(),
					bookingPublicId);
		}

		if (deleteCount > 0) {
			return State.<UUID, String>builder().forSuccess(bookingPublicId);
		} else {
			return State.<UUID, String>builder()
					.forError("Booking not found or user not authorized to delete this booking");
		}
	}

	/* if there is a listing for the given landlord, we delete the booking associated to the listing */
	private int handleDeletionForLandlord(UUID bookingPublicId, UUID listingPublicId, ReadUserDTO connectedUser,
			int deleteCount) {
		Optional<DisplayCardListingDTO> listingVerificationOpt = listingService
				.getByPublicIdAndLandlordPublicId(listingPublicId, connectedUser.publicId());
		if (listingVerificationOpt.isPresent()) {
			deleteCount = bookingRepository.deleteBookingByPublicIdAndFkListing(bookingPublicId,
					listingVerificationOpt.get().publicId());
		}
		return deleteCount;
	}
	
	@Transactional(readOnly = true)
	public List<BookedListingDTO> getAllBookedListingForTheLandlord(){
		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		List<DisplayCardListingDTO> allProperties = listingService.getAllPropertiesByPublicId(connectedUser);
		List<UUID> allPropertiesByPublicIds = allProperties.stream().map(DisplayCardListingDTO::publicId).toList();
		List<Booking> allBookings= bookingRepository.findAllByFkListingIn(allPropertiesByPublicIds);
		return mapBookingToBookedListing(allBookings, allProperties);
	}
	
	public List<UUID> getBookingMatchByListingIdsAndBookedDate(List<UUID> listingsId, BookedDateDTO dates){
		return bookingRepository.findAllMatchWithDate(listingsId, dates.startDate(), dates.endDate()).stream().map(Booking::getFkListing).toList();
	}

}
