package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.dto.TradeDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;

import java.util.List;

public interface TradeService {

    /**
     * Create a new Trade
     *
     * @param tradeDTO a TradeDTO object containing information to create a new trade
     * @return the Trade object created
     */
    Trade createTrade(TradeDTO tradeDTO);

    /**
     * Get all Trades
     *
     * @return a list of all the Trades existing
     */
    List<TradeDTO> getAllTrade();

    /**
     * Get a Trade
     *
     * @param id the id of the Trade object researched
     * @return a TradeDTO object containing all information to show from the trade researched
     */
    TradeDTO getTradeDTO(Integer id) throws ObjectNotFoundException;

    /**
     * Update a Trade
     *
     * @param id the id of the Trade to update
     * @param tradeDTO a tradeDTO object containing all information to update
     * @return the Trade object updated
     */
    Trade updateTrade(Integer id, TradeDTO tradeDTO) throws ObjectNotFoundException;

    /**
     * Delete a Trade from its id
     *
     * @param id the id of the Trade object to delete
     */
    void deleteTrade(Integer id) throws ObjectNotFoundException;

}