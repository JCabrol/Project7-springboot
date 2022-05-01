package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.dto.RuleNameDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("ruleNameTests")
@SpringBootTest
public class RuleNameServiceTest {

    @Autowired
    private RuleNameService ruleNameService;

    @MockBean
    private RuleNameRepository ruleNameRepository;


    @Nested
    @Tag("RuleNameServiceTests")
    @DisplayName("createRuleName tests:")
    class CreateRuleNameTest {


        @DisplayName("GIVEN a ruleNameDTO containing all required information" +
                "WHEN the function createRuleName() is called " +
                "THEN a ruleName is created with expected information.")
        @Test
        void createRuleNameTest() {
            //GIVEN
            //a ruleNameDTO containing all required information
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            RuleNameDTO ruleNameDTO = new RuleNameDTO(name, description, json, template, sqlStr, sqlPart);
            RuleName ruleName = new RuleName(name, description, json, template, sqlStr, sqlPart);
            final ArgumentCaptor<RuleName> arg = ArgumentCaptor.forClass(RuleName.class);
            when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
            //WHEN
            //the function createRuleName() is called
            RuleName result = ruleNameService.createRuleName(ruleNameDTO);
            //THEN
            //a ruleName is created with expected information
            assertThat(result).isInstanceOf(RuleName.class);
            //the expected methods have been called with expected arguments
            verify(ruleNameRepository).save(arg.capture());
            assertEquals(name, arg.getValue().getName());
            assertEquals(description, arg.getValue().getDescription());
            assertEquals(json, arg.getValue().getJson());
            assertEquals(template, arg.getValue().getTemplate());
            assertEquals(sqlStr, arg.getValue().getSqlStr());
            assertEquals(sqlPart, arg.getValue().getSqlPart());
        }
    }


    @Nested
    @Tag("RuleNameServiceTests")
    @DisplayName("getAllRuleName tests:")
    class GetAllRuleNameTest {

        @DisplayName("GIVEN no ruleNames returned by ruleNameRepository " +
                "WHEN the function getAllRuleName() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllRuleNameWhenEmptyTest() {
            //GIVEN
            //no ruleNames returned by ruleNameRepository
            List<RuleName> allRuleNameTest = new ArrayList<>();
            when(ruleNameRepository.findAll()).thenReturn(allRuleNameTest);
            //WHEN
            //the function getAllRuleName() is called
            List<RuleNameDTO> result = ruleNameService.getAllRuleName();
            //THEN
            //it returns an empty list
            assertThat(result).isEmpty();
            //and the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN ruleNames returned by ruleNameRepository " +
                "WHEN the function getAllRuleName() is called " +
                "THEN it returns the expected list.")
        @Test
        void getAllRuleNameWhenNotEmptyTest() {
            //GIVEN
            //ruleNames returned by ruleNameRepository
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            String name2 = "name2";
            String description2 = "description2";
            String json2 = "json2";
            String template2 = "template2";
            String sqlStr2 = "sqlStr2";
            String sqlPart2 = "sqlPart2";
            RuleName ruleName = new RuleName(name, description, json, template, sqlStr, sqlPart);
            RuleName ruleName2 = new RuleName(name2, description2, json2, template2, sqlStr2, sqlPart2);
            List<RuleName> allRuleNameTest = new ArrayList<>();
            allRuleNameTest.add(ruleName);
            allRuleNameTest.add(ruleName2);
            when(ruleNameRepository.findAll()).thenReturn(allRuleNameTest);
            //WHEN
            //the function getAllRuleName() is called
            List<RuleNameDTO> result = ruleNameService.getAllRuleName();
            //THEN
            //it returns the expected list
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo(name);
            assertThat(result.get(0).getDescription()).isEqualTo(description);
            assertThat(result.get(0).getJson()).isEqualTo(json);
            assertThat(result.get(0).getTemplate()).isEqualTo(template);
            assertThat(result.get(0).getSqlStr()).isEqualTo(sqlStr);
            assertThat(result.get(0).getSqlPart()).isEqualTo(sqlPart);
            assertThat(result.get(1).getName()).isEqualTo(name2);
            assertThat(result.get(1).getDescription()).isEqualTo(description2);
            assertThat(result.get(1).getJson()).isEqualTo(json2);
            assertThat(result.get(1).getTemplate()).isEqualTo(template2);
            assertThat(result.get(1).getSqlStr()).isEqualTo(sqlStr2);
            assertThat(result.get(1).getSqlPart()).isEqualTo(sqlPart2);
            //and the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).findAll();
        }
    }


    @Nested
    @Tag("RuleNameServiceTests")
    @DisplayName("getRuleNameDTO tests:")
    class GetRuleNameDTOTest {

