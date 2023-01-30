package com.BE.EWallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionDTO {
    private String username;
    private String password;
    private Long amount;
}
