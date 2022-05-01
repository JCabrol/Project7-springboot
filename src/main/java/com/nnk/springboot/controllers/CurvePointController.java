package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.dto.CurvePointDTO;
import com.nnk.springboot.services.CurvePointService;
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
public class CurvePointController {

    @Autowired
    private CurvePointService curvePointService;

    private static final String CURVE_POINT_HOME_REDIRECTION = "redirect:/curvePoint/list";
    private static final String VIEW_ATTRIBUTE_NAME = "curvePoint";
    /**
     * Read - Get all curvePoints registered in database
     *
     * @return - A view containing the list of all curvePoint
     */
    @ApiOperation(value = "Displays all curvePoint registered.")
    @GetMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePointDTO> list = curvePointService.getAllCurvePoint();
        model.addAttribute("curvePointList", list);
        return "curvePoint/list";
    }

    /**
     * Read - Displays form to add curvePoint
     *
     * @return - post "curvePoint:add" which is validation and registration for the curvePoint to add
     */
    @ApiOperation(value = "Displays a form to add curvePoint.")
    @GetMapping("/curvePoint/add")
    public String addBidForm(Model model) {
        CurvePointDTO curvePointDTO = new CurvePointDTO();
        model.addAttribute(VIEW_ATTRIBUTE_NAME, curvePointDTO);
        return "curvePoint/add";
    }

    /**
     * Create - Add a new curvePoint
     *
     * @param curvePoint: A curvePointDTO object containing information to create curvePoint
     * @return the curvePoint form page if there is validation error, the curvePoint list page if the curvePoint is correctly created
     */
    @ApiOperation(value = "Add a curvePoint.")
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDTO curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(VIEW_ATTRIBUTE_NAME, curvePoint);
            return "curvePoint/add";
        }
        curvePointService.createCurvePoint(curvePoint);
        return CURVE_POINT_HOME_REDIRECTION;
    }

    /**
     * Read - Get one curvePoint by its id and displays form to update it
     *
     * @param id - An Integer which is the id of the curvePoint to update
     * @return a view containing information to show and to update about the selected curvePoint
     */
    @ApiOperation(value = "Get a curvePoint by its id and displays form to update it.")
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePointDTO curvePointDTO = curvePointService.getCurvePointDTO(id);
        model.addAttribute(VIEW_ATTRIBUTE_NAME, curvePointDTO);
        return "curvePoint/update";
    }

    /**
     * Update - Update an existing CurvePoint
     *
     * @param id     - An Integer which is the id of the curvePoint to update
     * @param curvePoint - A CurvePointDTO object containing information to update
     * @return the update form if there is any validation error, the curvePoint list page if the curvePoint is correctly updated
     */
    @ApiOperation(value = "Update a curvePoint by its id.")
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("curvePoint") CurvePointDTO curvePoint,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            curvePoint.setId(id);
            model.addAttribute(VIEW_ATTRIBUTE_NAME, curvePoint);
            return "curvePoint/update";
        }
        curvePointService.updateCurvePoint(id, curvePoint);
        return CURVE_POINT_HOME_REDIRECTION;
    }

    /**
     * Delete - Delete a curvePoint
     *
     * @param id - An Integer which is the id of the curvePoint to delete
     */
    @ApiOperation(value = "Delete a curvePoint by its id.")
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        curvePointService.deleteCurvePoint(id);
        return CURVE_POINT_HOME_REDIRECTION;
    }
}