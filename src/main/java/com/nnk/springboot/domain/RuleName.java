package com.nnk.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Rule_name")
public class RuleName {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    @Size(max = 125, message = "Name shouldn't be over 125 characters")
    private String name;

    @Column(name = "description")
    @Size(max = 125, message = "Description shouldn't be over 125 characters")
    private String description;

    @Column(name = "json")
    @Size(max = 125, message = "Json shouldn't be over 125 characters")
    private String json;

    @Column(name = "template")
    @Size(max = 512, message = "Template shouldn't be over 512 characters")
    private String template;

    @Column(name = "sql_str")
    @Size(max = 125, message = "Sql str shouldn't be over 125 characters")
    private String sqlStr;

    @Column(name = "sql_part")
    @Size(max = 125, message = "Sql part shouldn't be over 125 characters")
    private String sqlPart;

    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}
