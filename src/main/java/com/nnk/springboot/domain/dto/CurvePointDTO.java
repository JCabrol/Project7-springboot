package com.nnk.springboot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurvePointDTO {

    private Integer id;

    @NotNull(message = "Must not be null")
    private Integer curveId;

    private Double term;

    private Double value;

    public CurvePointDTO(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}
