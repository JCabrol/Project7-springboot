package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.dto.CurvePointDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointServiceImpl implements CurvePointService {

    @Autowired
    private CurvePointRepository curvePointRepository;

    /**
     * Create a new CurvePoint
     *
     * @param curvePointDTO a CurvePointDTO object containing information to create a new curvePoint
     * @return the CurvePoint object created
     */
    @Override
    public CurvePoint createCurvePoint(CurvePointDTO curvePointDTO) {
        log.debug("Function createCurvePoint in CurvePointService begins.");
        CurvePoint curvePoint = new CurvePoint(curvePointDTO.getCurveId(), curvePointDTO.getTerm(), curvePointDTO.getValue());
        curvePoint.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        curvePoint = curvePointRepository.save(curvePoint);
        log.info("New curvePoint with id number " + curvePoint.getId() + " has been created.");
        log.debug("Function createCurvePoint in CurvePointService ends without exception.");
        return curvePoint;
    }

    /**
     * Get all CurvePoints
     *
     * @return a list of all the CurvePoints existing
     */
    @Override
    public List<CurvePointDTO> getAllCurvePoint() {
        log.debug("Function getAllCurvePoint in CurvePointService begins.");
        List<CurvePointDTO> allCurvePointDTO = new ArrayList<>();
        List<CurvePoint> allCurvePoint = curvePointRepository.findAll();
        if (!allCurvePoint.isEmpty()) {
            allCurvePoint.forEach(curvePoint -> {
                CurvePointDTO curvePointDTO = transformCurvePointToDTO(curvePoint);
                allCurvePointDTO.add(curvePointDTO);
            });
        }
        log.debug("Function getAllCurvePoint in CurvePointService ends without exception.");
        return allCurvePointDTO;
    }

    /**
     * Get a CurvePoint
     *
     * @param id the id of the CurvePoint object researched
     * @return the CurvePoint object researched
     */
    private CurvePoint getCurvePoint(Integer id) throws ObjectNotFoundException {
        log.debug("Function getCurvePoint in CurvePointService begins.");
        Optional<CurvePoint> curvePointOptional = curvePointRepository.findById(id);
        if (curvePointOptional.isPresent()) {
            CurvePoint curvePoint = curvePointOptional.get();
            log.debug("Function getCurvePoint in CurvePointService ends without exception.");
            return curvePoint;
        } else {
            throw new ObjectNotFoundException("The curvePoint with id number " + id + " was not found.");
        }
    }

    /**
     * Get a CurvePoint
     *
     * @param id the id of the CurvePoint object researched
     * @return a CurvePointDTO object containing all information to show from the curvePoint researched
     */
    @Override
    public CurvePointDTO getCurvePointDTO(Integer id) throws ObjectNotFoundException {
        log.debug("Function getCurvePointDTO in CurvePointService begins.");
        CurvePoint curvePoint = getCurvePoint(id);
        CurvePointDTO curvePointDTO = transformCurvePointToDTO(curvePoint);
        log.debug("Function getCurvePointDTO in CurvePointService ends without exception.");
        return curvePointDTO;
    }

    /**
     * Update a CurvePoint
     *
     * @param id the id of the CurvePoint to update
     * @param curvePointDTO a curvePointDTO object containing all information to update
     * @return the CurvePoint object updated
     */
    @Override
    public CurvePoint updateCurvePoint(Integer id, CurvePointDTO curvePointDTO) throws ObjectNotFoundException {
        log.debug("Function updateCurvePoint in CurvePointService begins.");
        CurvePoint curvePoint = getCurvePoint(id);
        curvePoint.setCurveId(curvePointDTO.getCurveId());
        curvePoint.setTerm(curvePointDTO.getTerm());
        curvePoint.setValue(curvePointDTO.getValue());
        curvePoint = curvePointRepository.save(curvePoint);
        log.info("CurvePoint with id number " + curvePoint.getId() + " has been updated.");
        log.debug("Function updateCurvePoint in CurvePointService ends without exception.");
        return curvePoint;
    }

    /**
     * Delete a CurvePoint from its id
     *
     * @param id the id of the CurvePoint object to delete
     */
    @Override
    public void deleteCurvePoint(Integer id) throws ObjectNotFoundException {
        log.debug("Function deleteCurvePoint in CurvePointService begins.");
        if (curvePointRepository.existsById(id)) {
            curvePointRepository.deleteById(id);
            log.info("CurvePoint with id number " + id + " has been deleted.");
            log.debug("Function deleteCurvePoint in CurvePointService ends without exception.");
        } else {
            throw new ObjectNotFoundException("The curvePoint with id number " + id + " was not found so it could not have been deleted");
        }
    }

    /**
     * Transform curvePoint to CurvePointDTO object
     *
     * @param curvePoint the CurvePoint object to transform to CurvePointDTO object
     * @return a CurvePointDTO object containing all information to show from the CurvePoint object
     */
    private CurvePointDTO transformCurvePointToDTO(CurvePoint curvePoint) {
        log.trace("Function transformCurvePointToDTO in CurvePointService begins.");
        CurvePointDTO curvePointDTO = new CurvePointDTO(curvePoint.getId(), curvePoint.getCurveId(), curvePoint.getTerm(), curvePoint.getValue());
        log.trace("Function transformCurvePointToDTO in CurvePointService ends without exception.");
        return curvePointDTO;
    }
}
