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
@Table(name = "Rating")
public class Rating {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "moodys_rating")
    @Size(max = 125, message = "Moodys rating shouldn't be over 125 characters")
    private String moodysRating;

    @Column(name = "sand_p_rating")
    @Size(max = 125, message = "Sand p rating shouldn't be over 125 characters")
    private String sandPRating;

    @Column(name = "fitch_rating")
    @Size(max = 125, message = "Fitch rating shouldn't be over 125 characters")
    private String fitchRating;

    @Column(name = "order_number")
    private Integer orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
