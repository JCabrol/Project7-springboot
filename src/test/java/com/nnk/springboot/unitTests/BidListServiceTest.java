package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DTO.BidListDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("bidListTests")
@SpringBootTest
public class BidListServiceTest {

    @Autowired
    private BidListService bidListService;

    @MockBean
    private BidListRepository bidListRepository;


    @Nested
    @Tag("BidListServiceTests")
    @DisplayName("createBidList tests:")
    class CreateBidListTest {


        @DisplayName("GIVEN a bidListDTO containing all required information" +
                "WHEN the function createBidList() is called " +
                "THEN a bidList is created with expected information.")
        @Test
        void createBidListTest() {
            //GIVEN
            //a bidListDTO containing all required information
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            BidListDTO bidListDTO = new BidListDTO(account, type, bidQuantity);
            BidList bidList = new BidList(account, type, bidQuantity);
            final ArgumentCaptor<BidList> arg = ArgumentCaptor.forClass(BidList.class);
            when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
            //WHEN
            //the function createBidList() is called
            BidList result = bidListService.createBidList(bidListDTO);
            //THEN
            //a bidList is created with expected information
            assertThat(result).isInstanceOf(BidList.class);
            //the expected methods have been called with expected arguments
            verify(bidListRepository).save(arg.capture());
            assertEquals(account, arg.getValue().getAccount());
            assertEquals(type, arg.getValue().getType());
            assertEquals(bidQuantity, arg.getValue().getBidQuantity());
        }
    }


    @Nested
    @Tag("BidListServiceTests")
    @DisplayName("getAllBidList tests:")
    class GetAllBidListTest {

