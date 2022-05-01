package com.nnk.springboot.integrationTests;

import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ruleNameTests")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = BEFORE_CLASS)
public class RuleNameIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RuleNameService ruleNameService;

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RuleNameIntegrationTests")
    @DisplayName("homeIntegration tests:")
    class HomeIntegrationTest {
        @DisplayName("GIVEN a list of ruleName registered in database " +
                "WHEN the uri \"/ruleName/list\" is called " +
                "THEN the expected view is displayed with all ruleName registered.")
        @Test
        void homeIntegrationTest() throws Exception {
            //GIVEN
            //a list of ruleName registered in database
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
                    .andExpect(model().attributeExists("ruleNameList"))
                    .andExpect(content().string(containsString("name1")))
                    .andExpect(content().string(containsString("name2")))
                    .andExpect(content().string(containsString("name3")))
                    .andExpect(content().string(containsString("description1")))
                    .andExpect(content().string(containsString("description2")))
                    .andExpect(content().string(containsString("description3")))
                    .andExpect(content().string(containsString("json1")))
                    .andExpect(content().string(containsString("json2")))
                    .andExpect(content().string(containsString("json3")))
                    .andExpect(content().string(containsString("template1")))
                    .andExpect(content().string(containsString("template2")))
                    .andExpect(content().string(containsString("template3")))
                    .andExpect(content().string(containsString("sqlStr1")))
                    .andExpect(content().string(containsString("sqlStr2")))
                    .andExpect(content().string(containsString("sqlStr3")))
                    .andExpect(content().string(containsString("sqlPart1")))
                    .andExpect(content().string(containsString("sqlPart2")))
                    .andExpect(content().string(containsString("sqlPart3")));
        }

        @DisplayName("GIVEN an empty list of ruleName " +
                "WHEN the uri \"/ruleName/list\" is called " +
                "THEN the expected view is displayed with an empty list.")
        @Test
        void homeEmptyListIntegrationTest() throws Exception {
            //GIVEN
            //an empty list of ruleName
            ruleNameService.deleteRuleName(1);
            ruleNameService.deleteRuleName(2);
            ruleNameService.deleteRuleName(3);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/list");
            // WHEN
            //the uri "/ruleName/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed with an empty list
                    .andExpect(status().isOk())
                    .andExpect(view().name("ruleName/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("ruleNameList", empty()));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RuleNameIntegrationTests")
    @DisplayName("addIntegration tests:")
    class AddIntegrationTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/ruleName/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addIntegrationTest() throws Exception {
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


    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RuleNameIntegrationTests")
    @DisplayName("validateIntegration tests:")
    class ValidateIntegrationTest {

        @DisplayName("GIVEN a ruleNameDTO with all required information " +
                "WHEN the uri \"/ruleName/validate\" is called " +
                "THEN a ruleName with expected information is created and the page is correctly redirected.")
        @Test
        void validateIntegrationTest() throws Exception {
            //GIVEN
            // a ruleNameDTO with all required information
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
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
            assertEquals(4, ruleNameService.getAllRuleName().size());
            assertEquals(name, ruleNameService.getAllRuleName().get(3).getName());
            assertEquals(description, ruleNameService.getAllRuleName().get(3).getDescription());
            assertEquals(json, ruleNameService.getAllRuleName().get(3).getJson());
            assertEquals(template, ruleNameService.getAllRuleName().get(3).getTemplate());
            assertEquals(sqlStr, ruleNameService.getAllRuleName().get(3).getSqlStr());
            assertEquals(sqlPart, ruleNameService.getAllRuleName().get(3).getSqlPart());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RuleNameIntegrationTests")
    @DisplayName("showUpdateFormIntegration tests:")
    class ShowUpdateFormIntegrationTest {

        @DisplayName("GIVEN an existing ruleName " +
                "WHEN the uri \"/ruleName/update/{id}\" is called with \"get\" request " +
                "THEN the corresponding ruleNameDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormIntegrationTest() throws Exception {
            //GIVEN
            // an existing ruleName
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/update/{id}", 1);
            // WHEN
            //the uri "/ruleName/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding ruleNameDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("ruleName"))
                    .andExpect(view().name("ruleName/update"))
                    .andExpect(content().string(containsString("name1")))
                    .andExpect(content().string(containsString("description1")))
                    .andExpect(content().string(containsString("json1")))
                    .andExpect(content().string(containsString("template1")))
                    .andExpect(content().string(containsString("sqlStr1")))
                    .andExpect(content().string(containsString("sqlPart1")));
        }

        @DisplayName("GIVEN a non-existing ruleName " +
                "WHEN the uri \"/ruleName/update/{id}\" is called with \"get\" request " +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingIntegrationTest() throws Exception {
            //GIVEN
            // a non-existing ruleName
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/update/{id}", 4);
            // WHEN
            //the uri "/ruleName/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The ruleName with id number 4 was not found.")));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RuleNameIntegrationTests")
    @DisplayName("updateRuleNameIntegration tests:")
    class UpdateRuleNameIntegrationTest {

        @DisplayName("GIVEN an existing ruleName and all required information to update " +
                "WHEN the uri \"/ruleName/update/{id}\" is called with \"post\" request " +
                "THEN there is no error, the ruleName is correctly modified and the page is correctly redirected.")
        @Test
        void updateRuleNameIntegrationTest() throws Exception {
            //GIVEN
            // an existing ruleName and all required information to update
            String name = "name";
            String description = "description";
            String json = "json";
            String template = "template";
            String sqlStr = "sqlStr";
            String sqlPart = "sqlPart";
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/ruleName/update/{id}", 1)
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
                    // there is no error, the ruleName is correctly modified and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("ruleName"))
                    .andExpect(redirectedUrl("/ruleName/list"));
            assertEquals(3, ruleNameService.getAllRuleName().size());
            assertEquals(name, ruleNameService.getRuleNameDTO(1).getName());
            assertEquals(description, ruleNameService.getRuleNameDTO(1).getDescription());
            assertEquals(json, ruleNameService.getRuleNameDTO(1).getJson());
            assertEquals(template, ruleNameService.getRuleNameDTO(1).getTemplate());
            assertEquals(sqlStr, ruleNameService.getRuleNameDTO(1).getSqlStr());
            assertEquals(sqlPart, ruleNameService.getRuleNameDTO(1).getSqlPart());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RuleNameIntegrationTests")
    @DisplayName("deleteRuleNameIntegration tests:")
    class DeleteRuleNameIntegrationTest {

        @DisplayName("GIVEN an existing ruleName " +
                "WHEN the uri \"/ruleName/delete/{id}\" is called " +
                "THEN there is no error, the ruleName doesn't exist anymore and the page is correctly redirected.")
        @Test
        void deleteRuleNameIntegrationTest() throws Exception {
            //GIVEN
            // an existing ruleName
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/delete/{id}", 1);
            // WHEN
            //the uri "/ruleName/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the ruleName doesn't exist anymore and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/ruleName/list"));
            assertEquals(2, ruleNameService.getAllRuleName().size());
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ruleNameService.getRuleNameDTO(1));
            assertEquals("The ruleName with id number 1 was not found.", exception.getMessage());
        }

        @DisplayName("GIVEN a non-existing ruleName " +
                "WHEN the uri \"/ruleName/delete/{id}\" is called " +
                "THEN an error page is displayed with expected error message and no ruleName have been suppressed.")
        @Test
        void deleteRuleNameNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing ruleName
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/ruleName/delete/{id}", 4);
            // WHEN
            //the uri "/ruleName/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The ruleName with id number 4 was not found so it could not have been deleted")));
            assertEquals(3, ruleNameService.getAllRuleName().size());
        }
    }
}
