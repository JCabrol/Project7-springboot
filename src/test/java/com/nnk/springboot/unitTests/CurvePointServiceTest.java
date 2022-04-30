package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.dto.CurvePointDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("curvePointTests")
@SpringBootTest
public class CurvePointServiceTest {

    @Autowired
    private CurvePointService curvePointService;

    @MockBean
    private CurvePointRepository curvePointRepository;


    @Nested
    @Tag("CurvePointServiceTests")
    @DisplayName("createCurvePoint tests:")
    class CreateCurvePointTest {


        @DisplayName("GIVEN a curvePointDTO containing all required information" +
                "WHEN the function createCurvePoint() is called " +
                "THEN a curvePoint is created with expected information.")
        @Test
        void createCurvePointTest() {
            //GIVEN
            //a curvePointDTO containing all required information
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            CurvePointDTO curvePointDTO = new CurvePointDTO(curveId, term, value);
            CurvePoint curvePoint = new CurvePoint(curveId, term, value);
            final ArgumentCaptor<CurvePoint> arg = ArgumentCaptor.forClass(CurvePoint.class);
            when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
            //WHEN
            //the function createCurvePoint() is called
            Timestamp before = Timestamp.valueOf(LocalDateTime.now());
            CurvePoint result = curvePointService.createCurvePoint(curvePointDTO);
            Timestamp after = Timestamp.valueOf(LocalDateTime.now());
            //THEN
            //a curvePoint is created with expected information
            assertThat(result).isInstanceOf(CurvePoint.class);
            //the expected methods have been called with expected arguments
            verify(curvePointRepository).save(arg.capture());
            assertEquals(curveId, arg.getValue().getCurveId());
            assertEquals(term, arg.getValue().getTerm());
            assertEquals(value, arg.getValue().getValue());
            assertThat(arg.getValue().getCreationDate()).isBetween(before, after);
            assertNull(arg.getValue().getAsOfDate());
        }
    }


    @Nested
    @Tag("CurvePointServiceTests")
    @DisplayName("getAllCurvePoint tests:")
    class GetAllCurvePointTest {

