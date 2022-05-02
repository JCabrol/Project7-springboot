package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.dto.TradeDTO;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {

    @Autowired
    private TradeService tradeService;

    private static final String TRADE_HOME_REDIRECTION = "redirect:/trade/list";
    private static final String VIEW_ATTRIBUTE_NAME = "trade";
    /**
     * Read - Get all trades registered in database
     *
     * @return - A view containing the list of all trade
     */
    @ApiOperation(value = "Displays all trade registered.")
    @GetMapping("/trade/list")
    public String home(Model model) {
        List<TradeDTO> tradeList = tradeService.getAllTrade();
        model.addAttribute("tradeList", tradeList);
        return "trade/list";
    }

    /**
     * Read - Displays form to add trade
     *
     * @return - post "trade:add" which is validation and registration for the trade to add
     */
    @ApiOperation(value = "Displays a form to add trade.")
    @GetMapping("/trade/add")
    public String addTradeForm(Model model) {
        TradeDTO tradeDTO = new TradeDTO();
        model.addAttribute(VIEW_ATTRIBUTE_NAME, tradeDTO);
        return "trade/add";
    }

    /**
     * Create - Add a new trade
     *
     * @param trade: A tradeDTO object containing information to create trade
     * @return the trade form page if there is validation error, the trade list page if the trade is correctly created
     */
    @ApiOperation(value = "Add a trade.")
    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDTO trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(VIEW_ATTRIBUTE_NAME, trade);
            return "trade/add";
        }
        tradeService.createTrade(trade);
        return TRADE_HOME_REDIRECTION;
    }

    /**
     * Read - Get one trade by its id and displays form to update it
     *
     * @param id - An Integer which is the id of the trade to update
     * @return a view containing information to show and to update about the selected trade
     */
    @ApiOperation(value = "Get a trade by its id and displays form to update it.")
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        TradeDTO tradeDTO = tradeService.getTradeDTO(id);
        model.addAttribute(VIEW_ATTRIBUTE_NAME, tradeDTO);
        return "trade/update";
    }

    /**
     * Update - Update an existing trade
     *
     * @param id     - An Integer which is the id of the trade to update
     * @param trade - A TradeDTO object containing information to update
     * @return the update form if there is any validation error, the trade list page if the trade is correctly updated
     */
    @ApiOperation(value = "Update a trade by its id.")
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid @ModelAttribute("trade") TradeDTO trade,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            trade.setTradeId(id);
            model.addAttribute(VIEW_ATTRIBUTE_NAME, trade);
            return "trade/update";
        }
        tradeService.updateTrade(id, trade);
        return TRADE_HOME_REDIRECTION;
    }

    /**
     * Delete - Delete a trade
     *
     * @param id - An Integer which is the id of the trade to delete
     */
    @ApiOperation(value = "Delete a trade by its id.")
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.deleteTrade(id);
        return TRADE_HOME_REDIRECTION;
    }
}