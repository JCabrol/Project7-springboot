package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.dto.RuleNameDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RuleNameServiceImpl implements RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    /**
     * Create a new RuleName
     *
     * @param ruleNameDTO a RuleNameDTO object containing information to create a new ruleName
     * @return the RuleName object created
     */
    @Override
    public RuleName createRuleName(RuleNameDTO ruleNameDTO) {
        log.debug("Function createRuleName in RuleNameService begins.");
        RuleName ruleName = new RuleName(ruleNameDTO.getName(), ruleNameDTO.getDescription(), ruleNameDTO.getJson(), ruleNameDTO.getTemplate(), ruleNameDTO.getSqlStr(), ruleNameDTO.getSqlPart());
        ruleName = ruleNameRepository.save(ruleName);
        log.info("New ruleName with id number " + ruleName.getId() + " has been created.");
        log.debug("Function createRuleName in RuleNameService ends without exception.");
        return ruleName;
    }

    /**
     * Get all RuleNames
     *
     * @return a list of all the RuleNames existing
     */
    @Override
    public List<RuleNameDTO> getAllRuleName() {
        log.debug("Function getAllRuleName in RuleNameService begins.");
        List<RuleNameDTO> allRuleNameDTO = new ArrayList<>();
        List<RuleName> allRuleName = ruleNameRepository.findAll();
        if (!allRuleName.isEmpty()) {
            allRuleName.forEach(ruleName -> {
                RuleNameDTO ruleNameDTO = transformRuleNameToDTO(ruleName);
                allRuleNameDTO.add(ruleNameDTO);
            });
        }
        log.debug("Function getAllRuleName in RuleNameService ends without exception.");
        return allRuleNameDTO;
    }

    /**
     * Get a RuleName
     *
     * @param id the id of the RuleName object researched
     * @return the RuleName object researched
     */
    private RuleName getRuleName(Integer id) throws ObjectNotFoundException {
        log.debug("Function getRuleName in RuleNameService begins.");
        Optional<RuleName> ruleNameOptional = ruleNameRepository.findById(id);
        if (ruleNameOptional.isPresent()) {
            RuleName ruleName = ruleNameOptional.get();
            log.debug("Function getRuleName in RuleNameService ends without exception.");
            return ruleName;
        } else {
            throw new ObjectNotFoundException("The ruleName with id number " + id + " was not found.");
        }
    }

    /**
     * Get a RuleName
     *
     * @param id the id of the RuleName object researched
     * @return a RuleNameDTO object containing all information to show from the ruleName researched
     */
    @Override
    public RuleNameDTO getRuleNameDTO(Integer id) throws ObjectNotFoundException {
        log.debug("Function getRuleNameDTO in RuleNameService begins.");
        RuleName ruleName = getRuleName(id);
        RuleNameDTO ruleNameDTO = transformRuleNameToDTO(ruleName);
        log.debug("Function getRuleNameDTO in RuleNameService ends without exception.");
        return ruleNameDTO;
    }

    /**
     * Update a RuleName
     *
     * @param id          the id of the RuleName to update
     * @param ruleNameDTO a ruleNameDTO object containing all information to update
     * @return the RuleName object updated
     */
    @Override
    public RuleName updateRuleName(Integer id, RuleNameDTO ruleNameDTO) throws ObjectNotFoundException {
        log.debug("Function updateRuleName in RuleNameService begins.");
        RuleName ruleName = getRuleName(id);
        ruleName.setName(ruleNameDTO.getName());
        ruleName.setDescription(ruleNameDTO.getDescription());
        ruleName.setJson(ruleNameDTO.getJson());
        ruleName.setTemplate(ruleNameDTO.getTemplate());
        ruleName.setSqlStr(ruleNameDTO.getSqlStr());
        ruleName.setSqlPart(ruleNameDTO.getSqlPart());
        ruleName = ruleNameRepository.save(ruleName);
        log.info("RuleName with id number " + ruleName.getId() + " has been updated.");
        log.debug("Function updateRuleName in RuleNameService ends without exception.");
        return ruleName;
    }

    /**
     * Delete a RuleName from its id
     *
     * @param id the id of the RuleName object to delete
     */
    @Override
    public void deleteRuleName(Integer id) throws ObjectNotFoundException {
        log.debug("Function deleteRuleName in RuleNameService begins.");
        if (ruleNameRepository.existsById(id)) {
            ruleNameRepository.deleteById(id);
            log.info("RuleName with id number " + id + " has been deleted.");
            log.debug("Function deleteRuleName in RuleNameService ends without exception.");
        } else {
            throw new ObjectNotFoundException("The ruleName with id number " + id + " was not found so it could not have been deleted");
        }
    }

    /**
     * Transform ruleName to RuleNameDTO object
     *
     * @param ruleName the RuleName object to transform to RuleNameDTO object
     * @return a RuleNameDTO object containing all information to show from the RuleName object
     */
    private RuleNameDTO transformRuleNameToDTO(RuleName ruleName) {
        log.trace("Function transformRuleNameToDTO in RuleNameService begins.");
        RuleNameDTO ruleNameDTO = new RuleNameDTO(ruleName.getId(), ruleName.getName(), ruleName.getDescription(), ruleName.getJson(), ruleName.getTemplate(), ruleName.getSqlStr(), ruleName.getSqlPart());
        log.trace("Function transformRuleNameToDTO in RuleNameService ends without exception.");
        return ruleNameDTO;
    }
}
