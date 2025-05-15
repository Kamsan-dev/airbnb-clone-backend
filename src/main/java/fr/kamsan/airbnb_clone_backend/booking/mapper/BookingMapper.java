package fr.kamsan.airbnb_clone_backend.booking.mapper;

import org.mapstruct.Mapper;

import fr.kamsan.airbnb_clone_backend.booking.application.dto.BookedDateDTO;
import fr.kamsan.airbnb_clone_backend.booking.application.dto.NewBookingDTO;
import fr.kamsan.airbnb_clone_backend.booking.domain.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
	
	
	Booking newBookingDTOToBooking(NewBookingDTO newBookingDTO);
	
	BookedDateDTO bookingRangeDates(Booking booking);

}
