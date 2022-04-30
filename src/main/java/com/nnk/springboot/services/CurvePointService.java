package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.dto.CurvePointDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;

import java.util.List;

public interface CurvePointService {

    /**
     * Create a new CurvePoint
     *
     * @param curvePointDTO a CurvePointDTO object containing information to create a new curvePoint
     * @return the CurvePoint object created
     */
    CurvePoint createCurvePoint(CurvePointDTO curvePointDTO);

    /**
     * Get all CurvePoints
     *
     * @return a list of all the CurvePoints existing
     */
    List<CurvePointDTO> getAllCurvePoint();

    /**
     * Get a CurvePoint
     *
     * @param id the id of the CurvePoint object researched
     * @return a CurvePointDTO object containing all information to show from the curvePoint researched
     */
    CurvePointDTO getCurvePointDTO(Integer id) throws ObjectNotFoundException;

    /**
     * Update a CurvePoint
     *
     * @param id            the id of the CurvePoint to update
     * @param curvePointDTO a curvePointDTO object containing all information to update
     * @return the CurvePoint object updated
     */
    CurvePoint updateCurvePoint(Integer id, CurvePointDTO curvePointDTO) throws ObjectNotFoundException;

    /**
     * Delete a CurvePoint from its id
     *
     * @param id the id of the CurvePoint object to delete
     */
    void deleteCurvePoint(Integer id) throws ObjectNotFoundException;

}