        @DisplayName("GIVEN an existing ruleName " +
                "WHEN the function getRuleNameDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getRuleNameDTOExistingTest() {
            //GIVEN
            //an existing ruleName
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            Integer id = 1;
            RuleName ruleName = new RuleName(name, description, json, template, sqlStr, sqlPart);
            ruleName.setId(id);
            when(ruleNameRepository.findById(id)).thenReturn(Optional.of(ruleName));
            //WHEN
            //the function getRuleNameDTO() is called
            RuleNameDTO result = ruleNameService.getRuleNameDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(RuleNameDTO.class);
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getDescription()).isEqualTo(description);
            assertThat(result.getJson()).isEqualTo(json);
            assertThat(result.getTemplate()).isEqualTo(template);
            assertThat(result.getSqlStr()).isEqualTo(sqlStr);
            assertThat(result.getSqlPart()).isEqualTo(sqlPart);
            assertThat(result.getId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).findById(id);
        }


        @DisplayName("GIVEN a non-existing ruleName " +
                "WHEN the function getRuleNameDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getRuleNameDTONonExistingTest() {
            //GIVEN
            //a non-existing ruleName
            Integer id = 1;
            when(ruleNameRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getRuleNameDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ruleNameService.getRuleNameDTO(id));
            assertEquals("The ruleName with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).findById(id);
        }
    }


    @Nested
    @Tag("RuleNameServiceTests")
    @DisplayName("updateRuleNameDTO tests:")
    class UpdateRuleNameDTOTest {

        @DisplayName("GIVEN an existing ruleName and a RuleNameDTO containing all information" +
                "WHEN the function updateRuleNameDTO() is called " +
                "THEN it returns the RuleName object with information updated and other information unchanged.")
        @Test
        void updateRuleNameDTOExistingTest() {
            //GIVEN
            //an existing ruleName and a RuleNameDTO containing all information
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            String name2 = "name2";
            String description2 = "description2";
            String json2 = "json2";
            String template2 = "template2";
            String sqlStr2 = "sqlStr2";
            String sqlPart2 = "sqlPart2";
            Integer id = 1;
            RuleName ruleName = new RuleName(name, description, json, template, sqlStr, sqlPart);
            RuleNameDTO ruleNameDTO = new RuleNameDTO(name2, description2, json2, template2, sqlStr2, sqlPart2);
            ruleName.setId(id);
            final ArgumentCaptor<RuleName> arg = ArgumentCaptor.forClass(RuleName.class);
            when(ruleNameRepository.findById(id)).thenReturn(Optional.of(ruleName));
            when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
            //WHEN
            //the function updateRuleNameDTO() is called
            RuleName result = ruleNameService.updateRuleName(id, ruleNameDTO);
            //THEN
            // it returns the RuleName object with information updated and other information unchanged
            assertThat(result).isInstanceOf(RuleName.class);
            //and the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).findById(id);
            verify(ruleNameRepository).save(arg.capture());
            assertEquals(name2, arg.getValue().getName());
            assertEquals(description2, arg.getValue().getDescription());
            assertEquals(json2, arg.getValue().getJson());
            assertEquals(template2, arg.getValue().getTemplate());
            assertEquals(sqlStr2, arg.getValue().getSqlStr());
            assertEquals(sqlPart2, arg.getValue().getSqlPart());
            assertEquals(id, arg.getValue().getId());
        }

        @DisplayName("GIVEN a non-existing ruleName " +
                "WHEN the function updateRuleNameDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void updateRuleNameDTONonExistingTest() {
            //GIVEN
            //a non-existing ruleName
            Integer id = 1;
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            RuleNameDTO ruleNameDTO = new RuleNameDTO(name, description, json, template, sqlStr, sqlPart);
            when(ruleNameRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getRuleNameDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ruleNameService.updateRuleName(id, ruleNameDTO));
            assertEquals("The ruleName with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).findById(id);
            verify(ruleNameRepository, Mockito.times(0)).save(any(RuleName.class));
        }
    }

    @Nested
    @Tag("RuleNameServiceTests")
    @DisplayName("deleteRuleName tests:")
    class DeleteRuleNameTest {

        @DisplayName("GIVEN an existing ruleName" +
                "WHEN the function deleteRuleName() is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void deleteRuleNameExistingTest() {
            //GIVEN
            //an existing ruleName
            Integer id = 1;
            when(ruleNameRepository.existsById(id)).thenReturn(true);
            doNothing().when(ruleNameRepository).deleteById(id);
            //WHEN
            //the function deleteRuleName() is called
            ruleNameService.deleteRuleName(id);
            //THEN
            //the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).existsById(id);
            verify(ruleNameRepository, Mockito.times(1)).deleteById(id);
        }

        @DisplayName("GIVEN a non-existing ruleName " +
                "WHEN the function deleteRuleName() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deleteRuleNameDTONonExistingTest() {
            //GIVEN
            //a non-existing ruleName
            Integer id = 1;
            when(ruleNameRepository.existsById(id)).thenReturn(false);
            //WHEN
            //the function deleteRuleName() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ruleNameService.deleteRuleName(id));
            assertEquals("The ruleName with id number " + id + " was not found so it could not have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(ruleNameRepository, Mockito.times(1)).existsById(id);
            verify(ruleNameRepository, Mockito.times(0)).deleteById(anyInt());
        }
    }

}
