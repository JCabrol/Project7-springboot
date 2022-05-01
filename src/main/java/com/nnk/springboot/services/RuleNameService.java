package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.dto.RuleNameDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;

import java.util.List;

public interface RuleNameService {

    /**
     * Create a new RuleName
     *
     * @param ruleNameDTO a RuleNameDTO object containing information to create a new ruleName
     * @return the RuleName object created
     */
    RuleName createRuleName(RuleNameDTO ruleNameDTO);

    /**
     * Get all RuleNames
     *
     * @return a list of all the RuleNames existing
     */
    List<RuleNameDTO> getAllRuleName();

    /**
     * Get a RuleName
     *
     * @param id the id of the RuleName object researched
     * @return a RuleNameDTO object containing all information to show from the ruleName researched
     */
    RuleNameDTO getRuleNameDTO(Integer id) throws ObjectNotFoundException;

    /**
     * Update a RuleName
     *
     * @param id          the id of the RuleName to update
     * @param ruleNameDTO a ruleNameDTO object containing all information to update
     * @return the RuleName object updated
     */
    RuleName updateRuleName(Integer id, RuleNameDTO ruleNameDTO) throws ObjectNotFoundException;

    /**
     * Delete a RuleName from its id
     *
     * @param id the id of the RuleName object to delete
     */
    void deleteRuleName(Integer id) throws ObjectNotFoundException;

}