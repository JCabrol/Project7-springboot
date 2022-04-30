package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.dto.RatingDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;

import java.util.List;

public interface RatingService {

    /**
     * Create a new Rating
     *
     * @param ratingDTO a RatingDTO object containing information to create a new rating
     * @return the Rating object created
     */
    Rating createRating(RatingDTO ratingDTO);

    /**
     * Get all Ratings
     *
     * @return a list of all the Ratings existing
     */
    List<RatingDTO> getAllRating();

    /**
     * Get a Rating
     *
     * @param id the id of the Rating object researched
     * @return a RatingDTO object containing all information to show from the rating researched
     */
    RatingDTO getRatingDTO(Integer id) throws ObjectNotFoundException;

    /**
     * Update a Rating
     *
     * @param id            the id of the Rating to update
     * @param ratingDTO a ratingDTO object containing all information to update
     * @return the Rating object updated
     */
    Rating updateRating(Integer id, RatingDTO ratingDTO) throws ObjectNotFoundException;

    /**
     * Delete a Rating from its id
     *
     * @param id the id of the Rating object to delete
     */
    void deleteRating(Integer id) throws ObjectNotFoundException;

}