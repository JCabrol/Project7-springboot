package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.dto.RatingDTO;
import com.nnk.springboot.services.RatingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;

    private static final String RATING_HOME_REDIRECTION = "redirect:/rating/list";
    private static final String VIEW_ATTRIBUTE_NAME = "rating";

    /**
     * Read - Get all ratings registered in database
     *
     * @return - A view containing the list of all rating
     */
    @ApiOperation(value = "Displays all rating registered.")
    @GetMapping("/rating/list")
    public String home(Model model) {
        List<RatingDTO> list = ratingService.getAllRating();
        model.addAttribute("ratingList", list);
        return "rating/list";
    }

    /**
     * Read - Displays form to add rating
     *
     * @return - post "rating:add" which is validation and registration for the rating to add
     */
    @ApiOperation(value = "Displays a form to add rating.")
    @GetMapping("/rating/add")
    public String addBidForm(Model model) {
        RatingDTO ratingDTO = new RatingDTO();
        model.addAttribute(VIEW_ATTRIBUTE_NAME, ratingDTO);
        return "rating/add";
    }

    /**
     * Create - Add a new rating
     *
     * @param rating: A ratingDTO object containing information to create rating
     * @return the rating form page if there is validation error, the rating list page if the rating is correctly created
     */
    @ApiOperation(value = "Add a rating.")
    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDTO rating, BindingResult result, Model model) {
        ratingService.createRating(rating);
        return RATING_HOME_REDIRECTION;
    }

    /**
     * Read - Get one rating by its id and displays form to update it
     *
     * @param id - An Integer which is the id of the rating to update
     * @return a view containing information to show and to update about the selected rating
     */
    @ApiOperation(value = "Get a rating by its id and displays form to update it.")
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RatingDTO ratingDTO = ratingService.getRatingDTO(id);
        model.addAttribute(VIEW_ATTRIBUTE_NAME, ratingDTO);
        return "rating/update";
    }

    /**
     * Update - Update an existing rating
     *
     * @param id     - An Integer which is the id of the rating to update
     * @param rating - A RatingDTO object containing information to update
     * @return the update form if there is any validation error, the rating list page if the rating is correctly updated
     */
    @ApiOperation(value = "Update a rating by its id.")
    @PostMapping("/rating/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("rating") RatingDTO rating,
                            BindingResult result, Model model) {
        ratingService.updateRating(id, rating);
        return RATING_HOME_REDIRECTION;
    }

    /**
     * Delete - Delete a rating
     *
     * @param id - An Integer which is the id of the rating to delete
     */
    @ApiOperation(value = "Delete a rating by its id.")
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        ratingService.deleteRating(id);
        return RATING_HOME_REDIRECTION;
    }
}