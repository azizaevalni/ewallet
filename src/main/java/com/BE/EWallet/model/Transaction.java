package com.BE.EWallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trx_id;
    private String username;
    private Integer amount;
    private  Long balance_before;
    private Long balance_after;
    private String Type;
    private String status;
    private LocalDate date;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
