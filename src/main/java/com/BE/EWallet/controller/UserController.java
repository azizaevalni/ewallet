package com.BE.EWallet.controller;

import com.BE.EWallet.dto.*;
import com.BE.EWallet.model.User;
import com.BE.EWallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws Exception {
        log.debug("REST request to save User : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new Exception("A new user cannot already have an ID");
        }
        UserDTO result = userService.save(userDTO);
        return ResponseEntity
                .created(new URI("/user/" + result.getId()))
                .body(result);
    }
    @PostMapping("/user/registration")
    public ResponseEntity<String> registration(@RequestBody UserRegisDTO userRegisDTO) {
        String username = userRegisDTO.username();
        String password = userRegisDTO.password();
        //validate username
        if (userService.validateUsername(username)) {
            return new ResponseEntity<>("400 - username taken", HttpStatus.BAD_REQUEST);
        }
        //validate password
        if (!userService.validatePassword(password)) {
            return new ResponseEntity<>("400 - password format invalid", HttpStatus.BAD_REQUEST);
        }

        //membuat user baru
        User user = new User(username, password);
        userService.createUser(user);
        return new ResponseEntity<>("200 - OK", HttpStatus.CREATED);
    }
    @GetMapping("/user/{username}/getinfo")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable String username) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserInfoDTO userInfo = new UserInfoDTO(username, user.getKtp());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
    @GetMapping("/user/{username}/getbalance")
    public ResponseEntity<Object> getUserBalance(@PathVariable String username) {
        try {
            UserBalanceDTO userBalanceDTO = userService.getUserBalance(username);
            return ResponseEntity.ok(userBalanceDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("400 - user not found");
        }
    }
    @PutMapping("/user/{username}/unban")
    public ResponseEntity<Object> unban (@PathVariable String username) {
        try {
            String unban = userService.unban(username);
            return ResponseEntity.ok(unban);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("400 - user not found");
        }
    }

    @PutMapping("/user/{username}/addktp")
    public ResponseEntity<String> addKtp(@PathVariable String username, @RequestBody KtpDTO ktpDTO) {

        String ktp = ktpDTO.getKtp();

        if (!userService.findByUsername(username)) {
            return new ResponseEntity<>("400 - user not found", HttpStatus.BAD_REQUEST);
        }
        if (userService.findByKtp(ktp)) {
            return new ResponseEntity<>("400 - ktp has been used by other user", HttpStatus.BAD_REQUEST);
        }
        if (!userService.validateKtp(ktp)) {
            return new ResponseEntity<>("400 - incorrect ktp format", HttpStatus.BAD_REQUEST);
        }
        userService.addKtp(username, ktp);

        return new ResponseEntity<>("200 - OK", HttpStatus.OK);
    }
    @PostMapping ("/user/changepassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePassDTO changePassDTO) {

        String username = changePassDTO.getUsername();
        String newPassword= changePassDTO.getNewPassword();
        String oldPassword = changePassDTO.getOldPassword();


        if (!userService.findByUsername(username)){
            return new ResponseEntity<>("400 - user not found", HttpStatus.BAD_REQUEST);
        }

        if (userService.isBanned(username)){
            return new ResponseEntity<>("400 - user is banned", HttpStatus.BAD_REQUEST);
        }

        if (!userService.validatePassword(username, oldPassword)){
            return new ResponseEntity<>("400 - format invalid", HttpStatus.BAD_REQUEST);
        }

        if (!userService.validateNewPassword(newPassword)){
            return new ResponseEntity<>("400 - format invalid"
                    , HttpStatus.BAD_REQUEST);
        }

        userService.changePassword(username, newPassword);

        return new ResponseEntity<>("200 - OK", HttpStatus.OK);
    }




}
