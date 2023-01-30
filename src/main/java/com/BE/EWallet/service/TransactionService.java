package com.BE.EWallet.service;

import com.BE.EWallet.dto.CreateTransactionDTO;
import com.BE.EWallet.model.Transaction;
import com.BE.EWallet.model.User;
import com.BE.EWallet.repository.TransactionRepo;
import com.BE.EWallet.repository.UserRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.aop.ClassFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Getter
@Setter
@Service
public class TransactionService {

        @Autowired
        private UserRepo userRepo;

        @Autowired
        private TransactionRepo transactionRepo;
        private ClassFilter passwordEncoder;


    public boolean createTransaction(CreateTransactionDTO createTransactionDTO) {
            User user = userRepo.findByUsername(createTransactionDTO.getUsername());
            if (user == null) {
                return false;
            }

            if (!createTransactionDTO.getPassword().equals(user.getPassword())) {
                return false;
            }

            if(user.getBan()){
                return false;
            }

            if(user.getBalance() < createTransactionDTO.getAmount()){
                return false;
            }

            if(createTransactionDTO.getAmount() > 1000000 && user.getKtp() == null){
                return false;
            }

            if(createTransactionDTO.getAmount() > 5000000 && user.getKtp() != null){
                return false;
            }

            if(createTransactionDTO.getAmount() < 10000) {
                return false;
            }

            user.setBalance((long) (user.getBalance() - createTransactionDTO.getAmount() - (createTransactionDTO.getAmount() * 0.125)));
            userRepo.save(user);

            Transaction transaction = new Transaction();
            transaction.setUsername(createTransactionDTO.getUsername());
            transaction.setAmount(createTransactionDTO.getAmount());
            transaction.setType("withdraw");
            transactionRepo.save(transaction);

            return true;
        }
    }

