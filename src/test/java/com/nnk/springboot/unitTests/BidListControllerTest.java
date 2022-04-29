package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DTO.BidListDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.BidListService;
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

@Tag("bidListTests")
@SpringBootTest
@AutoConfigureMockMvc
public class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    @Nested
    @Tag("BidListControllerTests")
    @DisplayName("home tests:")
    class HomeTest {
        @DisplayName("GIVEN a list of bidList returned by the service" +
                "WHEN the uri \"/bidList/list\" is called " +
                "THEN the expected view is displayed and the model contains expected attribute.")
        @Test
        void homeTest() throws Exception {
            //GIVEN
            //a list of bidList returned by the service
            List<BidListDTO> bidListDTOList = new ArrayList<>();
            when(bidListService.getAllBidList()).thenReturn(bidListDTOList);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/list");
            // WHEN
            //the uri "/bidList/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed and the model contains expected attribute
                    .andExpect(status().isOk())
                    .andExpect(view().name("bidList/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("bidList", bidListDTOList));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(1)).getAllBidList();
        }
    }

    @Nested
    @Tag("BidListControllerTests")
    @DisplayName("add tests:")
    class AddTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/bidList/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addTest() throws Exception {
            //GIVEN
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/add");
            // WHEN
            //the uri "/bidList/add" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the status is "isOk" and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("bidList/add"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("bidList"));
        }
    }

    @Nested
    @Tag("BidListControllerTests")
    @DisplayName("validate tests:")
    class ValidateTest {

        @DisplayName("GIVEN a bidListDTO with all required information" +
                "WHEN the uri \"/bidList/validate\" is called " +
                "THEN a bidList with expected information is created and the page is correctly redirected.")
        @Test
        void validateTest() throws Exception {
            //GIVEN
            // a bidListDTO with all required information
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            BidList bidList = new BidList(account, type, bidQuantity);
            final ArgumentCaptor<BidListDTO> arg = ArgumentCaptor.forClass(BidListDTO.class);
            when(bidListService.createBidList(any(BidListDTO.class))).thenReturn(bidList);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/validate")
                    .param("account", account)
                    .param("type", type)
                    .param("bidQuantity", String.valueOf(bidQuantity));
            // WHEN
            //the uri "/bidList/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // a bidList with expected information is created and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("bidList"))
                    .andExpect(redirectedUrl("/bidList/list"));
            //the expected methods have been called with expected arguments
            verify(bidListService).createBidList(arg.capture());
            assertEquals(account, arg.getValue().getAccount());
            assertEquals(type, arg.getValue().getType());
            assertEquals(bidQuantity, arg.getValue().getBidQuantity());
        }

        @DisplayName("GIVEN a bidListDTO without required information" +
                "WHEN the uri \"/bidList/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned.")
        @Test
        void validateMissingInformationTest() throws Exception {
            //GIVEN
            // a bidListDTO without required information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/validate");
            // WHEN
            //the uri "/bidList/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/add"));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(0)).createBidList(any(BidListDTO.class));
        }

        @DisplayName("GIVEN a bidListDTO with information over authorized size" +
                "WHEN the uri \"/bidList/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned.")
        @Test
        void validateTooLongInformationTest() throws Exception {
            //GIVEN
            // a bidListDTO with information over authorized size
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/validate")
                    .param("account", "account123456789012345678901234567890")
                    .param("type", "type123456789012345678901234567890");
            // WHEN
            //the uri "/bidList/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/add"));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(0)).createBidList(any(BidListDTO.class));
        }
    }

    @Nested
    @Tag("BidListControllerTests")
    @DisplayName("showUpdateForm tests:")
    class ShowUpdateFormTest {

        @DisplayName("GIVEN an existing bidList" +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"get\" request" +
                "THEN the corresponding bidListDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormTest() throws Exception {
            //GIVEN
            // an existing bidList
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            int id = 1;
            BidListDTO bidListDTO = new BidListDTO(account, type, bidQuantity, id);
            when(bidListService.getBidListDTO(id)).thenReturn(bidListDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/update/{id}", id);
            // WHEN
            //the uri "/bidList/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding bidListDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("bidList", bidListDTO))
                    .andExpect(view().name("bidList/update"));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(1)).getBidListDTO(id);
        }

        @DisplayName("GIVEN a non-existing bidList" +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"get\" request" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing bidList
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(bidListService.getBidListDTO(id)).thenThrow(objectNotFoundException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/update/{id}", id);
            // WHEN
            //the uri "/bidList/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(1)).getBidListDTO(id);
        }
    }

    @Nested
    @Tag("BidListControllerTests")
    @DisplayName("updateBid tests:")
    class UpdateBidTest {

        @DisplayName("GIVEN an existing bidList and all required information to update" +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"post\" request" +
                "THEN there is no error, the expected information is sent and the page is correctly redirected.")
        @Test
        void updateBidTest() throws Exception {
            //GIVEN
            // an existing bidList and all required information to update
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            int id = 1;
            BidList bidList = new BidList(account, type, bidQuantity);
            final ArgumentCaptor<BidListDTO> arg = ArgumentCaptor.forClass(BidListDTO.class);
            when(bidListService.updateBidList(eq(id), any(BidListDTO.class))).thenReturn(bidList);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/update/{id}", id)
                    .param("account", account)
                    .param("type", type)
                    .param("bidQuantity", String.valueOf(bidQuantity));
            // WHEN
            //the uri "/bidList/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the expected information is sent and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("bidList"))
                    .andExpect(redirectedUrl("/bidList/list"));
            //the expected methods have been called with expected arguments
            verify(bidListService).updateBidList(eq(id), arg.capture());
            assertEquals(account, arg.getValue().getAccount());
            assertEquals(type, arg.getValue().getType());
            assertEquals(bidQuantity, arg.getValue().getBidQuantity());
        }

        @DisplayName("GIVEN missing information" +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"post\" request" +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateBidMissingInformationTest() throws Exception {
            //GIVEN
            // missing information
            int id = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/update/{id}", id);
            // WHEN
            //the uri "/bidList/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "update" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/update"));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(0)).updateBidList(anyInt(), any(BidListDTO.class));
        }

        @DisplayName("GIVEN too long information" +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"post\" request" +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateBidTooLongInformationTest() throws Exception {
            //GIVEN
            // too long information
            int id = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/update/{id}", id)
                    .param("account", "account012345678901234567890123456789")
                    .param("type", "type012345678901234567890123456789");
            // WHEN
            //the uri "/bidList/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "update" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/update"));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(0)).updateBidList(anyInt(), any(BidListDTO.class));
        }
    }

    @Nested
    @Tag("BidListControllerTests")
    @DisplayName("deleteBid tests:")
    class DeleteBidTest {

        @DisplayName("GIVEN an existing bidList" +
                "WHEN the uri \"/bidList/delete/{id}\" is called" +
                "THEN there is no error and the page is correctly redirected.")
        @Test
        void deleteBidTest() throws Exception {
            //GIVEN
            // an existing bidList
            int id = 1;
            doNothing().when(bidListService).deleteBidList(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/delete/{id}", id);
            // WHEN
            //the uri "/bidList/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/bidList/list"));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(1)).deleteBidList(id);
        }

        @DisplayName("GIVEN a non-existing bidList" +
                "WHEN the uri \"/bidList/delete/{id}\" is called" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing bidList
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(bidListService).deleteBidList(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/delete/{id}", id);
            // WHEN
            //the uri "/bidList/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(bidListService, Mockito.times(1)).deleteBidList(id);
        }
    }


}
