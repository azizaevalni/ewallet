package com.BE.EWallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReportResponseDTO {
    private List<ReportDTO> reportBalanceChangeInPercentage;
}

