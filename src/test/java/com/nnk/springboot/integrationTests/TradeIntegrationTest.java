package com.nnk.springboot.integrationTests;

import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.TradeService;
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

@Tag("tradeTests")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = BEFORE_CLASS)
public class TradeIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TradeService tradeService;

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("TradeIntegrationTests")
    @DisplayName("homeIntegration tests:")
    class HomeIntegrationTest {
        @DisplayName("GIVEN a list of trade registered in database " +
                "WHEN the uri \"/trade/list\" is called " +
                "THEN the expected view is displayed with all trade registered.")
        @Test
        void homeIntegrationTest() throws Exception {
            //GIVEN
            //a list of trade registered in database
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
                    .andExpect(model().attributeExists("tradeList"))
                    .andExpect(content().string(containsString("account1")))
                    .andExpect(content().string(containsString("account2")))
                    .andExpect(content().string(containsString("account3")))
                    .andExpect(content().string(containsString("type1")))
                    .andExpect(content().string(containsString("type2")))
                    .andExpect(content().string(containsString("type3")))
                    .andExpect(content().string(containsString(String.valueOf(10.0))))
                    .andExpect(content().string(containsString(String.valueOf(20.0))))
                    .andExpect(content().string(containsString(String.valueOf(30.0))));
        }

        @DisplayName("GIVEN an empty list of trade " +
                "WHEN the uri \"/trade/list\" is called " +
                "THEN the expected view is displayed with an empty list.")
        @Test
        void homeEmptyListIntegrationTest() throws Exception {
            //GIVEN
            //an empty list of trade
            tradeService.deleteTrade(1);
            tradeService.deleteTrade(2);
            tradeService.deleteTrade(3);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/list");
            // WHEN
            //the uri "/trade/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed with an empty list
                    .andExpect(status().isOk())
                    .andExpect(view().name("trade/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("tradeList", empty()));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("TradeIntegrationTests")
    @DisplayName("addIntegration tests:")
    class AddIntegrationTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/trade/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addIntegrationTest() throws Exception {
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


    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("TradeIntegrationTests")
    @DisplayName("validateIntegration tests:")
    class ValidateIntegrationTest {

        @DisplayName("GIVEN a tradeDTO with all required information " +
                "WHEN the uri \"/trade/validate\" is called " +
                "THEN a trade with expected information is created and the page is correctly redirected.")
        @Test
        void validateIntegrationTest() throws Exception {
            //GIVEN
            // a tradeDTO with all required information
            String account = "account4";
            String type = "type4";
            Double buyQuantity = 40.0;
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
            assertEquals(4, tradeService.getAllTrade().size());
            assertEquals(account, tradeService.getAllTrade().get(3).getAccount());
            assertEquals(type, tradeService.getAllTrade().get(3).getType());
            assertEquals(buyQuantity, tradeService.getAllTrade().get(3).getBuyQuantity());
        }

        @DisplayName("GIVEN a tradeDTO without required information " +
                "WHEN the uri \"/trade/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned and no trade have been created.")
        @Test
        void validateMissingInformationIntegrationTest() throws Exception {
            //GIVEN
            // a tradeDTO without required information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/validate");
            // WHEN
            //the uri "/trade/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned and no trade have been created
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/add"))
                    .andExpect(content().string(containsString("Account is mandatory")))
                    .andExpect(content().string(containsString("Type is mandatory")));
            assertEquals(3, tradeService.getAllTrade().size());
        }

        @DisplayName("GIVEN a tradeDTO with information over authorized size " +
                "WHEN the uri \"/trade/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned and no trade have been created.")
        @Test
        void validateTooLongInformationIntegrationTest() throws Exception {
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
                    // there are 2 errors and the page "add" is returned and no trade have been created
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/add"))
                    .andExpect(content().string(containsString("Account should not be over 30 characters")))
                    .andExpect(content().string(containsString("Type should not be over 30 characters")));
            assertEquals(3, tradeService.getAllTrade().size());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("TradeIntegrationTests")
    @DisplayName("showUpdateFormIntegration tests:")
    class ShowUpdateFormIntegrationTest {

        @DisplayName("GIVEN an existing trade " +
                "WHEN the uri \"/trade/update/{id}\" is called with \"get\" request " +
                "THEN the corresponding tradeDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormIntegrationTest() throws Exception {
            //GIVEN
            // an existing trade
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/update/{id}", 1);
            // WHEN
            //the uri "/trade/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding tradeDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(view().name("trade/update"))
                    .andExpect(content().string(containsString("account1")))
                    .andExpect(content().string(containsString("type1")))
                    .andExpect(content().string(containsString(String.valueOf(10.0))));
        }

        @DisplayName("GIVEN a non-existing trade " +
                "WHEN the uri \"/trade/update/{id}\" is called with \"get\" request " +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingIntegrationTest() throws Exception {
            //GIVEN
            // a non-existing trade
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/update/{id}", 4);
            // WHEN
            //the uri "/trade/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The trade with id number 4 was not found.")));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("TradeIntegrationTests")
    @DisplayName("updateBidIntegration tests:")
    class UpdateBidIntegrationTest {

        @DisplayName("GIVEN an existing trade and all required information to update " +
                "WHEN the uri \"/trade/update/{id}\" is called with \"post\" request " +
                "THEN there is no error, the trade is correctly modified and the page is correctly redirected.")
        @Test
        void updateBidIntegrationTest() throws Exception {
            //GIVEN
            // an existing trade and all required information to update
            String newAccount = "newAccount";
            String newType = "newType";
            Double newBuyQuantity = 5.5;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/update/{id}", 1)
                    .param("account", newAccount)
                    .param("type", newType)
                    .param("buyQuantity", String.valueOf(newBuyQuantity));
            // WHEN
            //the uri "/trade/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the trade is correctly modified and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("trade"))
                    .andExpect(redirectedUrl("/trade/list"));
            assertEquals(3, tradeService.getAllTrade().size());
            assertEquals(newAccount, tradeService.getTradeDTO(1).getAccount());
            assertEquals(newType, tradeService.getTradeDTO(1).getType());
            assertEquals(newBuyQuantity, tradeService.getTradeDTO(1).getBuyQuantity());
        }

        @DisplayName("GIVEN missing information " +
                "WHEN the uri \"/trade/update/{id}\" is called with \"post\" request " +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateBidMissingInformationTest() throws Exception {
            //GIVEN
            // missing information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/trade/update/{id}", 1);
            // WHEN
            //the uri "/trade/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned and the trade is not modified
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/update"))
                    .andExpect(content().string(containsString("Account is mandatory")))
                    .andExpect(content().string(containsString("Type is mandatory")));
            assertEquals(3, tradeService.getAllTrade().size());
            assertEquals("account1", tradeService.getTradeDTO(1).getAccount());
            assertEquals("type1", tradeService.getTradeDTO(1).getType());
            assertEquals(10.0, tradeService.getTradeDTO(1).getBuyQuantity());
        }

        @DisplayName("GIVEN too long information " +
                "WHEN the uri \"/trade/update/{id}\" is called with \"post\" request " +
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
                    // there are 2 errors and the page "add" is returned and the trade is not modified
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("trade"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("trade", 2))
                    .andExpect(view().name("trade/update"))
                    .andExpect(content().string(containsString("Account should not be over 30 characters")))
                    .andExpect(content().string(containsString("Type should not be over 30 characters")));
            assertEquals(3, tradeService.getAllTrade().size());
            assertEquals("account1", tradeService.getTradeDTO(1).getAccount());
            assertEquals("type1", tradeService.getTradeDTO(1).getType());
            assertEquals(10.0, tradeService.getTradeDTO(1).getBuyQuantity());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("TradeIntegrationTests")
    @DisplayName("deleteBidIntegration tests:")
    class DeleteBidIntegrationTest {

        @DisplayName("GIVEN an existing trade " +
                "WHEN the uri \"/trade/delete/{id}\" is called " +
                "THEN there is no error, the trade doesn't exist anymore and the page is correctly redirected.")
        @Test
        void deleteBidIntegrationTest() throws Exception {
            //GIVEN
            // an existing trade
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/delete/{id}", 1);
            // WHEN
            //the uri "/trade/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the trade doesn't exist anymore and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/trade/list"));
            assertEquals(2, tradeService.getAllTrade().size());
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> tradeService.getTradeDTO(1));
            assertEquals("The trade with id number 1 was not found.", exception.getMessage());
        }

        @DisplayName("GIVEN a non-existing trade " +
                "WHEN the uri \"/trade/delete/{id}\" is called " +
                "THEN an error page is displayed with expected error message and no trade have been suppressed.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing trade
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/trade/delete/{id}", 4);
            // WHEN
            //the uri "/trade/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The trade with id number 4 was not found so it could not have been deleted")));
            assertEquals(3, tradeService.getAllTrade().size());
        }
    }
}
