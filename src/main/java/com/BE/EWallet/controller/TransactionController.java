package com.BE.EWallet.controller;

import com.BE.EWallet.constant.Constant;
import com.BE.EWallet.dto.TopupTransactionDTO;
import com.BE.EWallet.dto.TransferDTO;
import com.BE.EWallet.model.Transaction;
import com.BE.EWallet.model.User;
import com.BE.EWallet.repository.TransactionRepo;
import com.BE.EWallet.repository.UserRepo;
import com.BE.EWallet.service.TransactionService;
import com.BE.EWallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TransactionRepo transactionRepo;

    @PostMapping("/transaction/create")
    public ResponseEntity<Object> createTransaction (@RequestBody TransferDTO transferDTO) {
        String username = transferDTO.getUsername();
        User user = userRepo.findByUsername(username);
        User user1 = userRepo.findByUsername(transferDTO.getDestinationUsername());
        String password = transferDTO.getPassword();
        String destinationUsername = transferDTO.getDestinationUsername();
        Integer amount = transferDTO.getAmount();

        if (user == null) {
            return new ResponseEntity<>("user not found", HttpStatus.BAD_REQUEST);
        }
        if (userService.validatePassword(password)) {
            return new ResponseEntity<>("password invalid", HttpStatus.BAD_REQUEST);
        }
        if (userService.validatePassword(transferDTO.getPassword())) {
            return new ResponseEntity<>("password invalid", HttpStatus.BAD_REQUEST);
        }

        if (user.getBan()) {
            return new ResponseEntity<>("user banned", HttpStatus.BAD_REQUEST);
        }

        if (!user.getPassword().equals(transferDTO.getPassword())){
            user.setPasswordAttempt(user.getPasswordAttempt() + 1);

            if (user.getPasswordAttempt() == 3){
                user.setBan(true);
            }

            userRepo.save(user);
            return ResponseEntity.badRequest().body("Wrong Password");
        }
        if (amount < Constant.MIN_TRANSACTION_AMOUNT) {
            return new ResponseEntity<>("Minimum Transaction Amount is 10.000", HttpStatus.BAD_REQUEST);
        }
        if (!transactionService.BalanceExceed(destinationUsername, amount)) {
            return new ResponseEntity<>("Transaction Limit Exceeded", HttpStatus.BAD_REQUEST);
        }

        if (!transactionService.BalanceIsEnough(username, amount)) {
            return new ResponseEntity<>("Not Enough Balance", HttpStatus.BAD_REQUEST);
        }

        if (user.getBan()) {
            return new ResponseEntity<>("user banned", HttpStatus.BAD_REQUEST);
        }

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
        return new ResponseEntity<>("OK", HttpStatus.OK);

    }


    @PostMapping("/transaction/top-up")
    public ResponseEntity<String> topup(@RequestBody TopupTransactionDTO topupTransactionDTO) {
        System.out.println(topupTransactionDTO);

        // Validate user and amount
        User user = validateUser(topupTransactionDTO.getUsername(), topupTransactionDTO.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body("User Not Found");
        }

        if (userService.validatePassword(topupTransactionDTO.getPassword())) {
            return new ResponseEntity<>("Password Invalid", HttpStatus.BAD_REQUEST);
        }

        if (user.getBan()) {
            return new ResponseEntity<>("User Banned", HttpStatus.BAD_REQUEST);
        }

        if (!user.getPassword().equals(topupTransactionDTO.getPassword())){
            user.setPasswordAttempt(user.getPasswordAttempt() + 1);

            if (user.getPasswordAttempt() == 3){
                user.setBan(true);
            }

            userRepo.save(user);
            return ResponseEntity.badRequest().body("Wrong Password");
        }

        if (topupTransactionDTO.getAmount() > Constant.MAX_TOPUP) {
            return ResponseEntity.badRequest().body("Maximal Topup Exceeded");
        }
        int newBalance = (int) (user.getBalance() + topupTransactionDTO.getAmount());
        if (newBalance > Constant.MAX_BALANCE){
            return ResponseEntity.badRequest().body("Maximal Balance Exceeded");
        }
        Transaction transactionTopup = new Transaction();
        transactionTopup.setUsername(topupTransactionDTO.getUsername());
        transactionTopup.setAmount(topupTransactionDTO.getAmount());
        transactionTopup.setType("TOP-UP");
        transactionTopup.setStatus("SETTLED");
        transactionTopup.setUser(user);
        transactionTopup.setDate(LocalDate.now());
        transactionTopup.setBalance_before(user.getBalance());
        transactionTopup.setBalance_after((user.getBalance() + topupTransactionDTO.getAmount()));
       
        transactionRepo.save(transactionTopup);
        userService.executeTopup(user,newBalance);
        return ResponseEntity.ok().body("Berhasil");
    }

    private User validateUser(String username, String password) {
        User user = userService.getByUsername(username);
        return user;
    }
}

