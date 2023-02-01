package com.BE.EWallet.service;

import com.BE.EWallet.constant.Constant;
import com.BE.EWallet.dto.UserBalanceDTO;
import com.BE.EWallet.dto.UserDTO;
import com.BE.EWallet.mapper.UserMapper;
import com.BE.EWallet.model.User;
import com.BE.EWallet.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.BE.EWallet.constant.Constant.MAX_TRANSACTION_AMOUNT;

@RequiredArgsConstructor
@Service
    public class UserService {
    @Autowired
    private final UserMapper userMapper;
    private final UserRepo userRepo;

    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user = userRepo.save(user);
        return userMapper.toDto(user);
    }

    public boolean validateUsername(String username) {
        if (userRepo.findByUsername(username) != null) {
            return false;
        }
        return true;
    }

    public boolean validatePassword(@NotNull String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#^?&])[A-Za-z\\d@$!%*#^?&]{10,}$";
        if (password.matches(regex)) {
            //valid password
            return false;
        }
        //invalid password
        return true;
    }

    public void createUser(User user) {
        userRepo.save(user);
    }

    public boolean findByUsername(String username) {
        if (userRepo.findByUsername(username) != null) {
            return true;
        }
        return false;
    }


    public UserBalanceDTO getUserBalance(String username) {
        var user = userRepo.findByUsername(username);
        user.getBalance();
        UserBalanceDTO userBalanceDTO = new UserBalanceDTO(user.getBalance(), MAX_TRANSACTION_AMOUNT);
        return userBalanceDTO;
    }

    public String unban(String username) {
        var user = userRepo.findByUsername(username);
        user.setPasswordAttempt(0);
        user.setBan(false);
        User result = userRepo.save(user);
        return "OK";
    }

    public boolean findByKtp(String ktp) {

        return userRepo.findByKtp(ktp) != null;
    }

    public void addKtp(String username, String ktp) {
        User user = userRepo.findByUsername(username);
        user.setKtp(ktp);
        user.setTransactionLimit(Constant.MAX_TRANSACTION_AMOUNT_WITH_KTP);
        userRepo.save(user);
    }

    public boolean validateKtp(String ktp) {
        String regex = String.format("^[0-9]{%d}$", Constant.KTP_LENGTH);
        return ktp.matches(regex);
    }

    public User getByUsername(String username) {

        return userRepo.findByUsername(username);
    }

    public Boolean validatePassword(String username, String passwordInput){
        User user = userRepo.findByUsername(username);
        String password = user.getPassword();

        int incorrectPasswordCount = user.getPasswordAttempt();

        if (!password.equals(passwordInput)){
            user.setPasswordAttempt(incorrectPasswordCount + 1);
            if (user.getPasswordAttempt() >= Constant.MAX_TRY){
                user.setBan(true);
                user.setPasswordAttempt(0);
            }
            userRepo.save(user);
            return false;
        }
        return true;
}

    public void changePassword(String username, String password){
        User user = userRepo.findByUsername(username);
        user.setPassword(password);
        userRepo.save(user);
    }
    public Boolean isBanned(String username){
        User user = userRepo.findByUsername(username);
        return user.getBan();
    }

    public boolean validateNewPassword(String newPassword) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?_&(){},./<>?;':\"])[A-Za-z\\d@$!%*#?_&(){},./<>?;':\"]{10,}$";
        return newPassword.matches(regex);
    }

    public void executeTopup(User user, int newBalance) {
        user.setBalance((long) newBalance);
        userRepo.save(user);
    }
}
