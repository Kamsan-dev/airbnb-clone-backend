package fr.kamsan.airbnb_clone_backend.booking.application.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.kamsan.airbnb_clone_backend.booking.application.dto.BookedDateDTO;
import fr.kamsan.airbnb_clone_backend.booking.application.dto.NewBookingDTO;
import fr.kamsan.airbnb_clone_backend.booking.domain.Booking;
import fr.kamsan.airbnb_clone_backend.booking.mapper.BookingMapper;
import fr.kamsan.airbnb_clone_backend.booking.repository.BookingRepository;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.ListingCreateBookingDTO;
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
	public State<Void, String> create(NewBookingDTO newBookingDTO){
		Booking booking = bookingMapper.newBookingDTOToBooking(newBookingDTO);
		Optional<ListingCreateBookingDTO> listingOpt =  listingService.getByListingPublicId(newBookingDTO.listingPublicId());
		
		if (listingOpt.isEmpty()) {
			return State.<Void, String>builder().forError("Listing public id not found");
		}

		boolean alreadyBooked = bookingRepository.bookingExistsAtInterval(newBookingDTO.startDate(), newBookingDTO.endDate(), newBookingDTO.listingPublicId());
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
		booking.setTotalPrice((int) (numberOfNights * listingCreateBookingDTO.price().value()) );
		
		bookingRepository.save(booking);
		
		return State.<Void,String>builder().forSuccess();
	}
	
	@Transactional(readOnly = true)
	public List<BookedDateDTO> checkAvailability(UUID publicId){
		return bookingRepository.findAllByFkListing(publicId).stream().map(bookingMapper::bookingToCheckAvailability).toList();
	}

}
