package com.BE.EWallet.service;

import com.BE.EWallet.dto.ReportDTO;
import com.BE.EWallet.model.Transaction;
import com.BE.EWallet.model.User;
import com.BE.EWallet.repository.TransactionRepo;
import com.BE.EWallet.repository.UserRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class ReportService {
    @Autowired
    UserRepo userRepo;

    public List<ReportDTO> getreport (LocalDate date){
        String balanceChangeDate = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        List <User> list = userRepo.findAll();
        List <ReportDTO> reportDTOList = new ArrayList<>();

        list.forEach(user -> {
            ReportDTO reportDTO = new ReportDTO();
            List<Transaction> transactionList = user.getTransactions().stream().filter(transaction -> transaction.getDate().equals(date)).toList();

            if (!transactionList.isEmpty()){
                Long firstTransaction = transactionList.get(0).getBalance_before();
                Long lastTransaction = transactionList.get(transactionList.size()-1).getBalance_after();
                reportDTO.setUsername(user.getUsername());
                if (firstTransaction == 0){
                    reportDTO.setBalanceChangeDate(balanceChangeDate);
                    reportDTO.setChangeInPercentage("-");
                }else {
                    double changeInPercentage = 1.0 * (lastTransaction - firstTransaction) / firstTransaction *100;
                    reportDTO.setChangeInPercentage(changeInPercentage+"%");
                    reportDTO.setBalanceChangeDate(balanceChangeDate);
                }

            } else {
                reportDTO.setUsername(user.getUsername());
                reportDTO.setChangeInPercentage("0%");
                reportDTO.setBalanceChangeDate(balanceChangeDate);
            }

            reportDTOList.add(reportDTO);
        });

        return reportDTOList;
    }






}
