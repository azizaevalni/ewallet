package com.BE.EWallet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransferResponseDTO {
   private Integer trx_id;
   private String originUsername;
   private String destinationUsername;
   private Integer amount;
   private String status;
}
