package com.BE.EWallet.service;

import com.BE.EWallet.constant.Constant;
import com.BE.EWallet.model.User;
import com.BE.EWallet.repository.TransactionRepo;
import com.BE.EWallet.repository.UserRepo;
import lombok.Getter;
import lombok.Setter;
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

