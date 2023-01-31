package com.BE.EWallet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransferDTO {
    private String username;
   private String password;
    private String destinationUsername;
    private Integer amount;
}
