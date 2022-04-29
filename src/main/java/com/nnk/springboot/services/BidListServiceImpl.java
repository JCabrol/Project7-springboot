package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.dto.BidListDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BidListServiceImpl implements BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    /**
     * Create a new BidList
     *
     * @param bidListDTO a BidListDTO object containing information to create a new bidList
     * @return the BidList object created
     */
    @Override
    public BidList createBidList(BidListDTO bidListDTO) {
        log.debug("Function createBidList in BidListService begin.");
        BidList bidList = new BidList(bidListDTO.getAccount(), bidListDTO.getType(), bidListDTO.getBidQuantity());
        bidList = bidListRepository.save(bidList);
        log.info("New bidList with id number " + bidList.getBidListId() + " has been created.");
        log.debug("Function createBidList in BidListService ends without exception.");
        return bidList;
    }

    /**
     * Get all BidLists
     *
     * @return a list of all the BidLists existing
     */
    @Override
    public List<BidListDTO> getAllBidList() {
        log.debug("Function getAllBidList in BidListService begin.");
        List<BidListDTO> allBidListDTO = new ArrayList<>();
        List<BidList> allBidList = bidListRepository.findAll();
        if (!allBidList.isEmpty()) {
            allBidList.forEach(bidList -> {
                BidListDTO bidListDTO = transformBidListToDTO(bidList);
                allBidListDTO.add(bidListDTO);
            });
        }
        log.debug("Function getAllBidList in BidListService ends without exception.");
        return allBidListDTO;
    }

    /**
     * Get a BidList
     *
     * @param id the id of the BidList object researched
     * @return the BidList object researched
     */
    private BidList getBidList(Integer id) throws ObjectNotFoundException {
        log.debug("Function getBidList in BidListService begin.");
        Optional<BidList> bidListOptional = bidListRepository.findById(id);
        if (bidListOptional.isPresent()) {
            BidList bidList = bidListOptional.get();
            log.debug("Function getBidList in BidListService ends without exception.");
            return bidList;
        } else {
            throw new ObjectNotFoundException("The bidList with id number " + id + " was not found.");
        }
    }

    /**
     * Get a BidList
     *
     * @param id the id of the BidList object researched
     * @return a BidListDTO object containing all information to show from the bidList researched
     */
    @Override
    public BidListDTO getBidListDTO(Integer id) throws ObjectNotFoundException {
        log.debug("Function getBidListDTO in BidListService begin.");
        BidList bidList = getBidList(id);
        BidListDTO bidListDTO = transformBidListToDTO(bidList);
        log.debug("Function getBidListDTO in BidListService ends without exception.");
        return bidListDTO;
    }

    /**
     * Update a BidList
     *
     * @param id         the id of the BidList to update
     * @param bidListDTO a bidListDTO object containing all information to update
     * @return the BidList object updated
     */
    @Override
    public BidList updateBidList(Integer id, BidListDTO bidListDTO) throws ObjectNotFoundException {
        log.debug("Function updateBidList in BidListService begin.");
        BidList bidList = getBidList(id);
        bidList.setAccount(bidListDTO.getAccount());
        bidList.setType(bidListDTO.getType());
        bidList.setBidQuantity(bidListDTO.getBidQuantity());
        bidList = bidListRepository.save(bidList);
        log.info("BidList with id number " + bidList.getBidListId() + " has been updated.");
        log.debug("Function updateBidList in BidListService ends without exception.");
        return bidList;
    }

    /**
     * Delete a BidList from its id
     *
     * @param id the id of the BidList object to delete
     */
    @Override
    public void deleteBidList(Integer id) throws ObjectNotFoundException {
        log.debug("Function deleteBidList in BidListService begin.");
        if (bidListRepository.existsById(id)) {
            bidListRepository.deleteById(id);
            log.info("BidList with id number " + id + " has been deleted.");
            log.debug("Function deleteBidList in BidListService ends without exception.");
        } else {
            throw new ObjectNotFoundException("The bidList with id number " + id + " was not found so it could not have been deleted");
        }
    }

    /**
     * Transform bidList to BidListDTO object
     *
     * @param bidList the BidList object to transform to BidListDTO object
     * @return a BidListDTO object containing all information to show from the BidList object
     */
    private BidListDTO transformBidListToDTO(BidList bidList) {
        log.trace("Function transformBidListToDTO in BidListService begin.");
        BidListDTO bidListDTO = new BidListDTO(bidList.getAccount(), bidList.getType(), bidList.getBidQuantity(), bidList.getBidListId());
        log.trace("Function transformBidListToDTO in BidListService ends without exception.");
        return bidListDTO;
    }
}
