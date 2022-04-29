package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.dto.BidListDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;

import java.util.List;

public interface BidListService {

    /**
     * Create a new BidList
     *
     * @param bidListDTO a BidListDTO object containing information to create a new bidList
     * @return the BidList object created
     */
    BidList createBidList(BidListDTO bidListDTO);

    /**
     * Get all BidLists
     *
     * @return a list of all the BidLists existing
     */
    List<BidListDTO> getAllBidList();

    /**
     * Get a BidList
     *
     * @param id the id of the BidList object researched
     * @return a BidListDTO object containing all information to show from the bidList researched
     */
    BidListDTO getBidListDTO(Integer id) throws ObjectNotFoundException;

    /**
     * Update a BidList
     *
     * @param id the id of the BidList to update
     * @param bidListDTO a bidListDTO object containing all information to update
     * @return the BidList object updated
     */
    BidList updateBidList(Integer id, BidListDTO bidListDTO) throws ObjectNotFoundException;

    /**
     * Delete a BidList from its id
     *
     * @param id the id of the BidList object to delete
     */
    void deleteBidList(Integer id) throws ObjectNotFoundException;

}