package fr.kamsan.airbnb_clone_backend.listing.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.airbnb_clone_backend.listing.application.dto.DisplayCardListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.service.TenantService;
import fr.kamsan.airbnb_clone_backend.listing.domain.BookingCategory;
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

}