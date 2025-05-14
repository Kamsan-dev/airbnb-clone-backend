package fr.kamsan.airbnb_clone_backend.booking.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.airbnb_clone_backend.booking.application.dto.BookedDateDTO;
import fr.kamsan.airbnb_clone_backend.booking.application.dto.NewBookingDTO;
import fr.kamsan.airbnb_clone_backend.booking.application.service.BookingService;
import fr.kamsan.airbnb_clone_backend.sharedkernel.service.State;
import fr.kamsan.airbnb_clone_backend.sharedkernel.service.StatusNotification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {
	
	private final BookingService bookingService;
	
	@PostMapping("create")
	public ResponseEntity<?> create (@Valid @RequestBody NewBookingDTO newBookingDTO){
		State<Void, String> createState = bookingService.create(newBookingDTO);
		if(createState.getStatus().equals(StatusNotification.ERROR)) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, createState.getError());
			return ResponseEntity.of(problemDetail).build();
		} else {
			return ResponseEntity.ok(true);
		}
	}
	
	@GetMapping("check-availability")
	public ResponseEntity<List<BookedDateDTO>> checkAvailibity(@RequestParam UUID listingPublicId){
		return ResponseEntity.ok(bookingService.checkAvailability(listingPublicId));
	}

}
