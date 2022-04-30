package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.dto.CurvePointDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.CurvePointService;
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

@Tag("curvePointTests")
@SpringBootTest
@AutoConfigureMockMvc
public class CurvePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    @Nested
    @Tag("CurvePointControllerTests")
    @DisplayName("home tests:")
    class HomeTest {
        @DisplayName("GIVEN a list of curvePoint returned by the service" +
                "WHEN the uri \"/curvePoint/list\" is called " +
                "THEN the expected view is displayed and the model contains expected attribute.")
        @Test
        void homeTest() throws Exception {
            //GIVEN
            //a list of curvePoint returned by the service
            List<CurvePointDTO> curvePointDTOList = new ArrayList<>();
            when(curvePointService.getAllCurvePoint()).thenReturn(curvePointDTOList);
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
                    .andExpect(model().attribute("curvePointList", curvePointDTOList));
            //the expected methods have been called with expected arguments
            verify(curvePointService, Mockito.times(1)).getAllCurvePoint();
        }
    }

    @Nested
    @Tag("CurvePointControllerTests")
    @DisplayName("add tests:")
    class AddTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/curvePoint/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addTest() throws Exception {
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

    @Nested
    @Tag("CurvePointControllerTests")
    @DisplayName("validate tests:")
    class ValidateTest {

        @DisplayName("GIVEN a curvePointDTO with all required information" +
                "WHEN the uri \"/curvePoint/validate\" is called " +
                "THEN a curvePoint with expected information is created and the page is correctly redirected.")
        @Test
        void validateTest() throws Exception {
            //GIVEN
            // a curvePointDTO with all required information
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            CurvePoint curvePoint = new CurvePoint(curveId, term, value);
            final ArgumentCaptor<CurvePointDTO> arg = ArgumentCaptor.forClass(CurvePointDTO.class);
            when(curvePointService.createCurvePoint(any(CurvePointDTO.class))).thenReturn(curvePoint);
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
            //the expected methods have been called with expected arguments
            verify(curvePointService).createCurvePoint(arg.capture());
            assertEquals(curveId, arg.getValue().getCurveId());
            assertEquals(term, arg.getValue().getTerm());
            assertEquals(value, arg.getValue().getValue());
        }

        @DisplayName("GIVEN a curvePointDTO without required information" +
                "WHEN the uri \"/curvePoint/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned.")
        @Test
        void validateMissingInformationTest() throws Exception {
            //GIVEN
            // a curvePointDTO without required information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/curvePoint/validate");
            // WHEN
            //the uri "/curvePoint/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("curvePoint"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("curvePoint", 1))
                    .andExpect(view().name("curvePoint/add"));
            //the expected methods have been called with expected arguments
            verify(curvePointService, Mockito.times(0)).createCurvePoint(any(CurvePointDTO.class));
        }
    }

    @Nested
    @Tag("CurvePointControllerTests")
    @DisplayName("showUpdateForm tests:")
    class ShowUpdateFormTest {

        @DisplayName("GIVEN an existing curvePoint" +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"get\" request" +
                "THEN the corresponding curvePointDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormTest() throws Exception {
            //GIVEN
            // an existing curvePoint
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            int id = 1;
            CurvePointDTO curvePointDTO = new CurvePointDTO(id,curveId, term, value);
            when(curvePointService.getCurvePointDTO(id)).thenReturn(curvePointDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/update/{id}", id);
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding curvePointDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("curvePoint", curvePointDTO))
                    .andExpect(view().name("curvePoint/update"));
            //the expected methods have been called with expected arguments
            verify(curvePointService, Mockito.times(1)).getCurvePointDTO(id);
        }

        @DisplayName("GIVEN a non-existing curvePoint" +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"get\" request" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing curvePoint
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(curvePointService.getCurvePointDTO(id)).thenThrow(objectNotFoundException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/update/{id}", id);
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(curvePointService, Mockito.times(1)).getCurvePointDTO(id);
        }
    }

    @Nested
    @Tag("CurvePointControllerTests")
    @DisplayName("updateCurvePoint tests:")
    class UpdateCurvePointTest {

        @DisplayName("GIVEN an existing curvePoint and all required information to update" +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"post\" request" +
                "THEN there is no error, the expected information is sent and the page is correctly redirected.")
        @Test
        void updateCurvePointTest() throws Exception {
            //GIVEN
            // an existing curvePoint and all required information to update
            Integer curveId = 1;
            Double term = 5.0;
            Double value = 10.5;
            int id = 1;
            CurvePoint curvePoint = new CurvePoint(curveId, term, value);
            final ArgumentCaptor<CurvePointDTO> arg = ArgumentCaptor.forClass(CurvePointDTO.class);
            when(curvePointService.updateCurvePoint(eq(id), any(CurvePointDTO.class))).thenReturn(curvePoint);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/curvePoint/update/{id}", id)
                    .param("curveId", String.valueOf(curveId))
                    .param("term", String.valueOf(term))
                    .param("value", String.valueOf(value));
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the expected information is sent and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("curvePoint"))
                    .andExpect(redirectedUrl("/curvePoint/list"));
            //the expected methods have been called with expected arguments
            verify(curvePointService).updateCurvePoint(eq(id), arg.capture());
            assertEquals(curveId, arg.getValue().getCurveId());
            assertEquals(term, arg.getValue().getTerm());
            assertEquals(value, arg.getValue().getValue());
        }

        @DisplayName("GIVEN missing information" +
                "WHEN the uri \"/curvePoint/update/{id}\" is called with \"post\" request" +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateCurvePointMissingInformationTest() throws Exception {
            //GIVEN
            // missing information
            int id = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/curvePoint/update/{id}", id);
            // WHEN
            //the uri "/curvePoint/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "update" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("curvePoint"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("curvePoint", 1))
                    .andExpect(view().name("curvePoint/update"));
            //the expected methods have been called with expected arguments
            verify(curvePointService, Mockito.times(0)).updateCurvePoint(anyInt(), any(CurvePointDTO.class));
        }
    }

    @Nested
    @Tag("CurvePointControllerTests")
    @DisplayName("deleteCurvePoint tests:")
    class DeleteCurvePointTest {

        @DisplayName("GIVEN an existing curvePoint" +
                "WHEN the uri \"/curvePoint/delete/{id}\" is called" +
                "THEN there is no error and the page is correctly redirected.")
        @Test
        void deleteCurvePointTest() throws Exception {
            //GIVEN
            // an existing curvePoint
            int id = 1;
            doNothing().when(curvePointService).deleteCurvePoint(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/delete/{id}", id);
            // WHEN
            //the uri "/curvePoint/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/curvePoint/list"));
            //the expected methods have been called with expected arguments
            verify(curvePointService, Mockito.times(1)).deleteCurvePoint(id);
        }

        @DisplayName("GIVEN a non-existing curvePoint" +
                "WHEN the uri \"/curvePoint/delete/{id}\" is called" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void deleteCurvePointNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing curvePoint
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(curvePointService).deleteCurvePoint(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/curvePoint/delete/{id}", id);
            // WHEN
            //the uri "/curvePoint/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(curvePointService, Mockito.times(1)).deleteCurvePoint(id);
        }
    }


}
