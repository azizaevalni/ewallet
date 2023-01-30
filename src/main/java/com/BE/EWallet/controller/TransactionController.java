package com.BE.EWallet.controller;

import com.BE.EWallet.constant.Constant;
import com.BE.EWallet.dto.CreateTransactionDTO;
import com.BE.EWallet.dto.TopupTransactionDTO;
import com.BE.EWallet.model.User;
import com.BE.EWallet.repository.UserRepo;
import com.BE.EWallet.service.TransactionService;
import com.BE.EWallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.PublicKey;

public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @PostMapping("/transaction/create")
    public ResponseEntity <Object> createTransaction (@RequestBody CreateTransactionDTO createTransactionDTO){
        boolean success = transactionService.createTransaction(createTransactionDTO);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
        @PostMapping("/transaction/topup")
        public ResponseEntity<String> topup (@RequestBody TopupTransactionDTO topupTransactionDTO) {
            // Validate user and amount
            User user = validateUser(topupTransactionDTO.getUsername(), topupTransactionDTO.getPassword());
            if (user == null) {
                return ResponseEntity.badRequest().body("user not found");
            }
            if (topupTransactionDTO.getAmount() > user.getMaxTopup()) {
                return ResponseEntity.badRequest().body("max topup exceeded");
            }
            int newBalance = (int) (user.getBalance() + topupTransactionDTO.getAmount());
            if (newBalance > user.getMaxBalance()) {
                return ResponseEntity.badRequest().body("max balance exceeded");
            }
            // Perform topup
            user.setBalance((long) newBalance);
            // Return success
            return ResponseEntity.ok().build();
        }

        private User validateUser(String username, String password) {
            User user = userService.getByUsername(username);
            return user;
        }
    }

