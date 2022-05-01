package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.dto.RuleNameDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ruleNameTests")
@SpringBootTest
@AutoConfigureMockMvc
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    @Nested
    @Tag("RuleNameControllerTests")
    @DisplayName("home tests:")
    class HomeTest {
        @DisplayName("GIVEN a list of ruleName returned by the service" +
                "WHEN the uri \"/ruleName/list\" is called " +
                "THEN the expected view is displayed and the model contains expected attribute.")
        @Test
        void homeTest() throws Exception {
            //GIVEN
            //a list of ruleName returned by the service
            List<RuleNameDTO> ruleNameDTOList = new ArrayList<>();
            when(ruleNameService.getAllRuleName()).thenReturn(ruleNameDTOList);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/list");
            // WHEN
            //the uri "/ruleName/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed and the model contains expected attribute
                    .andExpect(status().isOk())
                    .andExpect(view().name("ruleName/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("ruleNameList", ruleNameDTOList));
            //the expected methods have been called with expected arguments
            verify(ruleNameService, Mockito.times(1)).getAllRuleName();
        }
    }

    @Nested
    @Tag("RuleNameControllerTests")
    @DisplayName("add tests:")
    class AddTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/ruleName/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addTest() throws Exception {
            //GIVEN
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/add");
            // WHEN
            //the uri "/ruleName/add" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the status is "isOk" and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("ruleName/add"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("ruleName"));
        }
    }

    @Nested
    @Tag("RuleNameControllerTests")
    @DisplayName("validate tests:")
    class ValidateTest {

        @DisplayName("GIVEN a ruleNameDTO with all required information" +
                "WHEN the uri \"/ruleName/validate\" is called " +
                "THEN a ruleName with expected information is created and the page is correctly redirected.")
        @Test
        void validateTest() throws Exception {
            //GIVEN
            // a ruleNameDTO with all required information
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            RuleName ruleName = new RuleName(name, description, json, template, sqlStr, sqlPart);
            final ArgumentCaptor<RuleNameDTO> arg = ArgumentCaptor.forClass(RuleNameDTO.class);
            when(ruleNameService.createRuleName(any(RuleNameDTO.class))).thenReturn(ruleName);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/ruleName/validate")
                    .param("name", name)
                    .param("description", description)
                    .param("json", json)
                    .param("template", template)
                    .param("sqlStr", sqlStr)
                    .param("sqlPart", sqlPart);
            // WHEN
            //the uri "/ruleName/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // a ruleName with expected information is created and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("ruleName"))
                    .andExpect(redirectedUrl("/ruleName/list"));
            //the expected methods have been called with expected arguments
            verify(ruleNameService).createRuleName(arg.capture());
            assertEquals(name, arg.getValue().getName());
            assertEquals(description, arg.getValue().getDescription());
            assertEquals(json, arg.getValue().getJson());
            assertEquals(template, arg.getValue().getTemplate());
            assertEquals(sqlStr, arg.getValue().getSqlStr());
            assertEquals(sqlPart, arg.getValue().getSqlPart());
        }
    }

    @Nested
    @Tag("RuleNameControllerTests")
    @DisplayName("showUpdateForm tests:")
    class ShowUpdateFormTest {

        @DisplayName("GIVEN an existing ruleName" +
                "WHEN the uri \"/ruleName/update/{id}\" is called with \"get\" request" +
                "THEN the corresponding ruleNameDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormTest() throws Exception {
            //GIVEN
            // an existing ruleName
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            int id = 1;
            RuleNameDTO ruleNameDTO = new RuleNameDTO(name, description, json, template, sqlStr, sqlPart);
            when(ruleNameService.getRuleNameDTO(id)).thenReturn(ruleNameDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/update/{id}", id);
            // WHEN
            //the uri "/ruleName/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding ruleNameDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("ruleName", ruleNameDTO))
                    .andExpect(view().name("ruleName/update"));
            //the expected methods have been called with expected arguments
            verify(ruleNameService, Mockito.times(1)).getRuleNameDTO(id);
        }

        @DisplayName("GIVEN a non-existing ruleName" +
                "WHEN the uri \"/ruleName/update/{id}\" is called with \"get\" request" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing ruleName
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(ruleNameService.getRuleNameDTO(id)).thenThrow(objectNotFoundException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/update/{id}", id);
            // WHEN
            //the uri "/ruleName/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(ruleNameService, Mockito.times(1)).getRuleNameDTO(id);
        }
    }

    @Nested
    @Tag("RuleNameControllerTests")
    @DisplayName("updateRuleName tests:")
    class UpdateRuleNameTest {

        @DisplayName("GIVEN an existing ruleName and all required information to update" +
                "WHEN the uri \"/ruleName/update/{id}\" is called with \"post\" request" +
                "THEN there is no error, the expected information is sent and the page is correctly redirected.")
        @Test
        void updateRuleNameTest() throws Exception {
            //GIVEN
            // an existing ruleName and all required information to update
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            int id = 1;
            RuleName ruleName = new RuleName(id, name, description, json, template, sqlStr, sqlPart);
            final ArgumentCaptor<RuleNameDTO> arg = ArgumentCaptor.forClass(RuleNameDTO.class);
            when(ruleNameService.updateRuleName(eq(id), any(RuleNameDTO.class))).thenReturn(ruleName);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/ruleName/update/{id}", id)
                    .param("name", name)
                    .param("description", description)
                    .param("json", json)
                    .param("template", template)
                    .param("sqlStr", sqlStr)
                    .param("sqlPart", sqlPart);
            // WHEN
            //the uri "/ruleName/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the expected information is sent and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("ruleName"))
                    .andExpect(redirectedUrl("/ruleName/list"));
            //the expected methods have been called with expected arguments
            verify(ruleNameService).updateRuleName(eq(id), arg.capture());
            assertEquals(name, arg.getValue().getName());
            assertEquals(description, arg.getValue().getDescription());
            assertEquals(json, arg.getValue().getJson());
            assertEquals(template, arg.getValue().getTemplate());
            assertEquals(sqlStr, arg.getValue().getSqlStr());
            assertEquals(sqlPart, arg.getValue().getSqlPart());
        }
    }

    @Nested
    @Tag("RuleNameControllerTests")
    @DisplayName("deleteRuleName tests:")
    class DeleteRuleNameTest {

        @DisplayName("GIVEN an existing ruleName" +
                "WHEN the uri \"/ruleName/delete/{id}\" is called" +
                "THEN there is no error and the page is correctly redirected.")
        @Test
        void deleteRuleNameTest() throws Exception {
            //GIVEN
            // an existing ruleName
            int id = 1;
            doNothing().when(ruleNameService).deleteRuleName(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/delete/{id}", id);
            // WHEN
            //the uri "/ruleName/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/ruleName/list"));
            //the expected methods have been called with expected arguments
            verify(ruleNameService, Mockito.times(1)).deleteRuleName(id);
        }

        @DisplayName("GIVEN a non-existing ruleName" +
                "WHEN the uri \"/ruleName/delete/{id}\" is called" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void deleteRuleNameNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing ruleName
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(ruleNameService).deleteRuleName(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/delete/{id}", id);
            // WHEN
            //the uri "/ruleName/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(ruleNameService, Mockito.times(1)).deleteRuleName(id);
        }
    }


}
