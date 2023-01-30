package com.BE.EWallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassDTO {
    String username;
    String oldPassword;
    String newPassword;

}
