package com.BE.EWallet.repository;

import com.BE.EWallet.model.Transaction;
import com.BE.EWallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction, Long > {
}
