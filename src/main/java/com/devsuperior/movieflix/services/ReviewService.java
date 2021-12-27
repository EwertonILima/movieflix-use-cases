package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

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

}