        @DisplayName("GIVEN no curvePoints returned by curvePointRepository " +
                "WHEN the function getAllCurvePoint() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllCurvePointWhenEmptyTest() {
            //GIVEN
            //no curvePoints returned by curvePointRepository
            List<CurvePoint> allCurvePointTest = new ArrayList<>();
            when(curvePointRepository.findAll()).thenReturn(allCurvePointTest);
            //WHEN
            //the function getAllCurvePoint() is called
            List<CurvePointDTO> result = curvePointService.getAllCurvePoint();
            //THEN
            //it returns an empty list
            assertThat(result).isEmpty();
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN curvePoints returned by curvePointRepository " +
                "WHEN the function getAllCurvePoint() is called " +
                "THEN it returns the expected list.")
        @Test
        void getAllCurvePointWhenNotEmptyTest() {
            //GIVEN
            //curvePoints returned by curvePointRepository
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            Integer curveId2 = 2;
            Double term2 = 52.0;
            Double value2 = 10.52;
            CurvePoint curvePoint = new CurvePoint(curveId, term, value);
            CurvePoint curvePoint2 = new CurvePoint(curveId2, term2, value2);
            List<CurvePoint> allCurvePointTest = new ArrayList<>();
            allCurvePointTest.add(curvePoint);
            allCurvePointTest.add(curvePoint2);
            when(curvePointRepository.findAll()).thenReturn(allCurvePointTest);
            //WHEN
            //the function getAllCurvePoint() is called
            List<CurvePointDTO> result = curvePointService.getAllCurvePoint();
            //THEN
            //it returns the expected list
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getCurveId()).isEqualTo(curveId);
            assertThat(result.get(0).getTerm()).isEqualTo(term);
            assertThat(result.get(0).getValue()).isEqualTo(value);
            assertThat(result.get(1).getCurveId()).isEqualTo(curveId2);
            assertThat(result.get(1).getTerm()).isEqualTo(term2);
            assertThat(result.get(1).getValue()).isEqualTo(value2);
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).findAll();
        }
    }


    @Nested
    @Tag("CurvePointServiceTests")
    @DisplayName("getCurvePointDTO tests:")
    class GetCurvePointDTOTest {

        @DisplayName("GIVEN an existing curvePoint " +
                "WHEN the function getCurvePointDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getCurvePointDTOExistingTest() {
            //GIVEN
            //an existing curvePoint
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            Integer id = 1;
            CurvePoint curvePoint = new CurvePoint(curveId, term, value);
            curvePoint.setId(id);
            when(curvePointRepository.findById(id)).thenReturn(Optional.of(curvePoint));
            //WHEN
            //the function getCurvePointDTO() is called
            CurvePointDTO result = curvePointService.getCurvePointDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(CurvePointDTO.class);
            assertThat(result.getCurveId()).isEqualTo(curveId);
            assertThat(result.getTerm()).isEqualTo(term);
            assertThat(result.getValue()).isEqualTo(value);
            assertThat(result.getId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).findById(id);
        }

        @DisplayName("GIVEN an existing curvePoint with all attributes " +
                "WHEN the function getCurvePointDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getCurvePointDTOAllAttributesTest() {
            //GIVEN
            //an existing curvePoint with all attributes
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            Integer id = 1;
            CurvePoint curvePoint = new CurvePoint(id,curveId,Timestamp.valueOf(LocalDateTime.now()), term, value,Timestamp.valueOf(LocalDateTime.now()));
            when(curvePointRepository.findById(id)).thenReturn(Optional.of(curvePoint));
            //WHEN
            //the function getCurvePointDTO() is called
            CurvePointDTO result = curvePointService.getCurvePointDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(CurvePointDTO.class);
            assertThat(result.getCurveId()).isEqualTo(curveId);
            assertThat(result.getTerm()).isEqualTo(term);
            assertThat(result.getValue()).isEqualTo(value);
            assertThat(result.getId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).findById(id);
        }


        @DisplayName("GIVEN a non-existing curvePoint " +
                "WHEN the function getCurvePointDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getCurvePointDTONonExistingTest() {
            //GIVEN
            //a non-existing curvePoint
            Integer id = 1;
            when(curvePointRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getCurvePointDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> curvePointService.getCurvePointDTO(id));
            assertEquals("The curvePoint with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).findById(id);
        }
    }


    @Nested
    @Tag("CurvePointServiceTests")
    @DisplayName("updateCurvePointDTO tests:")
    class UpdateCurvePointDTOTest {

        @DisplayName("GIVEN an existing curvePoint and a CurvePointDTO containing all information" +
                "WHEN the function updateCurvePointDTO() is called " +
                "THEN it returns the CurvePoint object with information updated and other information unchanged.")
        @Test
        void updateCurvePointDTOExistingTest() {
            //GIVEN
            //an existing curvePoint and a CurvePointDTO containing all information
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            Integer curveId2 = 2;
            Double term2 = 52.0;
            Double value2 = 12.5;
            Integer id = 1;
            CurvePoint curvePoint = new CurvePoint(curveId, term, value);
            CurvePointDTO curvePointDTO = new CurvePointDTO(curveId2, term2, value2);
            curvePoint.setId(id);
            curvePoint.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
            curvePoint.setAsOfDate(Timestamp.valueOf(LocalDateTime.now()));
            final ArgumentCaptor<CurvePoint> arg = ArgumentCaptor.forClass(CurvePoint.class);
            when(curvePointRepository.findById(id)).thenReturn(Optional.of(curvePoint));
            when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
            //WHEN
            //the function updateCurvePointDTO() is called
            CurvePoint result = curvePointService.updateCurvePoint(id, curvePointDTO);
            //THEN
            // it returns the CurvePoint object with information updated and other information unchanged
            assertThat(result).isInstanceOf(CurvePoint.class);
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).findById(id);
            verify(curvePointRepository).save(arg.capture());
            assertEquals(curveId2, arg.getValue().getCurveId());
            assertEquals(term2, arg.getValue().getTerm());
            assertEquals(value2, arg.getValue().getValue());
            assertEquals(id, arg.getValue().getId());
            assertEquals(curvePoint.getCreationDate(), arg.getValue().getCreationDate());
            assertEquals(curvePoint.getAsOfDate(), arg.getValue().getAsOfDate());
        }

        @DisplayName("GIVEN a non-existing curvePoint " +
                "WHEN the function updateCurvePointDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void updateCurvePointDTONonExistingTest() {
            //GIVEN
            //a non-existing curvePoint
            Integer id = 1;
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            CurvePointDTO curvePointDTO = new CurvePointDTO(curveId, term, value);
            when(curvePointRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getCurvePointDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> curvePointService.updateCurvePoint(id, curvePointDTO));
            assertEquals("The curvePoint with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).findById(id);
            verify(curvePointRepository, Mockito.times(0)).save(any(CurvePoint.class));
        }
    }

    @Nested
    @Tag("CurvePointServiceTests")
    @DisplayName("deleteCurvePoint tests:")
    class DeleteCurvePointTest {

        @DisplayName("GIVEN an existing curvePoint" +
                "WHEN the function deleteCurvePoint() is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void deleteCurvePointExistingTest() {
            //GIVEN
            //an existing curvePoint
            Integer id = 1;
            when(curvePointRepository.existsById(id)).thenReturn(true);
            doNothing().when(curvePointRepository).deleteById(id);
            //WHEN
            //the function deleteCurvePoint() is called
            curvePointService.deleteCurvePoint(id);
            //THEN
            //the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).existsById(id);
            verify(curvePointRepository, Mockito.times(1)).deleteById(id);
        }

        @DisplayName("GIVEN a non-existing curvePoint " +
                "WHEN the function deleteCurvePoint() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deleteCurvePointDTONonExistingTest() {
            //GIVEN
            //a non-existing curvePoint
            Integer id = 1;
            when(curvePointRepository.existsById(id)).thenReturn(false);
            //WHEN
            //the function deleteCurvePoint() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> curvePointService.deleteCurvePoint(id));
            assertEquals("The curvePoint with id number " + id + " was not found so it could not have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(curvePointRepository, Mockito.times(1)).existsById(id);
            verify(curvePointRepository, Mockito.times(0)).deleteById(anyInt());
        }
    }

}
