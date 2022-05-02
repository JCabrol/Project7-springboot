package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.dto.TradeDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.TradeService;
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

@Tag("tradeTests")
@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Nested
    @Tag("TradeControllerTests")
    @DisplayName("home tests:")
    class HomeTest {
        @DisplayName("GIVEN a list of trade returned by the service" +
                "WHEN the uri \"/trade/list\" is called " +
                "THEN the expected view is displayed and the model contains expected attribute.")
        @Test
        void homeTest() throws Exception {
            //GIVEN
            //a list of trade returned by the service
            List<TradeDTO> tradeDTOList = new ArrayList<>();
            when(tradeService.getAllTrade()).thenReturn(tradeDTOList);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/list");
            // WHEN
            //the uri "/trade/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed and the model contains expected attribute
                    .andExpect(status().isOk())
                    .andExpect(view().name("trade/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("tradeList", tradeDTOList));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(1)).getAllTrade();
        }
    }

    @Nested
    @Tag("TradeControllerTests")
    @DisplayName("add tests:")
    class AddTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/trade/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addTest() throws Exception {
            //GIVEN
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/add");
            // WHEN
            //the uri "/trade/add" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the status is "isOk" and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("trade/add"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("trade"));
        }
    }

    @Nested
    @Tag("TradeControllerTests")
    @DisplayName("validate tests:")
    class ValidateTest {

        @DisplayName("GIVEN a tradeDTO with all required information" +
                "WHEN the uri \"/trade/validate\" is called " +
                "THEN a trade with expected information is created and the page is correctly redirected.")
        @Test
        void validateTest() throws Exception {
            //GIVEN
            // a tradeDTO with all required information
            String account = "account";
            String type = "type";
            Double buyQuantity = 10.0;
            Trade trade = new Trade(account, type, buyQuantity);
            final ArgumentCaptor<TradeDTO> arg = ArgumentCaptor.forClass(TradeDTO.class);
            when(tradeService.createTrade(any(TradeDTO.class))).thenReturn(trade);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/validate")
                    .param("account", account)
                    .param("type", type)
                    .param("buyQuantity", String.valueOf(buyQuantity));
            // WHEN
            //the uri "/trade/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // a trade with expected information is created and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("trade"))
                    .andExpect(redirectedUrl("/trade/list"));
            //the expected methods have been called with expected arguments
            verify(tradeService).createTrade(arg.capture());
            assertEquals(account, arg.getValue().getAccount());
            assertEquals(type, arg.getValue().getType());
            assertEquals(buyQuantity, arg.getValue().getBuyQuantity());
        }

        @DisplayName("GIVEN a tradeDTO without required information" +
                "WHEN the uri \"/trade/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned.")
        @Test
        void validateMissingInformationTest() throws Exception {
            //GIVEN
            // a tradeDTO without required information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/validate");
            // WHEN
            //the uri "/trade/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/add"));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(0)).createTrade(any(TradeDTO.class));
        }

        @DisplayName("GIVEN a tradeDTO with information over authorized size" +
                "WHEN the uri \"/trade/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned.")
        @Test
        void validateTooLongInformationTest() throws Exception {
            //GIVEN
            // a tradeDTO with information over authorized size
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/validate")
                    .param("account", "account123456789012345678901234567890")
                    .param("type", "type123456789012345678901234567890");
            // WHEN
            //the uri "/trade/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/add"));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(0)).createTrade(any(TradeDTO.class));
        }
    }

    @Nested
    @Tag("TradeControllerTests")
    @DisplayName("showUpdateForm tests:")
    class ShowUpdateFormTest {

        @DisplayName("GIVEN an existing trade" +
                "WHEN the uri \"/trade/update/{id}\" is called with \"get\" request" +
                "THEN the corresponding tradeDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormTest() throws Exception {
            //GIVEN
            // an existing trade
            String account = "account";
            String type = "type";
            Double buyQuantity = 10.0;
            int id = 1;
            TradeDTO tradeDTO = new TradeDTO(account, type, buyQuantity, id);
            when(tradeService.getTradeDTO(id)).thenReturn(tradeDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/update/{id}", id);
            // WHEN
            //the uri "/trade/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding tradeDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("trade", tradeDTO))
                    .andExpect(view().name("trade/update"));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(1)).getTradeDTO(id);
        }

        @DisplayName("GIVEN a non-existing trade" +
                "WHEN the uri \"/trade/update/{id}\" is called with \"get\" request" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing trade
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(tradeService.getTradeDTO(id)).thenThrow(objectNotFoundException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/update/{id}", id);
            // WHEN
            //the uri "/trade/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(1)).getTradeDTO(id);
        }
    }

    @Nested
    @Tag("TradeControllerTests")
    @DisplayName("updateBid tests:")
    class UpdateBidTest {

        @DisplayName("GIVEN an existing trade and all required information to update" +
                "WHEN the uri \"/trade/update/{id}\" is called with \"post\" request" +
                "THEN there is no error, the expected information is sent and the page is correctly redirected.")
        @Test
        void updateBidTest() throws Exception {
            //GIVEN
            // an existing trade and all required information to update
            String account = "account";
            String type = "type";
            Double buyQuantity = 10.0;
            int id = 1;
            Trade trade = new Trade(account, type, buyQuantity);
            final ArgumentCaptor<TradeDTO> arg = ArgumentCaptor.forClass(TradeDTO.class);
            when(tradeService.updateTrade(eq(id), any(TradeDTO.class))).thenReturn(trade);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/update/{id}", id)
                    .param("account", account)
                    .param("type", type)
                    .param("buyQuantity", String.valueOf(buyQuantity));
            // WHEN
            //the uri "/trade/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the expected information is sent and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("trade"))
                    .andExpect(redirectedUrl("/trade/list"));
            //the expected methods have been called with expected arguments
            verify(tradeService).updateTrade(eq(id), arg.capture());
            assertEquals(account, arg.getValue().getAccount());
            assertEquals(type, arg.getValue().getType());
            assertEquals(buyQuantity, arg.getValue().getBuyQuantity());
        }

        @DisplayName("GIVEN missing information" +
                "WHEN the uri \"/trade/update/{id}\" is called with \"post\" request" +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateBidMissingInformationTest() throws Exception {
            //GIVEN
            // missing information
            int id = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/update/{id}", id);
            // WHEN
            //the uri "/trade/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "update" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/update"));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(0)).updateTrade(anyInt(), any(TradeDTO.class));
        }

        @DisplayName("GIVEN too long information" +
                "WHEN the uri \"/trade/update/{id}\" is called with \"post\" request" +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateBidTooLongInformationTest() throws Exception {
            //GIVEN
            // too long information
            int id = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/update/{id}", id)
                    .param("account", "account012345678901234567890123456789")
                    .param("type", "type012345678901234567890123456789");
            // WHEN
            //the uri "/trade/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "update" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/update"));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(0)).updateTrade(anyInt(), any(TradeDTO.class));
        }
    }

    @Nested
    @Tag("TradeControllerTests")
    @DisplayName("deleteBid tests:")
    class DeleteBidTest {

        @DisplayName("GIVEN an existing trade" +
                "WHEN the uri \"/trade/delete/{id}\" is called" +
                "THEN there is no error and the page is correctly redirected.")
        @Test
        void deleteBidTest() throws Exception {
            //GIVEN
            // an existing trade
            int id = 1;
            doNothing().when(tradeService).deleteTrade(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/delete/{id}", id);
            // WHEN
            //the uri "/trade/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/trade/list"));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(1)).deleteTrade(id);
        }

        @DisplayName("GIVEN a non-existing trade" +
                "WHEN the uri \"/trade/delete/{id}\" is called" +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing trade
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(tradeService).deleteTrade(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/delete/{id}", id);
            // WHEN
            //the uri "/trade/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(tradeService, Mockito.times(1)).deleteTrade(id);
        }
    }


}
