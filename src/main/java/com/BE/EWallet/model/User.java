package com.BE.EWallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
//@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "user_name", name = "ux_u_user_name")})
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @Column (name = "user_name")// Membuat nama kolom
    private String username;
    private String  password;
    private String ktp;
    private Boolean ban = false;

    private Long balance = 0L;
    private Long transactionLimit = 1000000L;
    private int MaxIncorrectPassword = 3;
    private int MaxBalance = 10000000;
    private int MaxTopup = 10000000;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
