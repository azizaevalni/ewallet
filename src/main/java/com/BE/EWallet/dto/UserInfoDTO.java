package com.BE.EWallet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class UserInfoDTO  {
    String username;
    String  ktp;

    public UserInfoDTO(String username, String ktp) {
        this.username = username;
        this.ktp = ktp;
    }
}
