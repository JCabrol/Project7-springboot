package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.dto.TradeDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    /**
     * Create a new Trade
     *
     * @param tradeDTO a TradeDTO object containing information to create a new trade
     * @return the Trade object created
     */
    @Override
    public Trade createTrade(TradeDTO tradeDTO) {
        log.debug("Function createTrade in TradeService begin.");
        Trade trade = new Trade(tradeDTO.getAccount(), tradeDTO.getType(), tradeDTO.getBuyQuantity());
        trade.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        trade = tradeRepository.save(trade);
        log.info("New trade with id number " + trade.getTradeId() + " has been created.");
        log.debug("Function createTrade in TradeService ends without exception.");
        return trade;
    }

    /**
     * Get all Trades
     *
     * @return a list of all the Trades existing
     */
    @Override
    public List<TradeDTO> getAllTrade() {
        log.debug("Function getAllTrade in TradeService begin.");
        List<TradeDTO> allTradeDTO = new ArrayList<>();
        List<Trade> allTrade = tradeRepository.findAll();
        if (!allTrade.isEmpty()) {
            allTrade.forEach(trade -> {
                TradeDTO tradeDTO = transformTradeToDTO(trade);
                allTradeDTO.add(tradeDTO);
            });
        }
        log.debug("Function getAllTrade in TradeService ends without exception.");
        return allTradeDTO;
    }

    /**
     * Get a Trade
     *
     * @param id the id of the Trade object researched
     * @return the Trade object researched
     */
    private Trade getTrade(Integer id) throws ObjectNotFoundException {
        log.debug("Function getTrade in TradeService begin.");
        Optional<Trade> tradeOptional = tradeRepository.findById(id);
        if (tradeOptional.isPresent()) {
            Trade trade = tradeOptional.get();
            log.debug("Function getTrade in TradeService ends without exception.");
            return trade;
        } else {
            throw new ObjectNotFoundException("The trade with id number " + id + " was not found.");
        }
    }

    /**
     * Get a Trade
     *
     * @param id the id of the Trade object researched
     * @return a TradeDTO object containing all information to show from the trade researched
     */
    @Override
    public TradeDTO getTradeDTO(Integer id) throws ObjectNotFoundException {
        log.debug("Function getTradeDTO in TradeService begin.");
        Trade trade = getTrade(id);
        TradeDTO tradeDTO = transformTradeToDTO(trade);
        log.debug("Function getTradeDTO in TradeService ends without exception.");
        return tradeDTO;
    }

    /**
     * Update a Trade
     *
     * @param id         the id of the Trade to update
     * @param tradeDTO a tradeDTO object containing all information to update
     * @return the Trade object updated
     */
    @Override
    public Trade updateTrade(Integer id, TradeDTO tradeDTO) throws ObjectNotFoundException {
        log.debug("Function updateTrade in TradeService begin.");
        Trade trade = getTrade(id);
        trade.setAccount(tradeDTO.getAccount());
        trade.setType(tradeDTO.getType());
        trade.setBuyQuantity(tradeDTO.getBuyQuantity());
        trade = tradeRepository.save(trade);
        log.info("Trade with id number " + trade.getTradeId() + " has been updated.");
        log.debug("Function updateTrade in TradeService ends without exception.");
        return trade;
    }

    /**
     * Delete a Trade from its id
     *
     * @param id the id of the Trade object to delete
     */
    @Override
    public void deleteTrade(Integer id) throws ObjectNotFoundException {
        log.debug("Function deleteTrade in TradeService begin.");
        if (tradeRepository.existsById(id)) {
            tradeRepository.deleteById(id);
            log.info("Trade with id number " + id + " has been deleted.");
            log.debug("Function deleteTrade in TradeService ends without exception.");
        } else {
            throw new ObjectNotFoundException("The trade with id number " + id + " was not found so it could not have been deleted");
        }
    }

    /**
     * Transform trade to TradeDTO object
     *
     * @param trade the Trade object to transform to TradeDTO object
     * @return a TradeDTO object containing all information to show from the Trade object
     */
    private TradeDTO transformTradeToDTO(Trade trade) {
        log.trace("Function transformTradeToDTO in TradeService begin.");
        TradeDTO tradeDTO = new TradeDTO(trade.getAccount(), trade.getType(), trade.getBuyQuantity(), trade.getTradeId());
        log.trace("Function transformTradeToDTO in TradeService ends without exception.");
        return tradeDTO;
    }
}
