package com.nnk.springboot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidListDTO {

    @NotBlank(message = "Account is mandatory")
    @Size(max = 30, message = "Account should not be over 30 characters")
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Type should not be over 30 characters")
    private String type;

    private Double bidQuantity;

    private Integer bidListId;

    public BidListDTO(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
