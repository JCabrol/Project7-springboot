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
@Table(name = "Trade")
public class Trade {


    @Id
    @Column(name = "trade_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tradeId;


    @NotBlank(message = "Account is mandatory")
    @Size(max = 30, message = "Account shouldn't be over 30 characters")
    @Column(name = "account")
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Account shouldn't be over 30 characters")
    @Column(name = "type")
    private String type;

    @Column(name = "buy_quantity")
    private Double buyQuantity;

    @Column(name = "sell_quantity")
    private Double sellQuantity;

    @Column(name = "buy_price")
    private Double buyPrice;

    @Column(name = "sell_price")
    private Double sellPrice;

    @Column(name = "benchmark")
    @Size(max = 125, message = "Benchmark shouldn't be over 125 characters")
    private String benchmark;

    @Column(name = "trade_date")
    private Timestamp tradeDate;

    @Column(name = "security")
    @Size(max = 125, message = "Security shouldn't be over 125 characters")
    private String security;

    @Column(name = "status")
    @Size(max = 10, message = "Status shouldn't be over 10 characters")
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

    public Trade(String account, String type, Double buyQuantity) {
        this.account = account;
        this.type = type;
        this.buyQuantity = buyQuantity;
    }
}
