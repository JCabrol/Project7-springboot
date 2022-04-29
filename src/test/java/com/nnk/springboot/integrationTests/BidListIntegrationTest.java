package com.nnk.springboot.integrationTests;

import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.BidListService;
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

@Tag("bidListTests")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = BEFORE_CLASS)
public class BidListIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BidListService bidListService;

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("BidListIntegrationTests")
    @DisplayName("homeIntegration tests:")
    class HomeIntegrationTest {
        @DisplayName("GIVEN a list of bidList registered in database " +
                "WHEN the uri \"/bidList/list\" is called " +
                "THEN the expected view is displayed with all bidList registered.")
        @Test
        void homeIntegrationTest() throws Exception {
            //GIVEN
            //a list of bidList registered in database
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
                    .andExpect(model().attributeExists("bidList"))
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

        @DisplayName("GIVEN an empty list of bidList " +
                "WHEN the uri \"/bidList/list\" is called " +
                "THEN the expected view is displayed with an empty list.")
        @Test
        void homeEmptyListIntegrationTest() throws Exception {
            //GIVEN
            //an empty list of bidList
            bidListService.deleteBidList(1);
            bidListService.deleteBidList(2);
            bidListService.deleteBidList(3);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/list");
            // WHEN
            //the uri "/bidList/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed with an empty list
                    .andExpect(status().isOk())
                    .andExpect(view().name("bidList/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("bidList", empty()));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("BidListIntegrationTests")
    @DisplayName("addIntegration tests:")
    class AddIntegrationTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/bidList/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @Test
        void addIntegrationTest() throws Exception {
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


    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("BidListIntegrationTests")
    @DisplayName("validateIntegration tests:")
    class ValidateIntegrationTest {

        @DisplayName("GIVEN a bidListDTO with all required information " +
                "WHEN the uri \"/bidList/validate\" is called " +
                "THEN a bidList with expected information is created and the page is correctly redirected.")
        @Test
        void validateIntegrationTest() throws Exception {
            //GIVEN
            // a bidListDTO with all required information
            String account = "account4";
            String type = "type4";
            Double bidQuantity = 40.0;
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
            assertEquals(4, bidListService.getAllBidList().size());
            assertEquals(account, bidListService.getAllBidList().get(3).getAccount());
            assertEquals(type, bidListService.getAllBidList().get(3).getType());
            assertEquals(bidQuantity, bidListService.getAllBidList().get(3).getBidQuantity());
        }

        @DisplayName("GIVEN a bidListDTO without required information " +
                "WHEN the uri \"/bidList/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned and no bidList have been created.")
        @Test
        void validateMissingInformationIntegrationTest() throws Exception {
            //GIVEN
            // a bidListDTO without required information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/validate");
            // WHEN
            //the uri "/bidList/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned and no bidList have been created
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/add"))
                    .andExpect(content().string(containsString("Account is mandatory")))
                    .andExpect(content().string(containsString("Type is mandatory")));
            assertEquals(3, bidListService.getAllBidList().size());
        }

        @DisplayName("GIVEN a bidListDTO with information over authorized size " +
                "WHEN the uri \"/bidList/validate\" is called " +
                "THEN there are 2 errors and the page \"add\" is returned and no bidList have been created.")
        @Test
        void validateTooLongInformationIntegrationTest() throws Exception {
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
                    // there are 2 errors and the page "add" is returned and no bidList have been created
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/add"))
                    .andExpect(content().string(containsString("Account should not be over 30 characters")))
                    .andExpect(content().string(containsString("Type should not be over 30 characters")));
            assertEquals(3, bidListService.getAllBidList().size());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("BidListIntegrationTests")
    @DisplayName("showUpdateFormIntegration tests:")
    class ShowUpdateFormIntegrationTest {

        @DisplayName("GIVEN an existing bidList " +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"get\" request " +
                "THEN the corresponding bidListDTO is attached to the model and the expected view is displayed.")
        @Test
        void showUpdateFormIntegrationTest() throws Exception {
            //GIVEN
            // an existing bidList
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/update/{id}", 1);
            // WHEN
            //the uri "/bidList/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding bidListDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(view().name("bidList/update"))
                    .andExpect(content().string(containsString("account1")))
                    .andExpect(content().string(containsString("type1")))
                    .andExpect(content().string(containsString(String.valueOf(10.0))));
        }

        @DisplayName("GIVEN a non-existing bidList " +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"get\" request " +
                "THEN an error page is displayed with expected error message.")
        @Test
        void showUpdateFormNonExistingIntegrationTest() throws Exception {
            //GIVEN
            // a non-existing bidList
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/update/{id}", 4);
            // WHEN
            //the uri "/bidList/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The bidList with id number 4 was not found.")));
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("BidListIntegrationTests")
    @DisplayName("updateBidIntegration tests:")
    class UpdateBidIntegrationTest {

        @DisplayName("GIVEN an existing bidList and all required information to update " +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"post\" request " +
                "THEN there is no error, the bidList is correctly modified and the page is correctly redirected.")
        @Test
        void updateBidIntegrationTest() throws Exception {
            //GIVEN
            // an existing bidList and all required information to update
            String newAccount = "newAccount";
            String newType = "newType";
            Double newBidQuantity = 5.5;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/update/{id}", 1)
                    .param("account", newAccount)
                    .param("type", newType)
                    .param("bidQuantity", String.valueOf(newBidQuantity));
            // WHEN
            //the uri "/bidList/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the bidList is correctly modified and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("bidList"))
                    .andExpect(redirectedUrl("/bidList/list"));
            assertEquals(3, bidListService.getAllBidList().size());
            assertEquals(newAccount, bidListService.getBidListDTO(1).getAccount());
            assertEquals(newType, bidListService.getBidListDTO(1).getType());
            assertEquals(newBidQuantity, bidListService.getBidListDTO(1).getBidQuantity());
        }

        @DisplayName("GIVEN missing information " +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"post\" request " +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @Test
        void updateBidMissingInformationTest() throws Exception {
            //GIVEN
            // missing information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/bidList/update/{id}", 1);
            // WHEN
            //the uri "/bidList/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "add" is returned and the bidList is not modified
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/update"))
                    .andExpect(content().string(containsString("Account is mandatory")))
                    .andExpect(content().string(containsString("Type is mandatory")));
            assertEquals(3, bidListService.getAllBidList().size());
            assertEquals("account1", bidListService.getBidListDTO(1).getAccount());
            assertEquals("type1", bidListService.getBidListDTO(1).getType());
            assertEquals(10.0, bidListService.getBidListDTO(1).getBidQuantity());
        }

        @DisplayName("GIVEN too long information " +
                "WHEN the uri \"/bidList/update/{id}\" is called with \"post\" request " +
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
                    // there are 2 errors and the page "add" is returned and the bidList is not modified
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("bidList"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("bidList", 2))
                    .andExpect(view().name("bidList/update"))
                    .andExpect(content().string(containsString("Account should not be over 30 characters")))
                    .andExpect(content().string(containsString("Type should not be over 30 characters")));
            assertEquals(3, bidListService.getAllBidList().size());
            assertEquals("account1", bidListService.getBidListDTO(1).getAccount());
            assertEquals("type1", bidListService.getBidListDTO(1).getType());
            assertEquals(10.0, bidListService.getBidListDTO(1).getBidQuantity());
        }
    }

    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    @Nested
    @Tag("BidListIntegrationTests")
    @DisplayName("deleteBidIntegration tests:")
    class DeleteBidIntegrationTest {

        @DisplayName("GIVEN an existing bidList " +
                "WHEN the uri \"/bidList/delete/{id}\" is called " +
                "THEN there is no error, the bidList doesn't exist anymore and the page is correctly redirected.")
        @Test
        void deleteBidIntegrationTest() throws Exception {
            //GIVEN
            // an existing bidList
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/delete/{id}", 1);
            // WHEN
            //the uri "/bidList/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the bidList doesn't exist anymore and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/bidList/list"));
            assertEquals(2, bidListService.getAllBidList().size());
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> bidListService.getBidListDTO(1));
            assertEquals("The bidList with id number 1 was not found.", exception.getMessage());
        }

        @DisplayName("GIVEN a non-existing bidList " +
                "WHEN the uri \"/bidList/delete/{id}\" is called " +
                "THEN an error page is displayed with expected error message and no bidList have been suppressed.")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing bidList
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/bidList/delete/{id}", 4);
            // WHEN
            //the uri "/bidList/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("The bidList with id number 4 was not found so it could not have been deleted")));
            assertEquals(3, bidListService.getAllBidList().size());
        }
    }
}
