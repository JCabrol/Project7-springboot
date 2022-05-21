package com.nnk.springboot.integrationTests;

import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.CurvePointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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

@Tag("curvePointTests")
@SpringBootTest
@WithMockUser(username = "user", password = "123456Aa*", roles = "USER")
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = BEFORE_CLASS)
public class CurvePointIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CurvePointService curvePointService;

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("CurvePointIntegrationTests")
    @DisplayName("homeIntegration tests:")
    class HomeIntegrationTest {
        @DisplayName("GIVEN a list of curvePoint registered in database " +
                "WHEN the uri \"/curvePoint/list\" is called " +
                "THEN the expected view is displayed with all curvePoint registered.")
        @Test
        void homeIntegrationTest() throws Exception {
            //GIVEN
            //a list of curvePoint registered in database
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/list");
            // WHEN
            //the uri "/curvePoint/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed and the model contains expected attribute
                    .andExpect(status().isOk())
                    .andExpect(view().name("curvePoint/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("curvePointList"))
                    .andExpect(content().string(containsString(String.valueOf(1))))
                    .andExpect(content().string(containsString(String.valueOf(2))))
                    .andExpect(content().string(containsString(String.valueOf(3))))
                    .andExpect(content().string(containsString(String.valueOf(11.1))))
                    .andExpect(content().string(containsString(String.valueOf(22.2))))
                    .andExpect(content().string(containsString(String.valueOf(33.3))))
                    .andExpect(content().string(containsString(String.valueOf(10.0))))
                    .andExpect(content().string(containsString(String.valueOf(20.0))))
                    .andExpect(content().string(containsString(String.valueOf(30.0))));
        }

        @DisplayName("GIVEN an empty list of curvePoint " +
                "WHEN the uri \"/curvePoint/list\" is called " +
                "THEN the expected view is displayed with an empty list.")
        @Test
        void homeEmptyListIntegrationTest() throws Exception {
            //GIVEN
            //an empty list of curvePoint
            curvePointService.deleteCurvePoint(1);
            curvePointService.deleteCurvePoint(2);
            curvePointService.deleteCurvePoint(3);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/list");
            // WHEN
            //the uri "/curvePoint/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed with an empty list
                    .andExpect(status().isOk())
                    .andExpect(view().name("curvePoint/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("curvePointList", empty()));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("CurvePointIntegrationTests")
    @DisplayName("addIntegration tests:")
    class AddIntegrationTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/curvePoint/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addIntegrationTest() throws Exception {
            //GIVEN
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/add");
            // WHEN
            //the uri "/curvePoint/add" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the status is "isOk" and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("curvePoint/add"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("curvePoint"));
        }
    }


    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("CurvePointIntegrationTests")
    @DisplayName("validateIntegration tests:")
    class ValidateIntegrationTest {

        @DisplayName("GIVEN a curvePointDTO with all required information " +
                "WHEN the uri \"/curvePoint/validate\" is called " +
                "THEN a curvePoint with expected information is created and the page is correctly redirected.")
        @Test
        void validateIntegrationTest() throws Exception {
            //GIVEN
            // a curvePointDTO with all required information
            Integer curveId = 4;
            Double term = 40.0;
            Double value = 44.4;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/curvePoint/validate")
                    .param("curveId", String.valueOf(curveId))
                    .param("term", String.valueOf(term))
                    .param("value", String.valueOf(value));
            // WHEN
            //the uri "/curvePoint/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // a curvePoint with expected information is created and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("curvePoint"))
                    .andExpect(redirectedUrl("/curvePoint/list"));
            assertEquals(4, curvePointService.getAllCurvePoint().size());
            assertEquals(curveId, curvePointService.getAllCurvePoint().get(3).getCurveId());
            assertEquals(term, curvePointService.getAllCurvePoint().get(3).getTerm());
            assertEquals(value, curvePointService.getAllCurvePoint().get(3).getValue());
        }

        @DisplayName("GIVEN a curvePointDTO without required information " +
                "WHEN the uri \"/curvePoint/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned and no curvePoint have been created.")
        @Test
        void validateMissingInformationIntegrationTest() throws Exception {
            //GIVEN
            // a curvePointDTO without required information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/curvePoint/validate");
            // WHEN
            //the uri "/curvePoint/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned and no curvePoint have been created
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("curvePoint"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("curvePoint", 1))
                    .andExpect(view().name("curvePoint/add"))
                    .andExpect(content().string(containsString("Must not be null")));
            assertEquals(3, curvePointService.getAllCurvePoint().size());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("CurvePointIntegrationTests")
    @DisplayName("showUpdateFormIntegration tests:")
    class ShowUpdateFormIntegrationTest {

        @DisplayName("GIVEN an existing curvePoint " +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"get\" request " +
                "THEN the corresponding curvePointDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormIntegrationTest() throws Exception {
            //GIVEN
            // an existing curvePoint
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/update/{id}", 1);
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding curvePointDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("curvePoint"))
                    .andExpect(view().name("curvePoint/update"))
                    .andExpect(content().string(containsString(String.valueOf(1))))
                    .andExpect(content().string(containsString(String.valueOf(11.1))))
                    .andExpect(content().string(containsString(String.valueOf(10.0))));
        }

        @DisplayName("GIVEN a non-existing curvePoint " +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"get\" request " +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingIntegrationTest() throws Exception {
            //GIVEN
            // a non-existing curvePoint
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/update/{id}", 4);
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The curvePoint with id number 4 was not found.")));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("CurvePointIntegrationTests")
    @DisplayName("updateCurvePointIntegration tests:")
    class UpdateCurvePointIntegrationTest {

        @DisplayName("GIVEN an existing curvePoint and all required information to update " +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"post\" request " +
                "THEN there is no error, the curvePoint is correctly modified and the page is correctly redirected.")
        @Test
        void updateCurvePointIntegrationTest() throws Exception {
            //GIVEN
            // an existing curvePoint and all required information to update
            Integer newCurveId = 4;
            Double newTerm = 40.0;
            Double newValue = 44.4;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/curvePoint/update/{id}", 1)
                    .param("curveId", String.valueOf(newCurveId))
                    .param("term", String.valueOf(newTerm))
                    .param("value", String.valueOf(newValue));
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the curvePoint is correctly modified and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("curvePoint"))
                    .andExpect(redirectedUrl("/curvePoint/list"));
            assertEquals(3, curvePointService.getAllCurvePoint().size());
            assertEquals(newCurveId, curvePointService.getCurvePointDTO(1).getCurveId());
            assertEquals(newTerm, curvePointService.getCurvePointDTO(1).getTerm());
            assertEquals(newValue, curvePointService.getCurvePointDTO(1).getValue());
        }

        @DisplayName("GIVEN missing information " +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"post\" request " +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateCurvePointMissingInformationIntegrationTest() throws Exception {
            //GIVEN
            // missing information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/curvePoint/update/{id}", 1);
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned and the curvePoint is not modified
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("curvePoint"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("curvePoint", 1))
                    .andExpect(view().name("curvePoint/update"))
                    .andExpect(content().string(containsString("Must not be null")));
            assertEquals(3, curvePointService.getAllCurvePoint().size());
            assertEquals(1, curvePointService.getCurvePointDTO(1).getCurveId());
            assertEquals(10.0, curvePointService.getCurvePointDTO(1).getTerm());
            assertEquals(11.1, curvePointService.getCurvePointDTO(1).getValue());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("CurvePointIntegrationTests")
    @DisplayName("deleteCurvePointIntegration tests:")
    class DeleteCurvePointIntegrationTest {

        @DisplayName("GIVEN an existing curvePoint " +
                "WHEN the uri \"/curvePoint/delete/{id}\" is called " +
                "THEN there is no error, the curvePoint doesn't exist anymore and the page is correctly redirected.")
        @Test
        void deleteCurvePointIntegrationTest() throws Exception {
            //GIVEN
            // an existing curvePoint
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/delete/{id}", 1);
            // WHEN
            //the uri "/curvePoint/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the curvePoint doesn't exist anymore and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/curvePoint/list"));
            assertEquals(2, curvePointService.getAllCurvePoint().size());
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> curvePointService.getCurvePointDTO(1));
            assertEquals("The curvePoint with id number 1 was not found.", exception.getMessage());
        }

        @DisplayName("GIVEN a non-existing curvePoint " +
                "WHEN the uri \"/curvePoint/delete/{id}\" is called " +
                "THEN an error page is displayed with expected error message and no curvePoint have been suppressed.")
        @Test
        void deleteCurvePointNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing curvePoint
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/delete/{id}", 4);
            // WHEN
            //the uri "/curvePoint/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The curvePoint with id number 4 was not found so it could not have been deleted")));
            assertEquals(3, curvePointService.getAllCurvePoint().size());
        }
    }
}
