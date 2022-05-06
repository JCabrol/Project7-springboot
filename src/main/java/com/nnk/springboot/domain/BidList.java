package com.nnk.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bid_list")
public class BidList {

    @Id
    @Column(name = "bid_list_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bidListId;


    @NotBlank(message = "Account is mandatory")
    @Size(max = 30, message = "Account shouldn't be over 30 characters")
    @Column(name = "account")
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Account shouldn't be over 30 characters")
    @Column(name = "type")
    private String type;

    @Column(name = "bid_quantity")
    private Double bidQuantity;

    @Column(name = "ask_quantity")
    private Double askQuantity;

    @Column(name = "bid")
    private Double bid;

    @Column(name = "ask")
    private Double ask;

    @Column(name = "benchmark")
    @Size(max = 125, message = "Benchmark shouldn't be over 125 characters")
    private String benchmark;

    @Column(name = "bid_list_date")
    private Timestamp bidListDate;

    @Column(name = "commentary")
    @Size(max = 125, message = "Commentary shouldn't be over 125 characters")
    private String commentary;

    @Column(name = "security")
    @Size(max = 125, message = "Security shouldn't be over 125 characters")
    private String security;

    @Column(name = "status")
    @Size(max = 10, message = "Benchmark shouldn't be over 10 characters")
    private String status;

    @Column(name = "trader")
    @Size(max = 125, message = "Trader shouldn't be over 125 characters")
    private String trader;

    @Column(name = "book")
    @Size(max = 125, message = "Book shouldn't be over 125 characters")
    private String book;

    @Column(name = "creation_name")
    @Size(max = 125, message = "Creation name shouldn't be over 125 characters")
    private String creationName;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "revision_name")
    @Size(max = 125, message = "Revision name shouldn't be over 125 characters")
    private String revisionName;

    @Column(name = "revision_date")
    private Timestamp revisionDate;

    @Column(name = "deal_name")
    @Size(max = 125, message = "Deal name shouldn't be over 125 characters")
    private String dealName;

    @Column(name = "deal_type")
    @Size(max = 125, message = "Deal type shouldn't be over 125 characters")
    private String dealType;

    @Column(name = "source_list_id")
    @Size(max = 125, message = "Source list id shouldn't be over 125 characters")
    private String sourceListId;

    @Column(name = "side")
    @Size(max = 125, message = "Side shouldn't be over 125 characters")
    private String side;

    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
