package com.BE.EWallet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class UserDTO {
    private Long id;

    private String username;
    private String  password;
    private Long ktp;
    private boolean ban = false;

    public UserDTO(Long id, String username, String password, Long ktp, boolean ban) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.ktp = ktp;
        this.ban = ban;
    }
}
