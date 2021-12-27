package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private AuthService authService;

	@Transactional
	public ReviewDTO post(ReviewDTO dto) {
		try {

			Review entity = new Review();
			User user = authService.authenticated();
			entity.setText(dto.getText());
			entity.setMovie(new Movie(dto.getMovieId(), null, null, null, null, null, null));
			entity.setUser(user);
			entity = reviewRepository.save(entity);
			return new ReviewDTO(entity, user);

		} 
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}

	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> reviewsForCurrentUser(Long movieId) {
		Optional<Movie> obj = movieRepository.findById(movieId);
		obj.orElseThrow(() -> new ResourceNotFoundException("Movie not found."));

		User user = authService.authenticated();
		List<Review> list = reviewRepository.findByMovieAndUser(movieId, user);
		return list.stream().map(x -> new ReviewDTO(x, user)).collect(Collectors.toList());
	}

}
