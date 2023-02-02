package com.BE.EWallet.service;

import com.BE.EWallet.constant.Constant;
import com.BE.EWallet.dto.TransferDTO;
import com.BE.EWallet.dto.TransferResponseDTO;
import com.BE.EWallet.model.Transaction;
import com.BE.EWallet.model.User;
import com.BE.EWallet.repository.TransactionRepo;
import com.BE.EWallet.repository.UserRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Service
public class TransactionService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    public TransferResponseDTO createTransaction (TransferDTO transferDTO){
        String username = transferDTO.getUsername();
        User user = userRepo.findByUsername(username);
        User user1 = userRepo.findByUsername(transferDTO.getDestinationUsername());
        String password = transferDTO.getPassword();
        String destinationUsername = transferDTO.getDestinationUsername();
        Integer amount = transferDTO.getAmount();

        Transaction transactionOut = new Transaction();
        transactionOut.setUsername(transferDTO.getUsername());
        transactionOut.setAmount(transferDTO.getAmount());
        transactionOut.setType("TRANSFER-OUT");
        transactionOut.setStatus("SETTLED");
        transactionOut.setDate(LocalDate.now());
        transactionOut.setBalance_before(user.getBalance());
        transactionOut.setBalance_after((long) (user.getBalance() - transferDTO.getAmount() - (transferDTO.getAmount()*Constant.TRANSACTION_TAX)));

        Transaction transactionIn = new Transaction();
        transactionIn.setUsername(transferDTO.getDestinationUsername());
        transactionIn.setAmount(transferDTO.getAmount());
        transactionIn.setType("TRANSFER-IN");
        transactionIn.setStatus("SETTLED");
        transactionIn.setDate(LocalDate.now());
        transactionIn.setBalance_before(user1.getBalance());
        transactionIn.setBalance_after(user1.getBalance() + transferDTO.getAmount());

        user.setBalance((long) (user.getBalance() - transferDTO.getAmount() - (transferDTO.getAmount()*Constant.TRANSACTION_TAX)));
        user1.setBalance(user1.getBalance() + transferDTO.getAmount());
        transactionIn.setUser(user1);
        transactionOut.setUser(user);

        transactionRepo.saveAll(List.of(transactionIn,transactionOut));

        TransferResponseDTO transferResponseDTO = new TransferResponseDTO();
        transferResponseDTO.setTrx_id(transactionIn.getTrx_id());
        transferResponseDTO.setOriginUsername(transactionOut.getUsername());
        transferResponseDTO.setDestinationUsername(transactionIn.getUsername());
        transferResponseDTO.setAmount(transferDTO.getAmount());
        transferResponseDTO.setStatus("SETTLED");

        return transferResponseDTO;
    }
    public boolean BalanceIsEnough (String username, Integer amount){
        User user = userRepo.findByUsername(username);
        if ((user.getBalance() - amount * Constant.TRANSACTION_TAX)< Constant.MIN_BALANCE){
            return false;
        } return  true;

        }
        public boolean BalanceExceed(String username, Integer amount){
            User user = userRepo.findByUsername(username);
            if (user.getBalance() + amount > Constant.MAX_BALANCE){
                return false;
            } return  true;
        }



    }

