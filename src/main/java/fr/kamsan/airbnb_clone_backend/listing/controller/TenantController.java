package fr.kamsan.airbnb_clone_backend.listing.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayCardListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.SearchDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.service.TenantService;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;
import fr.kamsan.airbnb_clone_backend.sharedkernel.service.State;
import fr.kamsan.airbnb_clone_backend.sharedkernel.service.StatusNotification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenant-listing")
public class TenantController {

	private final TenantService tenantService;

	@GetMapping("/get-all-by-category")
	public ResponseEntity<Page<DisplayCardListingDTO>> findAllByBookingCategory(Pageable pageable,
			@RequestParam BookingCategory category) {

		return ResponseEntity.ok(tenantService.getAllListingByCategory(pageable, category));

	}

	@GetMapping("/get-one")
	public ResponseEntity<DisplayListingDTO> getOne(@RequestParam UUID publicId) {
		State<DisplayListingDTO, String> state = tenantService.getListingByPublicId(publicId);
		if (state.getStatus().equals(StatusNotification.OK)) {
			return ResponseEntity.ok(state.getValue());
		} else {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, state.getError());
			return ResponseEntity.of(problemDetail).build();
		}
	}

	@PostMapping("/search")
	public ResponseEntity<Page<DisplayCardListingDTO>> findAllByCriteria(Pageable pageable,
			@RequestBody @Valid SearchDTO searchDTO) {
		return ResponseEntity.ok(tenantService.search(pageable, searchDTO));
	}

}