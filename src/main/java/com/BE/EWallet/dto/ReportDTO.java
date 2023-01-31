package com.BE.EWallet.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportDTO {
    private String username;
    private String changeInPercentage;
    private String balanceChangeDate;

}
