package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.dto.TradeDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
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

@Tag("tradeTests")
@SpringBootTest
public class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @MockBean
    private TradeRepository tradeRepository;


    @Nested
    @Tag("TradeServiceTests")
    @DisplayName("createTrade tests:")
    class CreateTradeTest {


        @DisplayName("GIVEN a tradeDTO containing all required information" +
                "WHEN the function createTrade() is called " +
                "THEN a trade is created with expected information.")
        @Test
        void createTradeTest() {
            //GIVEN
            //a tradeDTO containing all required information
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            TradeDTO tradeDTO = new TradeDTO(account, type, bidQuantity);
            Trade trade = new Trade(account, type, bidQuantity);
            final ArgumentCaptor<Trade> arg = ArgumentCaptor.forClass(Trade.class);
            when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
            //WHEN
            //the function createTrade() is called
            Trade result = tradeService.createTrade(tradeDTO);
            //THEN
            //a trade is created with expected information
            assertThat(result).isInstanceOf(Trade.class);
            //the expected methods have been called with expected arguments
            verify(tradeRepository).save(arg.capture());
            assertEquals(account, arg.getValue().getAccount());
            assertEquals(type, arg.getValue().getType());
            assertEquals(bidQuantity, arg.getValue().getBuyQuantity());
        }
    }


    @Nested
    @Tag("TradeServiceTests")
    @DisplayName("getAllTrade tests:")
    class GetAllTradeTest {

        @DisplayName("GIVEN no trades returned by tradeRepository " +
                "WHEN the function getAllTrade() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllTradeWhenEmptyTest() {
            //GIVEN
            //no trades returned by tradeRepository
            List<Trade> allTradeTest = new ArrayList<>();
            when(tradeRepository.findAll()).thenReturn(allTradeTest);
            //WHEN
            //the function getAllTrade() is called
            List<TradeDTO> result = tradeService.getAllTrade();
            //THEN
            //it returns an empty list
            assertThat(result).isEmpty();
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN trades returned by tradeRepository " +
                "WHEN the function getAllTrade() is called " +
                "THEN it returns the expected list.")
        @Test
        void getAllTradeWhenNotEmptyTest() {
            //GIVEN
            //trades returned by tradeRepository
            String account = "account";
            String type = "type";
            Double buyQuantity = 10.0;
            String account2 = "account2";
            String type2 = "type2";
            Double buyQuantity2 = 5.0;
            Trade trade = new Trade(account, type, buyQuantity);
            Trade trade2 = new Trade(account2, type2, buyQuantity2);
            List<Trade> allTradeTest = new ArrayList<>();
            allTradeTest.add(trade);
            allTradeTest.add(trade2);
            when(tradeRepository.findAll()).thenReturn(allTradeTest);
            //WHEN
            //the function getAllTrade() is called
            List<TradeDTO> result = tradeService.getAllTrade();
            //THEN
            //it returns the expected list
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getAccount()).isEqualTo(account);
            assertThat(result.get(0).getType()).isEqualTo(type);
            assertThat(result.get(0).getBuyQuantity()).isEqualTo(buyQuantity);
            assertThat(result.get(1).getAccount()).isEqualTo(account2);
            assertThat(result.get(1).getType()).isEqualTo(type2);
            assertThat(result.get(1).getBuyQuantity()).isEqualTo(buyQuantity2);
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).findAll();
        }
    }


    @Nested
    @Tag("TradeServiceTests")
    @DisplayName("getTradeDTO tests:")
    class GetTradeDTOTest {

        @DisplayName("GIVEN an existing trade " +
                "WHEN the function getTradeDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getTradeDTOExistingTest() {
            //GIVEN
            //an existing trade
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            Integer id = 1;
            Trade trade = new Trade(account, type, bidQuantity);
            trade.setTradeId(id);
            when(tradeRepository.findById(id)).thenReturn(Optional.of(trade));
            //WHEN
            //the function getTradeDTO() is called
            TradeDTO result = tradeService.getTradeDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(TradeDTO.class);
            assertThat(result.getAccount()).isEqualTo(account);
            assertThat(result.getType()).isEqualTo(type);
            assertThat(result.getBuyQuantity()).isEqualTo(bidQuantity);
            assertThat(result.getTradeId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).findById(id);
        }

        @DisplayName("GIVEN an existing trade with all attributes " +
                "WHEN the function getTradeDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getTradeDTOAllAttributesTest() {
            //GIVEN
            //an existing trade with all attributes
            String account = "account";
            String type = "type";
            Double buyQuantity = 10.0;
            Integer id = 1;
            Trade trade = new Trade(id,account, type, buyQuantity,5.0,10.0,5.0,"benchmark",Timestamp.valueOf(LocalDateTime.now()),"security","status","trader","book","creationName",Timestamp.valueOf(LocalDateTime.now()),"revisionName",Timestamp.valueOf(LocalDateTime.now()),"dealName","dealType","sourceListId","side");
            when(tradeRepository.findById(id)).thenReturn(Optional.of(trade));
            //WHEN
            //the function getTradeDTO() is called
            TradeDTO result = tradeService.getTradeDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(TradeDTO.class);
            assertThat(result.getAccount()).isEqualTo(account);
            assertThat(result.getType()).isEqualTo(type);
            assertThat(result.getBuyQuantity()).isEqualTo(buyQuantity);
            assertThat(result.getTradeId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).findById(id);
        }


        @DisplayName("GIVEN a non-existing trade " +
                "WHEN the function getTradeDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getTradeDTONonExistingTest() {
            //GIVEN
            //a non-existing trade
            Integer id = 1;
            when(tradeRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getTradeDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> tradeService.getTradeDTO(id));
            assertEquals("The trade with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).findById(id);
        }
    }


    @Nested
    @Tag("TradeServiceTests")
    @DisplayName("updateTradeDTO tests:")
    class UpdateTradeDTOTest {

        @DisplayName("GIVEN an existing trade and a TradeDTO containing all information" +
                "WHEN the function updateTradeDTO() is called " +
                "THEN it returns the Trade object with information updated and other information unchanged.")
        @Test
        void updateTradeDTOExistingTest() {
            //GIVEN
            //an existing trade and a TradeDTO containing all information
            String account = "account";
            String type = "type";
            Double buyQuantity = 10.0;
            String account2 = "account2";
            String type2 = "type2";
            Double buyQuantity2 = 5.0;
            Integer id = 1;
            Trade trade = new Trade(account, type, buyQuantity);
            TradeDTO tradeDTO = new TradeDTO(account2, type2, buyQuantity2);
            trade.setTradeId(id);
            trade.setSellQuantity(5.0);
            trade.setBuyPrice(10.0);
            trade.setSellPrice(5.0);
            trade.setBenchmark("benchmark");
            trade.setTradeDate(Timestamp.valueOf(LocalDateTime.now()));
            trade.setSecurity("security");
            trade.setStatus("status");
            trade.setTrader("trader");
            trade.setBook("book");
            trade.setCreationName("creationName");
            trade.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
            trade.setRevisionName("revisionName");
            trade.setRevisionDate(Timestamp.valueOf(LocalDateTime.now()));
            trade.setDealName("dealName");
            trade.setDealType("dealType");
            trade.setSourceListId("sourceListId");
            trade.setSide("side");
            final ArgumentCaptor<Trade> arg = ArgumentCaptor.forClass(Trade.class);
            when(tradeRepository.findById(id)).thenReturn(Optional.of(trade));
            when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
            //WHEN
            //the function updateTradeDTO() is called
            Trade result = tradeService.updateTrade(id, tradeDTO);
            //THEN
            // it returns the Trade object with information updated and other information unchanged
            assertThat(result).isInstanceOf(Trade.class);
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).findById(id);
            verify(tradeRepository).save(arg.capture());
            assertEquals(account2, arg.getValue().getAccount());
            assertEquals(type2, arg.getValue().getType());
            assertEquals(buyQuantity2, arg.getValue().getBuyQuantity());
            assertEquals(id, arg.getValue().getTradeId());
            assertEquals(trade.getBenchmark(), arg.getValue().getBenchmark());
            assertEquals(trade.getTradeDate(), arg.getValue().getTradeDate());
            assertEquals(trade.getSecurity(), arg.getValue().getSecurity());
            assertEquals(trade.getStatus(), arg.getValue().getStatus());
            assertEquals(trade.getTrader(), arg.getValue().getTrader());
            assertEquals(trade.getBook(), arg.getValue().getBook());
            assertEquals(trade.getCreationName(), arg.getValue().getCreationName());
            assertEquals(trade.getCreationDate(), arg.getValue().getCreationDate());
            assertEquals(trade.getRevisionName(), arg.getValue().getRevisionName());
            assertEquals(trade.getRevisionDate(), arg.getValue().getRevisionDate());
            assertEquals(trade.getDealName(), arg.getValue().getDealName());
            assertEquals(trade.getDealType(), arg.getValue().getDealType());
            assertEquals(trade.getSourceListId(), arg.getValue().getSourceListId());
            assertEquals(trade.getSide(), arg.getValue().getSide());
            assertEquals(trade.getBuyQuantity(), arg.getValue().getBuyQuantity());
            assertEquals(trade.getBuyPrice(), arg.getValue().getBuyPrice());
            assertEquals(trade.getSellPrice(), arg.getValue().getSellPrice());
        }

        @DisplayName("GIVEN a non-existing trade " +
                "WHEN the function updateTradeDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void updateTradeDTONonExistingTest() {
            //GIVEN
            //a non-existing trade
            Integer id = 1;
            String account = "account";
            String type = "type";
            Double bidQuantity = 10.0;
            TradeDTO tradeDTO = new TradeDTO(account, type, bidQuantity);
            when(tradeRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getTradeDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> tradeService.updateTrade(id, tradeDTO));
            assertEquals("The trade with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).findById(id);
            verify(tradeRepository, Mockito.times(0)).save(any(Trade.class));
        }
    }

    @Nested
    @Tag("TradeServiceTests")
    @DisplayName("deleteTrade tests:")
    class DeleteTradeTest {

        @DisplayName("GIVEN an existing trade" +
                "WHEN the function deleteTrade() is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void deleteTradeExistingTest() {
            //GIVEN
            //an existing trade
            Integer id = 1;
            when(tradeRepository.existsById(id)).thenReturn(true);
            doNothing().when(tradeRepository).deleteById(id);
            //WHEN
            //the function deleteTrade() is called
            tradeService.deleteTrade(id);
            //THEN
            //the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).existsById(id);
            verify(tradeRepository, Mockito.times(1)).deleteById(id);
        }

        @DisplayName("GIVEN a non-existing trade " +
                "WHEN the function deleteTrade() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deleteTradeDTONonExistingTest() {
            //GIVEN
            //a non-existing trade
            Integer id = 1;
            when(tradeRepository.existsById(id)).thenReturn(false);
            //WHEN
            //the function deleteTrade() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> tradeService.deleteTrade(id));
            assertEquals("The trade with id number " + id + " was not found so it could not have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(tradeRepository, Mockito.times(1)).existsById(id);
            verify(tradeRepository, Mockito.times(0)).deleteById(anyInt());
        }
    }

}
