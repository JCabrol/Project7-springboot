package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.dto.BidListDTO;
import com.nnk.springboot.services.BidListService;
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
public class BidListController {

    @Autowired
    private BidListService bidListService;

    private static final String BID_LIST_HOME_REDIRECTION = "redirect:/bidList/list";
    private static final String VIEW_ATTRIBUTE_NAME = "bidList";

    /**
     * Read - Get all bidLists registered in database
     *
     * @return - A view containing the list of all bidList
     */
    @ApiOperation(value = "Displays all bidList registered.")
    @GetMapping("/bidList/list")
    public String home(Model model) {
        List<BidListDTO> list = bidListService.getAllBidList();
        model.addAttribute(VIEW_ATTRIBUTE_NAME, list);
        return "bidList/list";
    }

    /**
     * Read - Displays form to add bidList
     *
     * @return - post "bidList:add" which is validation and registration for the bidList to add
     */
    @ApiOperation(value = "Displays a form to add bidList.")
    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        BidListDTO bidListDTO = new BidListDTO();
        model.addAttribute(VIEW_ATTRIBUTE_NAME, bidListDTO);
        return "bidList/add";
    }

    /**
     * Create - Add a new bidList
     *
     * @param bidList: A bidListDTO object containing information to create bidList
     * @return the bidList form page if there is validation error, the bidList list page if the bidList is correctly created
     */
    @ApiOperation(value = "Add a bidList.")
    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidListDTO bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(VIEW_ATTRIBUTE_NAME, bidList);
            return "bidList/add";
        }
        bidListService.createBidList(bidList);
        return BID_LIST_HOME_REDIRECTION;
    }

    /**
     * Read - Get one bidList by its id and displays form to update it
     *
     * @param id - An Integer which is the id of the bidList to update
     * @return a view containing information to show and to update about the selected bidList
     */
    @ApiOperation(value = "Get a bidList by its id and displays form to update it.")
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidListDTO bidListDTO = bidListService.getBidListDTO(id);
        model.addAttribute(VIEW_ATTRIBUTE_NAME, bidListDTO);
        return "bidList/update";
    }

    /**
     * Update - Update an existing bidList
     *
     * @param id      - An Integer which is the id of the bidList to update
     * @param bidList - A BidListDTO object containing information to update
     * @return the update form if there is any validation error, the bidList list page if the bidList is correctly updated
     */
    @ApiOperation(value = "Update a bidList by its id.")
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("bidList") BidListDTO bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            bidList.setBidListId(id);
            model.addAttribute(VIEW_ATTRIBUTE_NAME, bidList);
            return "bidList/update";
        }
        bidListService.updateBidList(id, bidList);
        return BID_LIST_HOME_REDIRECTION;
    }

    /**
     * Delete - Delete a bidList
     *
     * @param id - An Integer which is the id of the bidList to delete
     */
    @ApiOperation(value = "Delete a bidList by its id.")
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidListService.deleteBidList(id);
        return BID_LIST_HOME_REDIRECTION;
    }
}