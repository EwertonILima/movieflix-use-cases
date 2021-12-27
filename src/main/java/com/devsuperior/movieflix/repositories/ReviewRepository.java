package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	@Query("SELECT obj FROM Review obj WHERE "
			+ "(obj.movie.id = :movieId) AND "
	        + "(obj.user = :user)")
	List<Review> findByMovieAndUser(Long movieId, User user);

}
