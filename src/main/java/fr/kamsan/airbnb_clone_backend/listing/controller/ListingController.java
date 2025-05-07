package fr.kamsan.airbnb_clone_backend.listing.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.kamsan.airbnb_clone_backend.listing.application.ListingService;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.CreatedListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.ListingDTO;
import fr.kamsan.airbnb_clone_backend.listing.application.dto.sub.PictureDTO;
import fr.kamsan.airbnb_clone_backend.user.application.UserException;
import fr.kamsan.airbnb_clone_backend.user.application.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/landlord-listing")
public class ListingController {

	private final ListingService listingService;
	private final Validator validator;
	private final UserService userService;
	private ObjectMapper objectMapper;

	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CreatedListingDTO> create(MultipartHttpServletRequest request,
			@RequestPart(name = "dto") String listingDTOString) throws IOException {

		List<PictureDTO> pictures = request.getFileMap().values().stream().map(mapMultiPartFileToPictureDTO()).toList();
		ListingDTO listingDTO = objectMapper.readValue(listingDTOString, ListingDTO.class);
		listingDTO.setPictures(pictures);

		Set<ConstraintViolation<ListingDTO>> violations = validator.validate(listingDTO);
		if (!violations.isEmpty()) {
			String violationsJoined = violations.stream()
					.map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
					.collect(Collectors.joining());

			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, violationsJoined);
			return ResponseEntity.of(problemDetail).build();
		} else {
			return ResponseEntity.ok(listingService.create(listingDTO));
		}

	}

	private static Function<MultipartFile, PictureDTO> mapMultiPartFileToPictureDTO() {
		return multipartFile -> {
			try {
				return new PictureDTO(multipartFile.getBytes(), multipartFile.getContentType(), false);
			} catch (IOException ex) {
				throw new UserException(String.format("Cannot parse multipart file: %s", multipartFile));
			}
		};
	}

}
