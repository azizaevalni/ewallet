package com.BE.EWallet.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class TransferDTO {
    private String username;
   private String password;
    private String destinationUsername;
    private Integer amount;
}
