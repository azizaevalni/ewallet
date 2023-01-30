package com.BE.EWallet.mapper;

import com.BE.EWallet.dto.UserDTO;
import com.BE.EWallet.model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper <UserDTO, User> {
}
