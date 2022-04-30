package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.dto.RatingDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.RatingService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ratingTests")
@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Nested
    @Tag("RatingControllerTests")
    @DisplayName("home tests:")
    class HomeTest {
        @DisplayName("GIVEN a list of rating returned by the service" +
                "WHEN the uri \"/rating/list\" is called " +
                "THEN the expected view is displayed and the model contains expected attribute.")
        @Test
        void homeTest() throws Exception {
            //GIVEN
            //a list of rating returned by the service
            List<RatingDTO> ratingDTOList = new ArrayList<>();
            when(ratingService.getAllRating()).thenReturn(ratingDTOList);
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
                    .andExpect(model().attribute("ratingList", ratingDTOList));
            //the expected methods have been called with expected arguments
            verify(ratingService, Mockito.times(1)).getAllRating();
        }
    }

    @Nested
    @Tag("RatingControllerTests")
    @DisplayName("add tests:")
    class AddTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/rating/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addTest() throws Exception {
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

    @Nested
    @Tag("RatingControllerTests")
    @DisplayName("validate tests:")
    class ValidateTest {

        @DisplayName("GIVEN a ratingDTO with all required information" +
                "WHEN the uri \"/rating/validate\" is called " +
                "THEN a rating with expected information is created and the page is correctly redirected.")
        @Test
        void validateTest() throws Exception {
            //GIVEN
            // a ratingDTO with all required information
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            Rating rating = new Rating(moodysRating, sandPRating, fitchRating,orderNumber);
            final ArgumentCaptor<RatingDTO> arg = ArgumentCaptor.forClass(RatingDTO.class);
            when(ratingService.createRating(any(RatingDTO.class))).thenReturn(rating);
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
            //the expected methods have been called with expected arguments
            verify(ratingService).createRating(arg.capture());
            assertEquals(moodysRating, arg.getValue().getMoodysRating());
            assertEquals(sandPRating, arg.getValue().getSandPRating());
            assertEquals(fitchRating, arg.getValue().getFitchRating());
            assertEquals(orderNumber, arg.getValue().getOrderNumber());
        }
    }

    @Nested
    @Tag("RatingControllerTests")
    @DisplayName("showUpdateForm tests:")
    class ShowUpdateFormTest {

        @DisplayName("GIVEN an existing rating" +
                "WHEN the uri \"/rating/update/{id}\" is called with \"get\" request" +
                "THEN the corresponding ratingDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormTest() throws Exception {
            //GIVEN
            // an existing rating
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            int id = 1;
            RatingDTO ratingDTO = new RatingDTO(id,moodysRating,sandPRating, fitchRating,orderNumber);
            when(ratingService.getRatingDTO(id)).thenReturn(ratingDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/update/{id}", id);
            // WHEN
            //the uri "/rating/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding ratingDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("rating", ratingDTO))
                    .andExpect(view().name("rating/update"));
            //the expected methods have been called with expected arguments
            verify(ratingService, Mockito.times(1)).getRatingDTO(id);
        }

        @DisplayName("GIVEN a non-existing rating" +
                "WHEN the uri \"/rating/update/{id}\" is called with \"get\" request" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing rating
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(ratingService.getRatingDTO(id)).thenThrow(objectNotFoundException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/update/{id}", id);
            // WHEN
            //the uri "/rating/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(ratingService, Mockito.times(1)).getRatingDTO(id);
        }
    }

    @Nested
    @Tag("RatingControllerTests")
    @DisplayName("updateRating tests:")
    class UpdateRatingTest {

        @DisplayName("GIVEN an existing rating and all required information to update" +
                "WHEN the uri \"/rating/update/{id}\" is called with \"post\" request" +
                "THEN there is no error, the expected information is sent and the page is correctly redirected.")
        @Test
        void updateRatingTest() throws Exception {
            //GIVEN
            // an existing rating and all required information to update
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            int id = 1;
            Rating rating = new Rating(id,moodysRating,sandPRating, fitchRating,orderNumber);
            final ArgumentCaptor<RatingDTO> arg = ArgumentCaptor.forClass(RatingDTO.class);
            when(ratingService.updateRating(eq(id), any(RatingDTO.class))).thenReturn(rating);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/rating/update/{id}", id)
                    .param("moodysRating", moodysRating)
                    .param("sandPRating", sandPRating)
                    .param("fitchRating", fitchRating)
                    .param("orderNumber", String.valueOf(orderNumber));
            // WHEN
            //the uri "/rating/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the expected information is sent and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("rating"))
                    .andExpect(redirectedUrl("/rating/list"));
            //the expected methods have been called with expected arguments
            verify(ratingService).updateRating(eq(id), arg.capture());
            assertEquals(moodysRating, arg.getValue().getMoodysRating());
            assertEquals(sandPRating, arg.getValue().getSandPRating());
            assertEquals(fitchRating, arg.getValue().getFitchRating());
            assertEquals(orderNumber, arg.getValue().getOrderNumber());
        }
    }

    @Nested
    @Tag("RatingControllerTests")
    @DisplayName("deleteRating tests:")
    class DeleteRatingTest {

        @DisplayName("GIVEN an existing rating" +
                "WHEN the uri \"/rating/delete/{id}\" is called" +
                "THEN there is no error and the page is correctly redirected.")
        @Test
        void deleteRatingTest() throws Exception {
            //GIVEN
            // an existing rating
            int id = 1;
            doNothing().when(ratingService).deleteRating(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/delete/{id}", id);
            // WHEN
            //the uri "/rating/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/rating/list"));
            //the expected methods have been called with expected arguments
            verify(ratingService, Mockito.times(1)).deleteRating(id);
        }

        @DisplayName("GIVEN a non-existing rating" +
                "WHEN the uri \"/rating/delete/{id}\" is called" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void deleteRatingNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing rating
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(ratingService).deleteRating(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/rating/delete/{id}", id);
            // WHEN
            //the uri "/rating/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(ratingService, Mockito.times(1)).deleteRating(id);
        }
    }


}
