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
public class RuleNameDTO {


    private Integer id;

    @Size(max = 125, message = "Name shouldn't be over 125 characters")
    private String name;

    @Size(max = 125, message = "Description shouldn't be over 125 characters")
    private String description;

    @Size(max = 125, message = "Json shouldn't be over 125 characters")
    private String json;

    @Size(max = 512, message = "Template shouldn't be over 512 characters")
    private String template;

    @Size(max = 125, message = "Sql str shouldn't be over 125 characters")
    private String sqlStr;

    @Size(max = 125, message = "Sql part shouldn't be over 125 characters")
    private String sqlPart;

    public RuleNameDTO(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}
