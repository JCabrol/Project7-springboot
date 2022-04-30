package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.dto.RatingDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Create a new Rating
     *
     * @param ratingDTO a RatingDTO object containing information to create a new rating
     * @return the Rating object created
     */
    @Override
    public Rating createRating(RatingDTO ratingDTO) {
        log.debug("Function createRating in RatingService begins.");
        Rating rating = new Rating(ratingDTO.getMoodysRating(), ratingDTO.getSandPRating(), ratingDTO.getFitchRating(), ratingDTO.getOrderNumber());
        rating = ratingRepository.save(rating);
        log.info("New rating with id number " + rating.getId() + " has been created.");
        log.debug("Function createRating in RatingService ends without exception.");
        return rating;
    }

    /**
     * Get all Ratings
     *
     * @return a list of all the Ratings existing
     */
    @Override
    public List<RatingDTO> getAllRating() {
        log.debug("Function getAllRating in RatingService begins.");
        List<RatingDTO> allRatingDTO = new ArrayList<>();
        List<Rating> allRating = ratingRepository.findAll();
        if (!allRating.isEmpty()) {
            allRating.forEach(rating -> {
                RatingDTO ratingDTO = transformRatingToDTO(rating);
                allRatingDTO.add(ratingDTO);
            });
        }
        log.debug("Function getAllRating in RatingService ends without exception.");
        return allRatingDTO;
    }

    /**
     * Get a Rating
     *
     * @param id the id of the Rating object researched
     * @return the Rating object researched
     */
    private Rating getRating(Integer id) throws ObjectNotFoundException {
        log.debug("Function getRating in RatingService begins.");
        Optional<Rating> ratingOptional = ratingRepository.findById(id);
        if (ratingOptional.isPresent()) {
            Rating rating = ratingOptional.get();
            log.debug("Function getRating in RatingService ends without exception.");
            return rating;
        } else {
            throw new ObjectNotFoundException("The rating with id number " + id + " was not found.");
        }
    }

    /**
     * Get a Rating
     *
     * @param id the id of the Rating object researched
     * @return a RatingDTO object containing all information to show from the rating researched
     */
    @Override
    public RatingDTO getRatingDTO(Integer id) throws ObjectNotFoundException {
        log.debug("Function getRatingDTO in RatingService begins.");
        Rating rating = getRating(id);
        RatingDTO ratingDTO = transformRatingToDTO(rating);
        log.debug("Function getRatingDTO in RatingService ends without exception.");
        return ratingDTO;
    }

    /**
     * Update a Rating
     *
     * @param id        the id of the Rating to update
     * @param ratingDTO a ratingDTO object containing all information to update
     * @return the Rating object updated
     */
    @Override
    public Rating updateRating(Integer id, RatingDTO ratingDTO) throws ObjectNotFoundException {
        log.debug("Function updateRating in RatingService begins.");
        Rating rating = getRating(id);
        rating.setMoodysRating(ratingDTO.getMoodysRating());
        rating.setSandPRating(ratingDTO.getSandPRating());
        rating.setFitchRating(ratingDTO.getFitchRating());
        rating.setOrderNumber(ratingDTO.getOrderNumber());
        rating = ratingRepository.save(rating);
        log.info("Rating with id number " + rating.getId() + " has been updated.");
        log.debug("Function updateRating in RatingService ends without exception.");
        return rating;
    }

    /**
     * Delete a Rating from its id
     *
     * @param id the id of the Rating object to delete
     */
    @Override
    public void deleteRating(Integer id) throws ObjectNotFoundException {
        log.debug("Function deleteRating in RatingService begins.");
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
            log.info("Rating with id number " + id + " has been deleted.");
            log.debug("Function deleteRating in RatingService ends without exception.");
        } else {
            throw new ObjectNotFoundException("The rating with id number " + id + " was not found so it could not have been deleted");
        }
    }

    /**
     * Transform rating to RatingDTO object
     *
     * @param rating the Rating object to transform to RatingDTO object
     * @return a RatingDTO object containing all information to show from the Rating object
     */
    private RatingDTO transformRatingToDTO(Rating rating) {
        log.trace("Function transformRatingToDTO in RatingService begins.");
        RatingDTO ratingDTO = new RatingDTO(rating.getId(), rating.getMoodysRating(), rating.getSandPRating(), rating.getFitchRating(), rating.getOrderNumber());
        log.trace("Function transformRatingToDTO in RatingService ends without exception.");
        return ratingDTO;
    }
}
