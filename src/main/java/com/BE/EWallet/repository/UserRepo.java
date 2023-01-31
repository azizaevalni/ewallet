package com.BE.EWallet.repository;

import com.BE.EWallet.dto.UserDTO;
import com.BE.EWallet.model.User;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long > {
    User findByUsername (String username);
    User findByKtp (String ktp);
}