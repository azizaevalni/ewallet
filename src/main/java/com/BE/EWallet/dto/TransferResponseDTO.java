package com.BE.EWallet.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponseDTO {
   private Long trx_id;
   private String originUsername;
   private String destinationUsername;
   private Integer amount;
   private String status;
}
