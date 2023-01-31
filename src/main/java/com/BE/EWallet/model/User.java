package com.BE.EWallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private Long transactionLimit = 1_000_000L;
    private int passwordAttempt = 0;



    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

}
