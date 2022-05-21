package com.nnk.springboot.integrationTests;

import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.RatingService;
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

@Tag("ratingTests")
@SpringBootTest
@WithMockUser(username = "user", password = "123456Aa*", roles = "USER")
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = BEFORE_CLASS)
public class RatingIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RatingService ratingService;

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RatingIntegrationTests")
    @DisplayName("homeIntegration tests:")
    class HomeIntegrationTest {
        @DisplayName("GIVEN a list of rating registered in database " +
                "WHEN the uri \"/rating/list\" is called " +
                "THEN the expected view is displayed with all rating registered.")
        @Test
        void homeIntegrationTest() throws Exception {
            //GIVEN
            //a list of rating registered in database
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/list");
            // WHEN
            //the uri "/rating/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed and the model contains expected attribute
                    .andExpect(status().isOk())
                    .andExpect(view().name("rating/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("ratingList"))
                    .andExpect(content().string(containsString("moodysRating1")))
                    .andExpect(content().string(containsString("moodysRating2")))
                    .andExpect(content().string(containsString("moodysRating3")))
                    .andExpect(content().string(containsString("sandPRating1")))
                    .andExpect(content().string(containsString("sandPRating2")))
                    .andExpect(content().string(containsString("sandPRating3")))
                    .andExpect(content().string(containsString("fitchRating1")))
                    .andExpect(content().string(containsString("fitchRating2")))
                    .andExpect(content().string(containsString("fitchRating3")))
                    .andExpect(content().string(containsString(String.valueOf(1))))
                    .andExpect(content().string(containsString(String.valueOf(2))))
                    .andExpect(content().string(containsString(String.valueOf(3))));
        }

        @DisplayName("GIVEN an empty list of rating " +
                "WHEN the uri \"/rating/list\" is called " +
                "THEN the expected view is displayed with an empty list.")
        @Test
        void homeEmptyListIntegrationTest() throws Exception {
            //GIVEN
            //an empty list of rating
            ratingService.deleteRating(1);
            ratingService.deleteRating(2);
            ratingService.deleteRating(3);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/list");
            // WHEN
            //the uri "/rating/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed with an empty list
                    .andExpect(status().isOk())
                    .andExpect(view().name("rating/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("ratingList", empty()));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RatingIntegrationTests")
    @DisplayName("addIntegration tests:")
    class AddIntegrationTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/rating/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addIntegrationTest() throws Exception {
            //GIVEN
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/add");
            // WHEN
            //the uri "/rating/add" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the status is "isOk" and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("rating/add"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("rating"));
        }
    }


    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RatingIntegrationTests")
    @DisplayName("validateIntegration tests:")
    class ValidateIntegrationTest {

        @DisplayName("GIVEN a ratingDTO with all required information " +
                "WHEN the uri \"/rating/validate\" is called " +
                "THEN a rating with expected information is created and the page is correctly redirected.")
        @Test
        void validateIntegrationTest() throws Exception {
            //GIVEN
            // a ratingDTO with all required information
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/rating/validate")
                    .param("moodysRating", moodysRating)
                    .param("sandPRating", sandPRating)
                    .param("fitchRating", fitchRating)
                    .param("orderNumber", String.valueOf(orderNumber));
            // WHEN
            //the uri "/rating/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // a rating with expected information is created and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("rating"))
                    .andExpect(redirectedUrl("/rating/list"));
            assertEquals(4, ratingService.getAllRating().size());
            assertEquals(moodysRating, ratingService.getAllRating().get(3).getMoodysRating());
            assertEquals(sandPRating, ratingService.getAllRating().get(3).getSandPRating());
            assertEquals(fitchRating, ratingService.getAllRating().get(3).getFitchRating());
            assertEquals(orderNumber, ratingService.getAllRating().get(3).getOrderNumber());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RatingIntegrationTests")
    @DisplayName("showUpdateFormIntegration tests:")
    class ShowUpdateFormIntegrationTest {

        @DisplayName("GIVEN an existing rating " +
                "WHEN the uri \"/rating/update/{id}\" is called with \"get\" request " +
                "THEN the corresponding ratingDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormIntegrationTest() throws Exception {
            //GIVEN
            // an existing rating
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/update/{id}", 1);
            // WHEN
            //the uri "/rating/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding ratingDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("rating"))
                    .andExpect(view().name("rating/update"))
                    .andExpect(content().string(containsString("moodysRating1")))
                    .andExpect(content().string(containsString("sandPRating1")))
                    .andExpect(content().string(containsString("fitchRating1")))
                    .andExpect(content().string(containsString(String.valueOf(1))));
        }

        @DisplayName("GIVEN a non-existing rating " +
                "WHEN the uri \"/rating/update/{id}\" is called with \"get\" request " +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingIntegrationTest() throws Exception {
            //GIVEN
            // a non-existing rating
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/update/{id}", 4);
            // WHEN
            //the uri "/rating/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The rating with id number 4 was not found.")));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RatingIntegrationTests")
    @DisplayName("updateRatingIntegration tests:")
    class UpdateRatingIntegrationTest {

        @DisplayName("GIVEN an existing rating and all required information to update " +
                "WHEN the uri \"/rating/update/{id}\" is called with \"post\" request " +
                "THEN there is no error, the rating is correctly modified and the page is correctly redirected.")
        @Test
        void updateRatingIntegrationTest() throws Exception {
            //GIVEN
            // an existing rating and all required information to update
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/rating/update/{id}", 1)
                    .param("moodysRating", moodysRating)
                    .param("sandPRating", sandPRating)
                    .param("fitchRating", fitchRating)
                    .param("orderNumber", String.valueOf(orderNumber));
            // WHEN
            //the uri "/rating/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the rating is correctly modified and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("rating"))
                    .andExpect(redirectedUrl("/rating/list"));
            assertEquals(3, ratingService.getAllRating().size());
            assertEquals(moodysRating, ratingService.getRatingDTO(1).getMoodysRating());
            assertEquals(sandPRating, ratingService.getRatingDTO(1).getSandPRating());
            assertEquals(fitchRating, ratingService.getRatingDTO(1).getFitchRating());
            assertEquals(orderNumber, ratingService.getRatingDTO(1).getOrderNumber());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("RatingIntegrationTests")
    @DisplayName("deleteRatingIntegration tests:")
    class DeleteRatingIntegrationTest {

        @DisplayName("GIVEN an existing rating " +
                "WHEN the uri \"/rating/delete/{id}\" is called " +
                "THEN there is no error, the rating doesn't exist anymore and the page is correctly redirected.")
        @Test
        void deleteRatingIntegrationTest() throws Exception {
            //GIVEN
            // an existing rating
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/delete/{id}", 1);
            // WHEN
            //the uri "/rating/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the rating doesn't exist anymore and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/rating/list"));
            assertEquals(2, ratingService.getAllRating().size());
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ratingService.getRatingDTO(1));
            assertEquals("The rating with id number 1 was not found.", exception.getMessage());
        }

        @DisplayName("GIVEN a non-existing rating " +
                "WHEN the uri \"/rating/delete/{id}\" is called " +
                "THEN an error page is displayed with expected error message and no rating have been suppressed.")
        @Test
        void deleteRatingNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing rating
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/delete/{id}", 4);
            // WHEN
            //the uri "/rating/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The rating with id number 4 was not found so it could not have been deleted")));
            assertEquals(3, ratingService.getAllRating().size());
        }
    }
}