        @DisplayName("GIVEN no bidLists returned by bidListRepository " +
                "WHEN the function getAllBidList() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllBidListWhenEmptyTest() {
            //GIVEN
            //no bidLists returned by bidListRepository
            List<BidList> allBidListTest = new ArrayList<>();
            when(bidListRepository.findAll()).thenReturn(allBidListTest);
            //WHEN
            //the function getAllBidList() is called
            List<BidListDTO> result = bidListService.getAllBidList();
            //THEN
            //it returns an empty list
            assertThat(result).isEmpty();
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN bidLists returned by bidListRepository " +
                "WHEN the function getAllBidList() is called " +
                "THEN it returns the expected list.")
        @Test
        void getAllBidListWhenNotEmptyTest() {
            //GIVEN
            //bidLists returned by bidListRepository
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            String account2 = "account2";
            String type2 = "type2";
            Double bidQuantity2 = 5.0;
            BidList bidList = new BidList(account, type, bidQuantity);
            BidList bidList2 = new BidList(account2, type2, bidQuantity2);
            List<BidList> allBidListTest = new ArrayList<>();
            allBidListTest.add(bidList);
            allBidListTest.add(bidList2);
            when(bidListRepository.findAll()).thenReturn(allBidListTest);
            //WHEN
            //the function getAllBidList() is called
            List<BidListDTO> result = bidListService.getAllBidList();
            //THEN
            //it returns the expected list
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getAccount()).isEqualTo(account);
            assertThat(result.get(0).getType()).isEqualTo(type);
            assertThat(result.get(0).getBidQuantity()).isEqualTo(bidQuantity);
            assertThat(result.get(1).getAccount()).isEqualTo(account2);
            assertThat(result.get(1).getType()).isEqualTo(type2);
            assertThat(result.get(1).getBidQuantity()).isEqualTo(bidQuantity2);
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).findAll();
        }
    }


    @Nested
    @Tag("BidListServiceTests")
    @DisplayName("getBidListDTO tests:")
    class GetBidListDTOTest {

        @DisplayName("GIVEN an existing bidList " +
                "WHEN the function getBidListDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getBidListDTOExistingTest() {
            //GIVEN
            //an existing bidList
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            Integer id = 1;
            BidList bidList = new BidList(account, type, bidQuantity);
            bidList.setBidListId(id);
            when(bidListRepository.findById(id)).thenReturn(Optional.of(bidList));
            //WHEN
            //the function getBidListDTO() is called
            BidListDTO result = bidListService.getBidListDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(BidListDTO.class);
            assertThat(result.getAccount()).isEqualTo(account);
            assertThat(result.getType()).isEqualTo(type);
            assertThat(result.getBidQuantity()).isEqualTo(bidQuantity);
            assertThat(result.getBidListId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).findById(id);
        }

        @DisplayName("GIVEN an existing bidList with all attributes " +
                "WHEN the function getBidListDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getBidListDTOAllAttributesTest() {
            //GIVEN
            //an existing bidList with all attributes
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            Integer id = 1;
            BidList bidList = new BidList(id,account, type, bidQuantity,5.0,10.0,5.0,"benchmark",Timestamp.valueOf(LocalDateTime.now()),"commentary","security","status","trader","book","creationName",Timestamp.valueOf(LocalDateTime.now()),"revisionName",Timestamp.valueOf(LocalDateTime.now()),"dealName","dealType","sourceListId","side");
            when(bidListRepository.findById(id)).thenReturn(Optional.of(bidList));
            //WHEN
            //the function getBidListDTO() is called
            BidListDTO result = bidListService.getBidListDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(BidListDTO.class);
            assertThat(result.getAccount()).isEqualTo(account);
            assertThat(result.getType()).isEqualTo(type);
            assertThat(result.getBidQuantity()).isEqualTo(bidQuantity);
            assertThat(result.getBidListId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).findById(id);
        }


        @DisplayName("GIVEN a non-existing bidList " +
                "WHEN the function getBidListDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getBidListDTONonExistingTest() {
            //GIVEN
            //a non-existing bidList
            Integer id = 1;
            when(bidListRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getBidListDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> bidListService.getBidListDTO(id));
            assertEquals("The bidList with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).findById(id);
        }
    }


    @Nested
    @Tag("BidListServiceTests")
    @DisplayName("updateBidListDTO tests:")
    class UpdateBidListDTOTest {

        @DisplayName("GIVEN an existing bidList and a BidListDTO containing all information" +
                "WHEN the function updateBidListDTO() is called " +
                "THEN it returns the BidList object with information updated and other information unchanged.")
        @Test
        void updateBidListDTOExistingTest() {
            //GIVEN
            //an existing bidList and a BidListDTO containing all information
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            String account2 = "account2";
            String type2 = "type2";
            Double bidQuantity2 = 5.0;
            Integer id = 1;
            BidList bidList = new BidList(account, type, bidQuantity);
            BidListDTO bidListDTO = new BidListDTO(account2, type2, bidQuantity2);
            bidList.setBidListId(id);
            bidList.setAskQuantity(5.0);
            bidList.setBid(10.0);
            bidList.setAsk(5.0);
            bidList.setBenchmark("benchmark");
            bidList.setBidListDate(Timestamp.valueOf(LocalDateTime.now()));
            bidList.setCommentary("commentary");
            bidList.setSecurity("security");
            bidList.setStatus("status");
            bidList.setTrader("trader");
            bidList.setBook("book");
            bidList.setCreationName("creationName");
            bidList.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
            bidList.setRevisionName("revisionName");
            bidList.setRevisionDate(Timestamp.valueOf(LocalDateTime.now()));
            bidList.setDealName("dealName");
            bidList.setDealType("dealType");
            bidList.setSourceListId("sourceListId");
            bidList.setSide("side");
            final ArgumentCaptor<BidList> arg = ArgumentCaptor.forClass(BidList.class);
            when(bidListRepository.findById(id)).thenReturn(Optional.of(bidList));
            when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
            //WHEN
            //the function updateBidListDTO() is called
            BidList result = bidListService.updateBidList(id, bidListDTO);
            //THEN
            // it returns the BidList object with information updated and other information unchanged
            assertThat(result).isInstanceOf(BidList.class);
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).findById(id);
            verify(bidListRepository).save(arg.capture());
            assertEquals(account2, arg.getValue().getAccount());
            assertEquals(type2, arg.getValue().getType());
            assertEquals(bidQuantity2, arg.getValue().getBidQuantity());
            assertEquals(id, arg.getValue().getBidListId());
            assertEquals(bidList.getBenchmark(), arg.getValue().getBenchmark());
            assertEquals(bidList.getBidListDate(), arg.getValue().getBidListDate());
            assertEquals(bidList.getCommentary(), arg.getValue().getCommentary());
            assertEquals(bidList.getSecurity(), arg.getValue().getSecurity());
            assertEquals(bidList.getStatus(), arg.getValue().getStatus());
            assertEquals(bidList.getTrader(), arg.getValue().getTrader());
            assertEquals(bidList.getBook(), arg.getValue().getBook());
            assertEquals(bidList.getCreationName(), arg.getValue().getCreationName());
            assertEquals(bidList.getCreationDate(), arg.getValue().getCreationDate());
            assertEquals(bidList.getRevisionName(), arg.getValue().getRevisionName());
            assertEquals(bidList.getRevisionDate(), arg.getValue().getRevisionDate());
            assertEquals(bidList.getDealName(), arg.getValue().getDealName());
            assertEquals(bidList.getDealType(), arg.getValue().getDealType());
            assertEquals(bidList.getSourceListId(), arg.getValue().getSourceListId());
            assertEquals(bidList.getSide(), arg.getValue().getSide());
            assertEquals(bidList.getAskQuantity(), arg.getValue().getAskQuantity());
            assertEquals(bidList.getAsk(), arg.getValue().getAsk());
            assertEquals(bidList.getBid(), arg.getValue().getBid());
        }

        @DisplayName("GIVEN a non-existing bidList " +
                "WHEN the function updateBidListDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void updateBidListDTONonExistingTest() {
            //GIVEN
            //a non-existing bidList
            Integer id = 1;
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            BidListDTO bidListDTO = new BidListDTO(account, type, bidQuantity);
            when(bidListRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getBidListDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> bidListService.updateBidList(id, bidListDTO));
            assertEquals("The bidList with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).findById(id);
            verify(bidListRepository, Mockito.times(0)).save(any(BidList.class));
        }
    }

    @Nested
    @Tag("BidListServiceTests")
    @DisplayName("deleteBidList tests:")
    class DeleteBidListTest {

        @DisplayName("GIVEN an existing bidList" +
                "WHEN the function deleteBidList() is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void deleteBidListExistingTest() {
            //GIVEN
            //an existing bidList
            Integer id = 1;
            when(bidListRepository.existsById(id)).thenReturn(true);
            doNothing().when(bidListRepository).deleteById(id);
            //WHEN
            //the function deleteBidList() is called
            bidListService.deleteBidList(id);
            //THEN
            //the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).existsById(id);
            verify(bidListRepository, Mockito.times(1)).deleteById(id);
        }

        @DisplayName("GIVEN a non-existing bidList " +
                "WHEN the function deleteBidList() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deleteBidListDTONonExistingTest() {
            //GIVEN
            //a non-existing bidList
            Integer id = 1;
            when(bidListRepository.existsById(id)).thenReturn(false);
            //WHEN
            //the function deleteBidList() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> bidListService.deleteBidList(id));
            assertEquals("The bidList with id number " + id + " was not found so it could not have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(bidListRepository, Mockito.times(1)).existsById(id);
            verify(bidListRepository, Mockito.times(0)).deleteById(anyInt());
        }
    }

}
