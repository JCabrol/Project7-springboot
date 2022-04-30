package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.dto.RatingDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
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

@Tag("ratingTests")
@SpringBootTest
public class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @MockBean
    private RatingRepository ratingRepository;


    @Nested
    @Tag("RatingServiceTests")
    @DisplayName("createRating tests:")
    class CreateRatingTest {


        @DisplayName("GIVEN a ratingDTO containing all required information" +
                "WHEN the function createRating() is called " +
                "THEN a rating is created with expected information.")
        @Test
        void createRatingTest() {
            //GIVEN
            //a ratingDTO containing all required information
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            RatingDTO ratingDTO = new RatingDTO(moodysRating, sandPRating, fitchRating, orderNumber);
            Rating rating = new Rating(moodysRating, sandPRating, fitchRating, orderNumber);
            final ArgumentCaptor<Rating> arg = ArgumentCaptor.forClass(Rating.class);
            when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
            //WHEN
            //the function createRating() is called
            Rating result = ratingService.createRating(ratingDTO);
            //THEN
            //a rating is created with expected information
            assertThat(result).isInstanceOf(Rating.class);
            //the expected methods have been called with expected arguments
            verify(ratingRepository).save(arg.capture());
            assertEquals(moodysRating, arg.getValue().getMoodysRating());
            assertEquals(sandPRating, arg.getValue().getSandPRating());
            assertEquals(fitchRating, arg.getValue().getFitchRating());
            assertEquals(orderNumber, arg.getValue().getOrderNumber());
        }
    }


    @Nested
    @Tag("RatingServiceTests")
    @DisplayName("getAllRating tests:")
    class GetAllRatingTest {

        @DisplayName("GIVEN no ratings returned by ratingRepository " +
                "WHEN the function getAllRating() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllRatingWhenEmptyTest() {
            //GIVEN
            //no ratings returned by ratingRepository
            List<Rating> allRatingTest = new ArrayList<>();
            when(ratingRepository.findAll()).thenReturn(allRatingTest);
            //WHEN
            //the function getAllRating() is called
            List<RatingDTO> result = ratingService.getAllRating();
            //THEN
            //it returns an empty list
            assertThat(result).isEmpty();
            //and the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN ratings returned by ratingRepository " +
                "WHEN the function getAllRating() is called " +
                "THEN it returns the expected list.")
        @Test
        void getAllRatingWhenNotEmptyTest() {
            //GIVEN
            //ratings returned by ratingRepository
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            String moodysRating2 = "moodysRating2";
            String sandPRating2 = "sandPRating2";
            String fitchRating2 = "fitchRating2";
            Integer orderNumber2 = 2;
            Rating rating = new Rating(moodysRating, sandPRating, fitchRating, orderNumber);
            Rating rating2 = new Rating(moodysRating2, sandPRating2, fitchRating2, orderNumber2);
            List<Rating> allRatingTest = new ArrayList<>();
            allRatingTest.add(rating);
            allRatingTest.add(rating2);
            when(ratingRepository.findAll()).thenReturn(allRatingTest);
            //WHEN
            //the function getAllRating() is called
            List<RatingDTO> result = ratingService.getAllRating();
            //THEN
            //it returns the expected list
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getMoodysRating()).isEqualTo(moodysRating);
            assertThat(result.get(0).getSandPRating()).isEqualTo(sandPRating);
            assertThat(result.get(0).getFitchRating()).isEqualTo(fitchRating);
            assertThat(result.get(0).getOrderNumber()).isEqualTo(orderNumber);
            assertThat(result.get(1).getMoodysRating()).isEqualTo(moodysRating2);
            assertThat(result.get(1).getSandPRating()).isEqualTo(sandPRating2);
            assertThat(result.get(1).getFitchRating()).isEqualTo(fitchRating2);
            assertThat(result.get(1).getOrderNumber()).isEqualTo(orderNumber2);
            //and the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).findAll();
        }
    }


    @Nested
    @Tag("RatingServiceTests")
    @DisplayName("getRatingDTO tests:")
    class GetRatingDTOTest {

        @DisplayName("GIVEN an existing rating " +
                "WHEN the function getRatingDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getRatingDTOExistingTest() {
            //GIVEN
            //an existing rating
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            Integer id = 1;
            Rating rating = new Rating(moodysRating, sandPRating, fitchRating, orderNumber);
            rating.setId(id);
            when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));
            //WHEN
            //the function getRatingDTO() is called
            RatingDTO result = ratingService.getRatingDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(RatingDTO.class);
            assertThat(result.getMoodysRating()).isEqualTo(moodysRating);
            assertThat(result.getSandPRating()).isEqualTo(sandPRating);
            assertThat(result.getFitchRating()).isEqualTo(fitchRating);
            assertThat(result.getOrderNumber()).isEqualTo(orderNumber);
            assertThat(result.getId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).findById(id);
        }


        @DisplayName("GIVEN a non-existing rating " +
                "WHEN the function getRatingDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getRatingDTONonExistingTest() {
            //GIVEN
            //a non-existing rating
            Integer id = 1;
            when(ratingRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getRatingDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ratingService.getRatingDTO(id));
            assertEquals("The rating with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).findById(id);
        }
    }


    @Nested
    @Tag("RatingServiceTests")
    @DisplayName("updateRatingDTO tests:")
    class UpdateRatingDTOTest {

        @DisplayName("GIVEN an existing rating and a RatingDTO containing all information" +
                "WHEN the function updateRatingDTO() is called " +
                "THEN it returns the Rating object with information updated and other information unchanged.")
        @Test
        void updateRatingDTOExistingTest() {
            //GIVEN
            //an existing rating and a RatingDTO containing all information
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            String moodysRating2 = "moodysRating2";
            String sandPRating2 = "sandPRating2";
            String fitchRating2 = "fitchRating2";
            Integer orderNumber2 = 2;
            Integer id = 1;
            Rating rating = new Rating(moodysRating, sandPRating, fitchRating, orderNumber);
            RatingDTO ratingDTO = new RatingDTO(moodysRating2, sandPRating2, fitchRating2, orderNumber2);
            rating.setId(id);
            final ArgumentCaptor<Rating> arg = ArgumentCaptor.forClass(Rating.class);
            when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));
            when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
            //WHEN
            //the function updateRatingDTO() is called
            Rating result = ratingService.updateRating(id, ratingDTO);
            //THEN
            // it returns the Rating object with information updated and other information unchanged
            assertThat(result).isInstanceOf(Rating.class);
            //and the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).findById(id);
            verify(ratingRepository).save(arg.capture());
            assertEquals(moodysRating2, arg.getValue().getMoodysRating());
            assertEquals(sandPRating2, arg.getValue().getSandPRating());
            assertEquals(fitchRating2, arg.getValue().getFitchRating());
            assertEquals(orderNumber2, arg.getValue().getOrderNumber());
            assertEquals(id, arg.getValue().getId());
        }

        @DisplayName("GIVEN a non-existing rating " +
                "WHEN the function updateRatingDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void updateRatingDTONonExistingTest() {
            //GIVEN
            //a non-existing rating
            Integer id = 1;
            String moodysRating = "moodysRating";
            String sandPRating = "sandPRating";
            String fitchRating = "fitchRating";
            Integer orderNumber = 1;
            RatingDTO ratingDTO = new RatingDTO(moodysRating, sandPRating, fitchRating, orderNumber);
            when(ratingRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getRatingDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ratingService.updateRating(id, ratingDTO));
            assertEquals("The rating with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).findById(id);
            verify(ratingRepository, Mockito.times(0)).save(any(Rating.class));
        }
    }

    @Nested
    @Tag("RatingServiceTests")
    @DisplayName("deleteRating tests:")
    class DeleteRatingTest {

        @DisplayName("GIVEN an existing rating" +
                "WHEN the function deleteRating() is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void deleteRatingExistingTest() {
            //GIVEN
            //an existing rating
            Integer id = 1;
            when(ratingRepository.existsById(id)).thenReturn(true);
            doNothing().when(ratingRepository).deleteById(id);
            //WHEN
            //the function deleteRating() is called
            ratingService.deleteRating(id);
            //THEN
            //the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).existsById(id);
            verify(ratingRepository, Mockito.times(1)).deleteById(id);
        }

        @DisplayName("GIVEN a non-existing rating " +
                "WHEN the function deleteRating() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deleteRatingDTONonExistingTest() {
            //GIVEN
            //a non-existing rating
            Integer id = 1;
            when(ratingRepository.existsById(id)).thenReturn(false);
            //WHEN
            //the function deleteRating() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> ratingService.deleteRating(id));
            assertEquals("The rating with id number " + id + " was not found so it could not have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(ratingRepository, Mockito.times(1)).existsById(id);
            verify(ratingRepository, Mockito.times(0)).deleteById(anyInt());
        }
    }

}
