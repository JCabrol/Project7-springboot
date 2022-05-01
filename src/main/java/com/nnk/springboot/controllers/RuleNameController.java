package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.dto.RuleNameDTO;
import com.nnk.springboot.services.RuleNameService;
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
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    private static final String RULE_NAME_HOME_REDIRECTION = "redirect:/ruleName/list";
    private static final String VIEW_ATTRIBUTE_NAME = "ruleName";

    /**
     * Read - Get all ruleNames registered in database
     *
     * @return - A view containing the list of all ruleName
     */
    @ApiOperation(value = "Displays all ruleName registered.")
    @GetMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleNameDTO> list = ruleNameService.getAllRuleName();
        model.addAttribute("ruleNameList", list);
        return "ruleName/list";
    }

    /**
     * Read - Displays form to add ruleName
     *
     * @return - post "ruleName:add" which is validation and registration for the ruleName to add
     */
    @ApiOperation(value = "Displays a form to add ruleName.")
    @GetMapping("/ruleName/add")
    public String addBidForm(Model model) {
        RuleNameDTO ruleNameDTO = new RuleNameDTO();
        model.addAttribute(VIEW_ATTRIBUTE_NAME, ruleNameDTO);
        return "ruleName/add";
    }

    /**
     * Create - Add a new ruleName
     *
     * @param ruleName: A ruleNameDTO object containing information to create ruleName
     * @return the ruleName form page if there is validation error, the ruleName list page if the ruleName is correctly created
     */
    @ApiOperation(value = "Add a ruleName.")
    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDTO ruleName, BindingResult result, Model model) {
        ruleNameService.createRuleName(ruleName);
        return RULE_NAME_HOME_REDIRECTION;
    }

    /**
     * Read - Get one ruleName by its id and displays form to update it
     *
     * @param id - An Integer which is the id of the ruleName to update
     * @return a view containing information to show and to update about the selected ruleName
     */
    @ApiOperation(value = "Get a ruleName by its id and displays form to update it.")
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleNameDTO ruleNameDTO = ruleNameService.getRuleNameDTO(id);
        model.addAttribute(VIEW_ATTRIBUTE_NAME, ruleNameDTO);
        return "ruleName/update";
    }

    /**
     * Update - Update an existing ruleName
     *
     * @param id       - An Integer which is the id of the ruleName to update
     * @param ruleName - A RuleNameDTO object containing information to update
     * @return the update form if there is any validation error, the ruleName list page if the ruleName is correctly updated
     */
    @ApiOperation(value = "Update a ruleName by its id.")
    @PostMapping("/ruleName/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("ruleName") RuleNameDTO ruleName,
                            BindingResult result, Model model) {
        ruleNameService.updateRuleName(id, ruleName);
        return RULE_NAME_HOME_REDIRECTION;
    }

    /**
     * Delete - Delete a ruleName
     *
     * @param id - An Integer which is the id of the ruleName to delete
     */
    @ApiOperation(value = "Delete a ruleName by its id.")
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleNameService.deleteRuleName(id);
        return RULE_NAME_HOME_REDIRECTION;
    }
}