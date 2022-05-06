package com.nnk.springboot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {


    private Integer id;

    @Size(max = 125, message = "Moodys rating shouldn't be over 125 characters")
    private String moodysRating;

    @Size(max = 125, message = "Sand p rating shouldn't be over 125 characters")
    private String sandPRating;

    @Size(max = 125, message = "Fitch rating shouldn't be over 125 characters")
    private String fitchRating;

    private Integer orderNumber;

    public RatingDTO(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
